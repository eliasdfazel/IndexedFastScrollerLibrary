/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/9/20 12:22 PM
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
import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactory
import net.geeksempire.indexedfastscroller.library.IndexedFastScroller
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.databinding.FastScrollerIndexViewBinding

fun IndexedFastScroller.setupLeftIndex(
    context: Context,
    rootView: ViewGroup,
    fastScrollerIndexViewBinding: FastScrollerIndexViewBinding,
    indexedFastScrollerFactory: IndexedFastScrollerFactory,
    finalPopupHorizontalOffset: Int) {

    when (rootView) {
        is ConstraintLayout -> {

            //Root View
            rootView.addView(fastScrollerIndexViewBinding.root)

            val rootLayoutParams =
                fastScrollerIndexViewBinding.root.layoutParams as ConstraintLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.topToTop = rootView.id
            rootLayoutParams.bottomToBottom = rootView.id
            rootLayoutParams.startToStart = rootView.id

            fastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            //Root View
            rootView.addView(fastScrollerIndexViewBinding.root)

            val rootLayoutParams =
                fastScrollerIndexViewBinding.root.layoutParams as RelativeLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, rootView.id)

            fastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

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

    //Popup Text
    val popupIndexLayoutParams =
        fastScrollerIndexViewBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.marginEnd = finalPopupHorizontalOffset

    fastScrollerIndexViewBinding.popupIndex.layoutParams = popupIndexLayoutParams

    fastScrollerIndexViewBinding.root
        .setPadding(
            indexedFastScrollerFactory.rootPaddingStart, indexedFastScrollerFactory.rootPaddingTop,
            indexedFastScrollerFactory.rootPaddingEnd, indexedFastScrollerFactory.rootPaddingBottom
        )

    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactory.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_left_popup_background
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