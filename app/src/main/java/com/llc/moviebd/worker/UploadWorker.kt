package com.llc.moviebd.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : Worker(appContext, workerParameters) {

    override fun doWork(): Result {

        // Indicate whether the work finished successfully with the Result
        return try {
            uploadImages()
            Result.success()
        }catch (e:Exception){
            Result.failure()
        }
    }

    // Do the work here--in this case, upload the images.
    private fun uploadImages() {
        // repository.uploadImage(image)
        TODO("Not yet implemented")
    }
}