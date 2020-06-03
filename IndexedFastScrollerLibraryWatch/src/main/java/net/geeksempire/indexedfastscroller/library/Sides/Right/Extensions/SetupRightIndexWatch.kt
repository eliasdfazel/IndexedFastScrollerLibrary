/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/3/20 7:28 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Sides.Right.Extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import kotlinx.coroutines.*
import net.geeksempire.indexedfastscroller.library.CurveUtils.IndexCurveItemAdapter
import net.geeksempire.indexedfastscroller.library.CurveUtils.IndexCurveWearLayoutManager
import net.geeksempire.indexedfastscroller.library.Factory.indexedFastScrollerFactoryWatch
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Right.RightSideIndexedFastScrollerWatch
import net.geeksempire.indexedfastscroller.library.databinding.RightFastScrollerIndexViewBinding

private fun setupCurveRightIndex(
    context: Context,
    rootView: ViewGroup,
    layoutInflater: LayoutInflater,
    indexedFastScrollerFactoryWatch: indexedFastScrollerFactoryWatch) = CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {

    val fastScrollerCurvedIndexView = layoutInflater.inflate(R.layout.right_curve_fast_scroller_index_view, null) as RelativeLayout
    val nestedIndexScrollViewCurve = fastScrollerCurvedIndexView.findViewById<WearableRecyclerView>(R.id.nestedIndexScrollViewCurve)
    rootView.addView(fastScrollerCurvedIndexView, 0)

    delay(1000)

    val wearableLinearLayoutManager = WearableLinearLayoutManager(context, IndexCurveWearLayoutManager())
    nestedIndexScrollViewCurve.layoutManager = wearableLinearLayoutManager

    nestedIndexScrollViewCurve.setOnTouchListener { view, motionEvent -> true }

    nestedIndexScrollViewCurve.isEdgeItemsCenteringEnabled = true
    nestedIndexScrollViewCurve.apply {
        isCircularScrollingGestureEnabled = true
        bezelFraction = 0.10f
        scrollDegreesPerScreen = 90f
    }

    val listOfNewCharOfItemsForIndex = indexedFastScrollerFactoryWatch.listOfNewCharOfItemsForIndex

    val stringHashSet: Set<String> = LinkedHashSet(listOfNewCharOfItemsForIndex)
    listOfNewCharOfItemsForIndex.clear()
    listOfNewCharOfItemsForIndex.addAll(stringHashSet)

    val indexCurveItemAdapter: IndexCurveItemAdapter = IndexCurveItemAdapter(context,
        indexedFastScrollerFactoryWatch,
        listOfNewCharOfItemsForIndex)
    nestedIndexScrollViewCurve.adapter = indexCurveItemAdapter

    delay(500)

    nestedIndexScrollViewCurve.smoothScrollToPosition(listOfNewCharOfItemsForIndex.size/2)
    nestedIndexScrollViewCurve.visibility = View.VISIBLE
}

fun RightSideIndexedFastScrollerWatch.setupRightIndex(
    context: Context,
    rootView: ViewGroup,
    layoutInflater: LayoutInflater,
    rightFastScrollerIndexViewBinding: RightFastScrollerIndexViewBinding,
    indexedFastScrollerFactoryWatch: indexedFastScrollerFactoryWatch) : RightSideIndexedFastScrollerWatch {

    setupCurveRightIndex(
        context,
        rootView,
        layoutInflater,
        indexedFastScrollerFactoryWatch
    )

    //Root View
    rootView.addView(rightFastScrollerIndexViewBinding.root)

    when (rootView) {
        is ConstraintLayout -> {

            val rootLayoutParams =
                rightFastScrollerIndexViewBinding.root.layoutParams as ConstraintLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.topToTop = rootView.id
            rootLayoutParams.bottomToBottom = rootView.id
            rootLayoutParams.endToEnd = rootView.id

            rightFastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            val rootLayoutParams =
                rightFastScrollerIndexViewBinding.root.layoutParams as RelativeLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT

            rootLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, rootView.id)

            rightFastScrollerIndexViewBinding.root.layoutParams = rootLayoutParams

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

    rightFastScrollerIndexViewBinding.root
        .setPadding(
            indexedFastScrollerFactoryWatch.rootPaddingStart, indexedFastScrollerFactoryWatch.rootPaddingTop,
            indexedFastScrollerFactoryWatch.rootPaddingEnd, indexedFastScrollerFactoryWatch.rootPaddingBottom
        )

    //Popup Text
    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactoryWatch.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_right_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactoryWatch.popupBackgroundTint)

    rightFastScrollerIndexViewBinding.popupIndex.background = popupIndexBackground
    rightFastScrollerIndexViewBinding.popupIndex.typeface = indexedFastScrollerFactoryWatch.popupTextFont

    rightFastScrollerIndexViewBinding.popupIndex.setTextColor(indexedFastScrollerFactoryWatch.popupTextColor)
    rightFastScrollerIndexViewBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactoryWatch.popupTextSize
    )

    return this@setupRightIndex
}