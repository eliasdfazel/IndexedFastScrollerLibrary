/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/9/20 12:54 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactory
import net.geeksempire.indexedfastscroller.library.Factory.convertToDp
import net.geeksempire.indexedfastscroller.library.IndexedFastScroller
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.databinding.BottomFastScrollerIndexViewBinding

fun IndexedFastScroller.setupBottomIndex(
    context: Context,
    rootView: ViewGroup,
    bottomFastScrollerIndexViewBinding: BottomFastScrollerIndexViewBinding,
    indexedFastScrollerFactory: IndexedFastScrollerFactory,
    finalPopupHorizontalOffset: Int) {

    //Root View
    rootView.addView(bottomFastScrollerIndexViewBinding.root)

    bottomFastScrollerIndexViewBinding.indexView.orientation = LinearLayoutCompat.HORIZONTAL

    val indexViewLayoutParams = bottomFastScrollerIndexViewBinding.indexView.layoutParams as ViewGroup.LayoutParams

    indexViewLayoutParams.height = 30.convertToDp(context)
    indexViewLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

    bottomFastScrollerIndexViewBinding.indexView.layoutParams = indexViewLayoutParams

    val nestedIndexScrollViewLayoutParams = bottomFastScrollerIndexViewBinding.nestedIndexScrollView.layoutParams as ConstraintLayout.LayoutParams

    nestedIndexScrollViewLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
    nestedIndexScrollViewLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

    bottomFastScrollerIndexViewBinding.nestedIndexScrollView.layoutParams = nestedIndexScrollViewLayoutParams

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

    //Popup Text
    val popupIndexLayoutParams =
        bottomFastScrollerIndexViewBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.bottomMargin = finalPopupHorizontalOffset

    bottomFastScrollerIndexViewBinding.popupIndex.layoutParams = popupIndexLayoutParams

    bottomFastScrollerIndexViewBinding.root
        .setPadding(
            indexedFastScrollerFactory.rootPaddingStart, indexedFastScrollerFactory.rootPaddingTop,
            indexedFastScrollerFactory.rootPaddingEnd, indexedFastScrollerFactory.rootPaddingBottom
        )

    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactory.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_bottom_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactory.popupBackgroundTint)

    bottomFastScrollerIndexViewBinding.popupIndex.background = popupIndexBackground
    bottomFastScrollerIndexViewBinding.popupIndex.typeface = indexedFastScrollerFactory.popupTextFont

    bottomFastScrollerIndexViewBinding.popupIndex.setTextColor(indexedFastScrollerFactory.popupTextColor)
    bottomFastScrollerIndexViewBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactory.popupTextSize
    )
}