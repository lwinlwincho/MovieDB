package com.llc.moviedb.ui.home.detail_movie

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.moviebd.R
import com.llc.moviebd.databinding.CardviewPopupWindowBinding
import com.llc.moviebd.databinding.FragmentMovieCollapseToolbarBinding
import com.llc.moviedb.data.model.Description
import com.llc.moviedb.data.model.MovieDetailModel
import com.llc.moviedb.extension.PointerPopupWindow
import com.llc.moviedb.extension.loadFromUrl
import com.llc.moviedb.extension.toHourMinute
import com.llc.moviedb.network.IMAGE_URL
import com.llc.moviedb.singleEvent.observeEvent
import com.llc.moviedb.ui.home.cast.CastItemAdapter
import com.llc.moviedb.ui.home.genre.GenreItemAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailFragment : Fragment(), DescriptionItemAdapter.DescriptionDelegate {

    private val viewModel: MovieDetailViewModel by viewModels()

    private var _binding: FragmentMovieCollapseToolbarBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()

    private val genreItemAdapter: GenreItemAdapter by lazy {
        GenreItemAdapter()
    }

    private val castItemAdapter: CastItemAdapter by lazy {
        CastItemAdapter()
    }

    private val descriptionItemAdapter: DescriptionItemAdapter by lazy {
        DescriptionItemAdapter(this)
    }

    var descriptionList = mutableListOf<Description>(
        Description(1, "Once", false),
        Description(2, "upon", true),
        Description(3, "a", false),
        Description(4, "time", true),
        Description(5, "in", false),
        Description(6, "a", true),
        Description(7, "quiet", false),
        Description(8, "village", true),
        Description(9, "nestled", false),
        Description(10, "between", true),
        Description(11, "rolling", false),
        Description(12, "hills", true),
        Description(13, "stood", false),
        Description(14, "a", true),
        Description(15, "majestic", false),
        Description(16, "oak", true),
        Description(17, "tree.\n", false),
        Description(18, "This", true),
        Description(19, "oak", false),
        Description(20, "tree", true),
        Description(21, "was", false),
        Description(22, "unlike", true),
        Description(23, "any", false),
        Description(24, "other", true),
        Description(25, "in", false),
        Description(26, "the", true),
        Description(27, "village;", false),
        Description(28, "its", true),
        Description(29, "branches", false),
        Description(30, "were", true),
        Description(31, "wide", false),
        Description(32, "and", true),
        Description(33, "Once", false),
        Description(34, "upon", true),
        Description(35, "a", false),
        Description(36, "time", true),
        Description(37, "in", false),
        Description(38, "a", true),
        Description(39, "quiet", false),
        Description(40, "village", true),
        Description(41, "nestled", false),
        Description(42, "between", true),
        Description(43, "rolling", false),
        Description(44, "hills", true),
        Description(45, "stood", false),
        Description(46, "a", true),
        Description(47, "majestic", false),
        Description(48, "oak", true),
        Description(49, "tree.\n", false),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieCollapseToolbarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId
        viewModel.getMovieDetail(movieId)

        viewModel.detailUIEvent.observe(viewLifecycleOwner) { detailResult ->
            when (detailResult) {
                is MovieDetailEvent.Loading -> {
                    binding.detailProgressBar.visibility = View.VISIBLE
                }

                is MovieDetailEvent.Success -> {
                    // binding.detailScrollView.visibility = View.VISIBLE
                    binding.detailProgressBar.visibility = View.GONE
                    bindDetailMovie(detailResult.movieDetailModel)
                }

                is MovieDetailEvent.Error -> {
                    binding.detailProgressBar.visibility = View.GONE
                    showMessage(detailResult.error)
                }

                is MovieDetailEvent.Credits -> {
                    castItemAdapter.submitList(detailResult.creditModel.cast)
                }

                else -> {}
            }
        }

        viewModel.favouriteEvent.observeEvent(viewLifecycleOwner) { favouriteEvent ->
            when (favouriteEvent) {
                is MovieDetailEvent.SuccessAdded -> {
                    binding.ivBookMark.setImageResource(R.drawable.ic_bookmark_filled_24)
                    if (favouriteEvent.message.isNotBlank()) showMessage(favouriteEvent.message)
                }

                is MovieDetailEvent.SuccessRemoved -> {
                    binding.ivBookMark.setImageResource(R.drawable.ic_bookmark_border_gray)
                    showMessage(favouriteEvent.message)
                }

                is MovieDetailEvent.Error -> {
                    showMessage(favouriteEvent.error)
                }

                else -> {}
            }
        }

        viewModel.favouriteStatusEvent.observeEvent(viewLifecycleOwner) { isFavourite ->
            val iconBookMarkId = getImageResourceId(isFavourite)
            binding.ivBookMark.setImageResource(iconBookMarkId)
        }

        binding.rvGenres.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = genreItemAdapter
        }

        binding.rvCasts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = castItemAdapter
        }

        /*binding.rvDescription.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = descriptionItemAdapter
        }*/

        binding.rvDescription.adapter = descriptionItemAdapter
        val choiceLayoutManager = FlexboxLayoutManager(context)
        choiceLayoutManager.flexDirection = FlexDirection.ROW
        choiceLayoutManager.justifyContent = JustifyContent.SPACE_AROUND
        binding.rvDescription.layoutManager = choiceLayoutManager

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        /* binding.tvDescription.setOnClickListener {
             popUpWindow()
         }*/
    }

    private fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext()).setMessage(message)
            .setPositiveButton("Ok") { _, _ -> }.show()
    }

    private fun getImageResourceId(isFavourite: Boolean): Int {
        return if (isFavourite) R.drawable.ic_bookmark_filled_24
        else R.drawable.ic_bookmark_border_gray
    }

    private fun bindDetailMovie(detailDataModel: MovieDetailModel) {

        //use this code in extension function
        /* Glide.with(requireContext())
             .load(IMAGE_URL + detailDataModel.backdrop_path)
             .transition(DrawableTransitionOptions.withCrossFade())
             .into(binding.ivDetail)*/

        viewModel.checkFavouriteMovie(detailDataModel.id)

        with(binding) {
            ivDetail.loadFromUrl(IMAGE_URL + detailDataModel.backdrop_path)
            tvDetailName.text = detailDataModel.original_title
            tvLength.text = detailDataModel.runtime.toHourMinute()
            tvRating.text = (detailDataModel.vote_average / 2).toString()

            tvDetailStarRate.text = root.context.getString(
                R.string.vote_average_format,
                detailDataModel.vote_average.toString()
            )

            tvLanguage.text = when (detailDataModel.original_language) {
                "en" -> "English"
                "ko" -> "Korea"
                "ja" -> "Japan"
                "fr" -> "France"
                "ch" -> "China"
                else -> detailDataModel.original_language
            }

            genreItemAdapter.submitList(detailDataModel.genres)

            // tvDescription.text = detailDataModel.overview
            descriptionItemAdapter.submitList(descriptionList)

            ivBookMark.setOnClickListener {
                viewModel.toggleFavourite(detailDataModel)
            }
        }
    }

    //it use for simple text view and not recyclerview
    fun popupWindow(adapterPosition: Int) {
        // val location = locateView(it)
        val popupView = LayoutInflater.from(context).inflate(R.layout.cardview_popup_window, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val locationList = ArrayList<Int>()
        val viewLocation = getLocationOfView(adapterPosition, locationList)

        popupWindow.showAtLocation(
            view, Gravity.NO_GRAVITY, viewLocation[0], viewLocation[1].minus(60)
        )
        Toast.makeText(
            requireContext(),
            "Touch Location: x = ${viewLocation[0]}, y = ${viewLocation[1].minus(60)}",
            Toast.LENGTH_SHORT
        ).show()

        popupView?.setOnTouchListener { _, _ ->
            popupWindow?.dismiss()
            true
        }
    }

    fun locateView(v: View?): Rect? {
        val locationList = IntArray(2)
        if (v == null) return null
        try {
            v.getLocationOnScreen(locationList)
        } catch (npe: NullPointerException) {
            //Happens when the view doesn't exist on screen anymore.
            return null
        }
        val location = Rect()
        location.left = locationList[0]
        location.top = locationList[1]
        location.right = location.left + v.width
        location.bottom = location.top + v.height
        return location
    }

    fun getLocationOfView(adapterPosition: Int, location: ArrayList<Int>): ArrayList<Int> {
        location.clear()
        val descriptionViewHolder: RecyclerView.ViewHolder? =
            binding.rvDescription.findViewHolderForAdapterPosition(adapterPosition)

        descriptionViewHolder?.itemView?.let {
            getLocationScreenList(it).toList()
        }?.let {
            location.addAll(it)
        }
        return location
    }

    fun getLocationScreenList(view: View): IntArray {
        val viewLocationList = IntArray(2)
        view.getLocationOnScreen(viewLocationList)
        return viewLocationList
    }

    //it use custom class "PointerPopupWindow" when click within recycler view simple spannable string , show pop up
    override fun onTapDescription(view: View) {
        val popupView = CardviewPopupWindowBinding.inflate(layoutInflater)
        Log.d("POP_UP", "width : ${popupView.root.width}")
        val pop = PointerPopupWindow(requireContext(),500) //specify the window width explicitly

        pop.contentView = popupView.root
        pop.setPointerImageRes(R.drawable.ic_popup_pointer)
        pop.customShowAsPointer(view)
    }
}