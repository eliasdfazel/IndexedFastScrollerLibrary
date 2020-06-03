/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/3/20 2:37 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Sides.Bottom.Extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.indexedfastscroller.library.Factory.indexedFastScrollerFactoryWatch
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Bottom.BottomSideIndexedFastScrollerWatch
import net.geeksempire.indexedfastscroller.library.databinding.BottomFastScrollerIndexViewBinding

fun BottomSideIndexedFastScrollerWatch.setupBottomIndex(
    context: Context,
    rootView: ViewGroup,
    bottomFastScrollerIndexViewBinding: BottomFastScrollerIndexViewBinding,
    indexedFastScrollerFactoryWatch: indexedFastScrollerFactoryWatch,
    finalPopupVerticalOffset: Int) : BottomSideIndexedFastScrollerWatch {

    //Root View
    rootView.addView(bottomFastScrollerIndexViewBinding.root)

    when (rootView) {
        is ConstraintLayout -> {

            val rootLayoutParams =
                bottomFastScrollerIndexViewBinding.root.layoutParams as ConstraintLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            rootLayoutParams.bottomToBottom = rootView.id
            rootLayoutParams.startToStart = rootView.id
            rootLayoutParams.endToEnd = rootView.id

            bottomFastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            val rootLayoutParams =
                bottomFastScrollerIndexViewBinding.root.layoutParams as RelativeLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            rootLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, rootView.id)

            bottomFastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

        }
        else -> {

            val unsupportedOperationException = UnsupportedOperationException()
            unsupportedOperationException.stackTrace = arrayOf(
                StackTraceElement(
                    "${this@setupBottomIndex.javaClass.simpleName}",
                    "initializeIndexView()",
                    "${this@setupBottomIndex.javaClass.simpleName}",
                    77
                )
            )
            unsupportedOperationException.addSuppressed(Throwable(context.getString(R.string.supportedRootError)))

            throw unsupportedOperationException
        }
    }

    bottomFastScrollerIndexViewBinding.root
        .setPadding(
            indexedFastScrollerFactoryWatch.rootPaddingStart, indexedFastScrollerFactoryWatch.rootPaddingTop,
            indexedFastScrollerFactoryWatch.rootPaddingEnd, indexedFastScrollerFactoryWatch.rootPaddingBottom
        )

    //Popup Text
    val popupIndexLayoutParams =
        bottomFastScrollerIndexViewBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.bottomMargin = finalPopupVerticalOffset

    bottomFastScrollerIndexViewBinding.popupIndex.layoutParams = popupIndexLayoutParams

    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactoryWatch.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_bottom_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactoryWatch.popupBackgroundTint)

    bottomFastScrollerIndexViewBinding.popupIndex.background = popupIndexBackground
    bottomFastScrollerIndexViewBinding.popupIndex.typeface = indexedFastScrollerFactoryWatch.popupTextFont

    bottomFastScrollerIndexViewBinding.popupIndex.setTextColor(indexedFastScrollerFactoryWatch.popupTextColor)
    bottomFastScrollerIndexViewBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactoryWatch.popupTextSize
    )

    return this@setupBottomIndex
}