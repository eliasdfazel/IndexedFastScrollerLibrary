/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/3/20 2:37 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Sides.Left.Extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.indexedfastscroller.library.Factory.indexedFastScrollerFactoryWatch
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Left.LeftSideIndexedFastScrollerWatch
import net.geeksempire.indexedfastscroller.library.databinding.LeftFastScrollerIndexViewBinding

fun LeftSideIndexedFastScrollerWatch.setupLeftIndex(
    context: Context,
    rootView: ViewGroup,
    leftFastScrollerIndexViewBinding: LeftFastScrollerIndexViewBinding,
    indexedFastScrollerFactoryWatch: indexedFastScrollerFactoryWatch,
    finalPopupHorizontalOffset: Int) : LeftSideIndexedFastScrollerWatch {

    when (rootView) {
        is ConstraintLayout -> {

            //Root View
            rootView.addView(leftFastScrollerIndexViewBinding.root)

            val rootLayoutParams =
                leftFastScrollerIndexViewBinding.root.layoutParams as ConstraintLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.topToTop = rootView.id
            rootLayoutParams.bottomToBottom = rootView.id
            rootLayoutParams.startToStart = rootView.id

            leftFastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            //Root View
            rootView.addView(leftFastScrollerIndexViewBinding.root)

            val rootLayoutParams =
                leftFastScrollerIndexViewBinding.root.layoutParams as RelativeLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, rootView.id)

            leftFastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

        }
        else -> {

            val unsupportedOperationException = UnsupportedOperationException()
            unsupportedOperationException.stackTrace = arrayOf(
                StackTraceElement(
                    "${this@setupLeftIndex.javaClass.simpleName}",
                    "initializeIndexView()",
                    "${this@setupLeftIndex.javaClass.simpleName}",
                    77
                )
            )
            unsupportedOperationException.addSuppressed(Throwable(context.getString(R.string.supportedRootError)))

            throw unsupportedOperationException
        }
    }

    leftFastScrollerIndexViewBinding.root
        .setPadding(
            indexedFastScrollerFactoryWatch.rootPaddingStart, indexedFastScrollerFactoryWatch.rootPaddingTop,
            indexedFastScrollerFactoryWatch.rootPaddingEnd, indexedFastScrollerFactoryWatch.rootPaddingBottom
        )

    //Popup Text
    val popupIndexLayoutParams =
        leftFastScrollerIndexViewBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.marginEnd = finalPopupHorizontalOffset

    leftFastScrollerIndexViewBinding.popupIndex.layoutParams = popupIndexLayoutParams

    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactoryWatch.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_left_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactoryWatch.popupBackgroundTint)

    leftFastScrollerIndexViewBinding.popupIndex.background = popupIndexBackground
    leftFastScrollerIndexViewBinding.popupIndex.typeface = indexedFastScrollerFactoryWatch.popupTextFont

    leftFastScrollerIndexViewBinding.popupIndex.setTextColor(indexedFastScrollerFactoryWatch.popupTextColor)
    leftFastScrollerIndexViewBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactoryWatch.popupTextSize
    )

    return this@setupLeftIndex
}