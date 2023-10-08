package com.llc.moviedb.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.llc.moviebd.R

/**
 * Created by mr on 14-8-5.
 * An extended PopupWindow which could add a pointer to the anchor view.
 * You could set your own pointer image,
 * this widget will compute the pointer location for you automatically.
 */
class PointerPopupWindow constructor(
    private val context: Context?,
    width: Int,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) : PopupWindow(width, height) {
    private val mContainer: LinearLayout
    private val mAnchorImage: ImageView
    private val mContent: FrameLayout?
    var marginScreen = 0
    var offsetMode = AlignMode.AUTO_OFFSET

    init {
//        if (width < 0) {
//            throw RuntimeException("You must specify the window width explicitly(do not use WRAP_CONTENT or MATCH_PARENT)!!!")
//        }
        mContainer = LinearLayout(context)
        mContainer.orientation = LinearLayout.VERTICAL
        mAnchorImage = ImageView(context)
        mContent = context?.let { FrameLayout(it) }
        setBackgroundDrawable(ColorDrawable())
        isOutsideTouchable = true
        isFocusable = true
    }

    fun setPointerImageDrawable(d: Drawable?) {
        mAnchorImage.setImageDrawable(d)
    }

    fun setPointerImageRes(res: Int) {
        mAnchorImage.setImageResource(res)
    }

    fun setPointerImageBitmap(bitmap: Bitmap?) {
        mAnchorImage.setImageBitmap(bitmap)
    }

    override fun setContentView(contentView: View?) {
        if (contentView != null) {
            mContainer.removeAllViews()
            mContent?.addView(
                contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mContainer.addView(
                mContent,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            mContainer.addView(
                mAnchorImage,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            //mContent?.removeAllViews()

            super.setContentView(mContainer)
            //super.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun dpToPx( dp: Float): Float {
        val resources = context!!.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        )
    }

    override fun setBackgroundDrawable(background: Drawable) {
        mContent?.setBackgroundDrawable(background)
        super.setBackgroundDrawable(ColorDrawable())
    }

    fun customShowAsPointer(anchor: View, yoff: Int) {
        customShowAsPointer(anchor, 0, yoff)
    }

    fun customShowAsPointer(anchor: View, mXoff: Int = 0, mYoff: Int = 0) {
        // get location and size
        var xoff = mXoff
        var yoff = mYoff
        val displayFrame = Rect()
        anchor.getWindowVisibleDisplayFrame(displayFrame)
        val displayFrameWidth = displayFrame.right - displayFrame.left
        val loc = IntArray(2)
        anchor.getLocationInWindow(loc) //get anchor location
        if (offsetMode == AlignMode.AUTO_OFFSET) {
            // compute center offset rate
            val offCenterRate = (displayFrame.centerX() - loc[0]) / displayFrameWidth.toFloat()
            xoff = ((anchor.width - width) / 2 + offCenterRate * width / 2).toInt()
        } else if (offsetMode == AlignMode.CENTER_FIX) {
            xoff = (anchor.width - width) / 2
        }
        val left = loc[0] + xoff
        val right = left + width
        // reset x offset to display the window fully in the screen
        if (right > displayFrameWidth - marginScreen) {
            xoff = displayFrameWidth - marginScreen - width - loc[0]
        }
        if (left < displayFrame.left + marginScreen) {
            xoff = displayFrame.left + marginScreen - loc[0]
        }
       //yoff =  loc[1].minus(60)
        computePointerLocation(anchor, xoff)
        Log.d("POP_UP", "x : $xoff, y : ${loc[1]}")
        super.showAsDropDown(anchor, xoff, yoff.minus(200))
    }

    private fun computePointerLocation(anchor: View, xoff: Int) {
        val aw = anchor.width
        val dw = mAnchorImage.drawable.intrinsicWidth
        mAnchorImage.setPadding((aw - dw) / 2 - xoff, 0, 0, 0)
    }

    @Deprecated("")
    /**
     * won't take effect in this widget,
     */
    override fun setClippingEnabled(enabled: Boolean) {
        super.setClippingEnabled(enabled)
    }

    enum class AlignMode {
        /**
         * default align mode,align the left|bottom of the anchor view
         */
        DEFAULT,

        /**PopupWindowMain
         * align center of the anchor view
         */
        CENTER_FIX,

        /**
         * according to the location of the anchor view in the display window,
         * auto offset the popup window to display center.
         */
        AUTO_OFFSET
    }
}