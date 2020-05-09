/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/9/20 12:31 PM
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
import net.geeksempire.indexedfastscroller.library.databinding.FastScrollerIndexViewBinding

fun IndexedFastScroller.setupBottomIndex(
    context: Context,
    rootView: ViewGroup,
    fastScrollerIndexViewBinding: FastScrollerIndexViewBinding,
    indexedFastScrollerFactory: IndexedFastScrollerFactory,
    finalPopupHorizontalOffset: Int) {

    //Root View
    rootView.addView(fastScrollerIndexViewBinding.root)

    fastScrollerIndexViewBinding.indexView.orientation = LinearLayoutCompat.HORIZONTAL

    val indexViewLayoutParams = fastScrollerIndexViewBinding.indexView.layoutParams as ViewGroup.LayoutParams

    indexViewLayoutParams.height = 30.convertToDp(context)
    indexViewLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

    fastScrollerIndexViewBinding.indexView.layoutParams = indexViewLayoutParams

    val nestedIndexScrollViewLayoutParams = fastScrollerIndexViewBinding.nestedIndexScrollView.layoutParams as ConstraintLayout.LayoutParams

    nestedIndexScrollViewLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
    nestedIndexScrollViewLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

    fastScrollerIndexViewBinding.nestedIndexScrollView.layoutParams = nestedIndexScrollViewLayoutParams

    when (rootView) {
        is ConstraintLayout -> {

            val rootLayoutParams =
                fastScrollerIndexViewBinding.root.layoutParams as ConstraintLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            rootLayoutParams.bottomToBottom = rootView.id
            rootLayoutParams.startToStart = rootView.id
            rootLayoutParams.endToEnd = rootView.id

            fastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            val rootLayoutParams =
                fastScrollerIndexViewBinding.root.layoutParams as RelativeLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            rootLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, rootView.id)

            fastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

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
        fastScrollerIndexViewBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.bottomMargin = finalPopupHorizontalOffset

    fastScrollerIndexViewBinding.popupIndex.layoutParams = popupIndexLayoutParams

    fastScrollerIndexViewBinding.root
        .setPadding(
            indexedFastScrollerFactory.rootPaddingStart, indexedFastScrollerFactory.rootPaddingTop,
            indexedFastScrollerFactory.rootPaddingEnd, indexedFastScrollerFactory.rootPaddingBottom
        )

    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactory.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_bottom_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactory.popupBackgroundTint)

    fastScrollerIndexViewBinding.popupIndex.background = popupIndexBackground
    fastScrollerIndexViewBinding.popupIndex.typeface = indexedFastScrollerFactory.popupTextFont

    fastScrollerIndexViewBinding.popupIndex.setTextColor(indexedFastScrollerFactory.popupTextColor)
    fastScrollerIndexViewBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactory.popupTextSize
    )
}