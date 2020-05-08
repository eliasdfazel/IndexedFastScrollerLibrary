/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/8/20 9:59 AM
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
import kotlinx.coroutines.*
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactory
import net.geeksempire.indexedfastscroller.library.IndexedFastScroller
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.databinding.FastScrollerIndexViewBinding

fun IndexedFastScroller.setupRightIndex(context: Context,
                                        fastScrollerIndexViewBinding: FastScrollerIndexViewBinding,
                                        rootView: ViewGroup,
                                        indexedFastScrollerFactory: IndexedFastScrollerFactory,
                                        finalPopupHorizontalOffset: Int) : Deferred<IndexedFastScroller> = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

    when(rootView) {
        is ConstraintLayout -> {

            //Root View
            val rootLayoutParams = fastScrollerIndexViewBinding.root.layoutParams as ConstraintLayout.LayoutParams
            rootLayoutParams.endToEnd = rootView.id

            fastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            //Root View
            val rootLayoutParams = fastScrollerIndexViewBinding.root.layoutParams as RelativeLayout.LayoutParams
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, rootView.id)

            fastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

        }
        else -> {

            val unsupportedOperationException = UnsupportedOperationException()
            unsupportedOperationException.stackTrace = arrayOf(
                StackTraceElement("${this@setupRightIndex.javaClass.simpleName}", "initializeIndexView()", "${this@setupRightIndex.javaClass.simpleName}", 77)
            )
            unsupportedOperationException.addSuppressed(Throwable(context.getString(R.string.supportedRootError)))

            throw unsupportedOperationException
        }
    }

    //Popup Text
    val popupIndexLayoutParams = fastScrollerIndexViewBinding.popupIndex.layoutParams as ConstraintLayout.LayoutParams
    popupIndexLayoutParams.marginEnd = finalPopupHorizontalOffset

    fastScrollerIndexViewBinding.popupIndex.layoutParams = popupIndexLayoutParams

    fastScrollerIndexViewBinding.root
        .setPadding(indexedFastScrollerFactory.rootPaddingStart, indexedFastScrollerFactory.rootPaddingTop,
            indexedFastScrollerFactory.rootPaddingEnd, indexedFastScrollerFactory.rootPaddingBottom)

    val popupIndexBackground: Drawable? = indexedFastScrollerFactory.popupBackgroundShape?:context.getDrawable(
        R.drawable.ic_launcher_balloon)?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactory.popupBackgroundTint)
    fastScrollerIndexViewBinding.popupIndex.background = popupIndexBackground
    fastScrollerIndexViewBinding.popupIndex.setTextColor(indexedFastScrollerFactory.popupTextColor)
    fastScrollerIndexViewBinding.popupIndex.typeface = indexedFastScrollerFactory.popupTextFont
    fastScrollerIndexViewBinding.popupIndex.setTextSize(TypedValue.COMPLEX_UNIT_SP, indexedFastScrollerFactory.popupTextSize)

    this@setupRightIndex
}