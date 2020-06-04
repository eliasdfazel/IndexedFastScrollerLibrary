/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/4/20 3:32 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Sides.Right.Extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactoryPhone
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Right.RightSideIndexedFastScrollerPhone
import net.geeksempire.indexedfastscroller.library.databinding.RightFastScrollerIndexViewPhoneBinding

fun RightSideIndexedFastScrollerPhone.setupRightIndex(
    context: Context,
    rootView: ViewGroup,
    rightFastScrollerIndexViewPhoneBinding: RightFastScrollerIndexViewPhoneBinding,
    indexedFastScrollerFactoryPhone: IndexedFastScrollerFactoryPhone,
    finalPopupHorizontalOffset: Int) : RightSideIndexedFastScrollerPhone {

    //Root View
    rootView.addView(rightFastScrollerIndexViewPhoneBinding.root)

    when (rootView) {
        is ConstraintLayout -> {

            val rootLayoutParams =
                rightFastScrollerIndexViewPhoneBinding.root.layoutParams as ConstraintLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.topToTop = rootView.id
            rootLayoutParams.bottomToBottom = rootView.id
            rootLayoutParams.endToEnd = rootView.id

            rightFastScrollerIndexViewPhoneBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            val rootLayoutParams =
                rightFastScrollerIndexViewPhoneBinding.root.layoutParams as RelativeLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, rootView.id)

            rightFastScrollerIndexViewPhoneBinding.root.layoutParams = rootLayoutParams

        }
        else -> {

            val unsupportedOperationException = UnsupportedOperationException()
            unsupportedOperationException.stackTrace = arrayOf(
                StackTraceElement(
                    "${this@setupRightIndex.javaClass.simpleName}",
                    "initializeIndexView()",
                    "${this@setupRightIndex.javaClass.simpleName}",
                    77
                )
            )
            unsupportedOperationException.addSuppressed(Throwable(context.getString(R.string.supportedRootError)))

            throw unsupportedOperationException
        }
    }

    rightFastScrollerIndexViewPhoneBinding.root
        .setPadding(
            indexedFastScrollerFactoryPhone.rootPaddingStart, indexedFastScrollerFactoryPhone.rootPaddingTop,
            indexedFastScrollerFactoryPhone.rootPaddingEnd, indexedFastScrollerFactoryPhone.rootPaddingBottom
        )

    //Popup Text
    val popupIndexLayoutParams =
        rightFastScrollerIndexViewPhoneBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.marginEnd = finalPopupHorizontalOffset

    rightFastScrollerIndexViewPhoneBinding.popupIndex.layoutParams = popupIndexLayoutParams

    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactoryPhone.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_right_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactoryPhone.popupBackgroundTint)

    rightFastScrollerIndexViewPhoneBinding.popupIndex.background = popupIndexBackground
    rightFastScrollerIndexViewPhoneBinding.popupIndex.typeface = indexedFastScrollerFactoryPhone.popupTextFont

    rightFastScrollerIndexViewPhoneBinding.popupIndex.setTextColor(indexedFastScrollerFactoryPhone.popupTextColor)
    rightFastScrollerIndexViewPhoneBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactoryPhone.popupTextSize
    )

    return this@setupRightIndex
}