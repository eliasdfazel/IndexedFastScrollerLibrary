/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/4/20 3:32 AM
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
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactoryPhone
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Bottom.BottomSideIndexedFastScrollerPhone
import net.geeksempire.indexedfastscroller.library.databinding.BottomFastScrollerIndexViewPhoneBinding

fun BottomSideIndexedFastScrollerPhone.setupBottomIndex(
    context: Context,
    rootView: ViewGroup,
    bottomFastScrollerIndexViewPhoneBinding: BottomFastScrollerIndexViewPhoneBinding,
    indexedFastScrollerFactoryPhone: IndexedFastScrollerFactoryPhone,
    finalPopupVerticalOffset: Int) : BottomSideIndexedFastScrollerPhone {

    //Root View
    rootView.addView(bottomFastScrollerIndexViewPhoneBinding.root)

    when (rootView) {
        is ConstraintLayout -> {

            val rootLayoutParams =
                bottomFastScrollerIndexViewPhoneBinding.root.layoutParams as ConstraintLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            rootLayoutParams.bottomToBottom = rootView.id
            rootLayoutParams.startToStart = rootView.id
            rootLayoutParams.endToEnd = rootView.id

            bottomFastScrollerIndexViewPhoneBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            val rootLayoutParams =
                bottomFastScrollerIndexViewPhoneBinding.root.layoutParams as RelativeLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            rootLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, rootView.id)

            bottomFastScrollerIndexViewPhoneBinding.root.layoutParams = rootLayoutParams

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

    bottomFastScrollerIndexViewPhoneBinding.root
        .setPadding(
            indexedFastScrollerFactoryPhone.rootPaddingStart, indexedFastScrollerFactoryPhone.rootPaddingTop,
            indexedFastScrollerFactoryPhone.rootPaddingEnd, indexedFastScrollerFactoryPhone.rootPaddingBottom
        )

    //Popup Text
    val popupIndexLayoutParams =
        bottomFastScrollerIndexViewPhoneBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.bottomMargin = finalPopupVerticalOffset

    bottomFastScrollerIndexViewPhoneBinding.popupIndex.layoutParams = popupIndexLayoutParams

    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactoryPhone.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_bottom_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactoryPhone.popupBackgroundTint)

    bottomFastScrollerIndexViewPhoneBinding.popupIndex.background = popupIndexBackground
    bottomFastScrollerIndexViewPhoneBinding.popupIndex.typeface = indexedFastScrollerFactoryPhone.popupTextFont

    bottomFastScrollerIndexViewPhoneBinding.popupIndex.setTextColor(indexedFastScrollerFactoryPhone.popupTextColor)
    bottomFastScrollerIndexViewPhoneBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactoryPhone.popupTextSize
    )

    return this@setupBottomIndex
}