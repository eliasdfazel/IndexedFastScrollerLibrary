/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/3/20 9:26 AM
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
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactory
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Left.LeftSideIndexedFastScrollerPhone
import net.geeksempire.indexedfastscroller.library.databinding.LeftFastScrollerIndexViewPhoneBinding

fun LeftSideIndexedFastScrollerPhone.setupLeftIndex(
    context: Context,
    rootView: ViewGroup,
    leftFastScrollerIndexViewPhoneBinding: LeftFastScrollerIndexViewPhoneBinding,
    indexedFastScrollerFactory: IndexedFastScrollerFactory,
    finalPopupHorizontalOffset: Int) : LeftSideIndexedFastScrollerPhone {

    when (rootView) {
        is ConstraintLayout -> {

            //Root View
            rootView.addView(leftFastScrollerIndexViewPhoneBinding.root)

            val rootLayoutParams =
                leftFastScrollerIndexViewPhoneBinding.root.layoutParams as ConstraintLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.topToTop = rootView.id
            rootLayoutParams.bottomToBottom = rootView.id
            rootLayoutParams.startToStart = rootView.id

            leftFastScrollerIndexViewPhoneBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            //Root View
            rootView.addView(leftFastScrollerIndexViewPhoneBinding.root)

            val rootLayoutParams =
                leftFastScrollerIndexViewPhoneBinding.root.layoutParams as RelativeLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, rootView.id)

            leftFastScrollerIndexViewPhoneBinding.root.layoutParams = rootLayoutParams

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

    leftFastScrollerIndexViewPhoneBinding.root
        .setPadding(
            indexedFastScrollerFactory.rootPaddingStart, indexedFastScrollerFactory.rootPaddingTop,
            indexedFastScrollerFactory.rootPaddingEnd, indexedFastScrollerFactory.rootPaddingBottom
        )

    //Popup Text
    val popupIndexLayoutParams =
        leftFastScrollerIndexViewPhoneBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.marginEnd = finalPopupHorizontalOffset

    leftFastScrollerIndexViewPhoneBinding.popupIndex.layoutParams = popupIndexLayoutParams

    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactory.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_left_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactory.popupBackgroundTint)

    leftFastScrollerIndexViewPhoneBinding.popupIndex.background = popupIndexBackground
    leftFastScrollerIndexViewPhoneBinding.popupIndex.typeface = indexedFastScrollerFactory.popupTextFont

    leftFastScrollerIndexViewPhoneBinding.popupIndex.setTextColor(indexedFastScrollerFactory.popupTextColor)
    leftFastScrollerIndexViewPhoneBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactory.popupTextSize
    )

    return this@setupLeftIndex
}