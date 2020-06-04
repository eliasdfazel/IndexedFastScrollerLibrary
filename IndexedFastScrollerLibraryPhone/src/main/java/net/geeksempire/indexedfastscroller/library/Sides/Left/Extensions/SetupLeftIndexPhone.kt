/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/4/20 3:32 AM
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
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactoryPhone
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Left.LeftSideIndexedFastScrollerPhone
import net.geeksempire.indexedfastscroller.library.databinding.LeftFastScrollerIndexViewPhoneBinding

fun LeftSideIndexedFastScrollerPhone.setupLeftIndex(
    context: Context,
    rootView: ViewGroup,
    leftFastScrollerIndexViewPhoneBinding: LeftFastScrollerIndexViewPhoneBinding,
    indexedFastScrollerFactoryPhone: IndexedFastScrollerFactoryPhone,
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
            indexedFastScrollerFactoryPhone.rootPaddingStart, indexedFastScrollerFactoryPhone.rootPaddingTop,
            indexedFastScrollerFactoryPhone.rootPaddingEnd, indexedFastScrollerFactoryPhone.rootPaddingBottom
        )

    //Popup Text
    val popupIndexLayoutParams =
        leftFastScrollerIndexViewPhoneBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.marginEnd = finalPopupHorizontalOffset

    leftFastScrollerIndexViewPhoneBinding.popupIndex.layoutParams = popupIndexLayoutParams

    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactoryPhone.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_left_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactoryPhone.popupBackgroundTint)

    leftFastScrollerIndexViewPhoneBinding.popupIndex.background = popupIndexBackground
    leftFastScrollerIndexViewPhoneBinding.popupIndex.typeface = indexedFastScrollerFactoryPhone.popupTextFont

    leftFastScrollerIndexViewPhoneBinding.popupIndex.setTextColor(indexedFastScrollerFactoryPhone.popupTextColor)
    leftFastScrollerIndexViewPhoneBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactoryPhone.popupTextSize
    )

    return this@setupLeftIndex
}