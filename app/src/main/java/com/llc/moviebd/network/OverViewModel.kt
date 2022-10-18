package com.llc.moviebd.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class OverViewModel : ViewModel() {

    private val _photo = MutableLiveData<MoviePhoto>()
    val photo: LiveData<MoviePhoto> = _photo

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    // Call getMarsPhotos() on init so we can display status immediately.
    init {
        getMoivePhoto()
    }

    private fun getMoivePhoto() {

        viewModelScope.launch {
            try {
                _photo.value = MovieAPI.retrofitService.getPhoto()[0]

                _status.value = "Success: Movie Properties Retrieve"
                //_date.value = "First Movie image URL : ${_photo.value!!.imgSrcUrl}"
            } catch (e: Exception) {
                _status.value = "Falure:${e.message}"
            }
        }
    }
}