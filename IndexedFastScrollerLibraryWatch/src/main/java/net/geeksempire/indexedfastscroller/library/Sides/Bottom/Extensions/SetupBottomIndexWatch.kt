/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/10/20 6:47 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Sides.Bottom.Extensions

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
import net.geeksempire.indexedfastscroller.library.CurveUtils.CurveData
import net.geeksempire.indexedfastscroller.library.CurveUtils.IndexCurveItemAdapter
import net.geeksempire.indexedfastscroller.library.CurveUtils.IndexCurveWearLayoutManager
import net.geeksempire.indexedfastscroller.library.Factory.IndexSide
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactoryWatch
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Bottom.BottomSideIndexedFastScrollerWatch
import net.geeksempire.indexedfastscroller.library.databinding.BottomFastScrollerIndexViewWatchBinding

private fun setupCurveBottomIndex(
    context: Context,
    rootView: ViewGroup,
    layoutInflater: LayoutInflater,
    IndexedFastScrollerFactoryWatch: IndexedFastScrollerFactoryWatch) = CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {

    val fastScrollerCurvedIndexView = layoutInflater.inflate(R.layout.bottom_curve_fast_scroller_index_view_watch, null) as RelativeLayout
    val nestedIndexScrollViewCurve = fastScrollerCurvedIndexView.findViewById<WearableRecyclerView>(R.id.nestedIndexScrollViewCurve)
    rootView.addView(fastScrollerCurvedIndexView, 0)

    delay(1000)

    val wearableLinearLayoutManager = if (context.resources.configuration.isScreenRound){
        WearableLinearLayoutManager(context, IndexCurveWearLayoutManager())
    } else {
        WearableLinearLayoutManager(context)
    }
    nestedIndexScrollViewCurve.layoutManager = wearableLinearLayoutManager

    nestedIndexScrollViewCurve.setOnTouchListener { view, motionEvent -> true }

    nestedIndexScrollViewCurve.isEdgeItemsCenteringEnabled = true
    nestedIndexScrollViewCurve.apply {
        isCircularScrollingGestureEnabled = true
        bezelFraction = 0.10f
        scrollDegreesPerScreen = 90f
    }

    val stringHashSet: Set<String> = LinkedHashSet(IndexedFastScrollerFactoryWatch.listOfNewCharOfItemsForIndex)

    val listOfCharOfItemsForIndex = ArrayList<CurveData>()

    stringHashSet.forEach {

        listOfCharOfItemsForIndex.add(
            CurveData(
                it,
                IndexSide.BOTTOM
            )
        )
    }

    val indexCurveItemAdapter: IndexCurveItemAdapter = IndexCurveItemAdapter(context,
        IndexedFastScrollerFactoryWatch,
        listOfCharOfItemsForIndex
    )
    nestedIndexScrollViewCurve.adapter = indexCurveItemAdapter

    delay(500)

    nestedIndexScrollViewCurve.smoothScrollToPosition(listOfCharOfItemsForIndex.size/2)
    nestedIndexScrollViewCurve.visibility = View.VISIBLE
}

fun BottomSideIndexedFastScrollerWatch.setupBottomIndex(
    context: Context,
    rootView: ViewGroup,
    layoutInflater: LayoutInflater,
    bottomFastScrollerIndexViewWatchBinding: BottomFastScrollerIndexViewWatchBinding,
    indexedFastScrollerFactoryWatch: IndexedFastScrollerFactoryWatch) : BottomSideIndexedFastScrollerWatch {

    setupCurveBottomIndex(
        context,
        rootView,
        layoutInflater,
        indexedFastScrollerFactoryWatch
    )

    //Root View
    rootView.addView(bottomFastScrollerIndexViewWatchBinding.root)

    when (rootView) {
        is ConstraintLayout -> {

            val rootLayoutParams =
                bottomFastScrollerIndexViewWatchBinding.root.layoutParams as ConstraintLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            rootLayoutParams.bottomToBottom = rootView.id
            rootLayoutParams.startToStart = rootView.id
            rootLayoutParams.endToEnd = rootView.id

            bottomFastScrollerIndexViewWatchBinding.root.layoutParams = rootLayoutParams

        }
        is RelativeLayout -> {

            val rootLayoutParams =
                bottomFastScrollerIndexViewWatchBinding.root.layoutParams as RelativeLayout.LayoutParams

            rootLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            rootLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            rootLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            rootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, rootView.id)

            bottomFastScrollerIndexViewWatchBinding.root.layoutParams = rootLayoutParams

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

    bottomFastScrollerIndexViewWatchBinding.root
        .setPadding(
            indexedFastScrollerFactoryWatch.rootPaddingStart, indexedFastScrollerFactoryWatch.rootPaddingTop,
            indexedFastScrollerFactoryWatch.rootPaddingEnd, indexedFastScrollerFactoryWatch.rootPaddingBottom
        )

    //Popup Text
    val popupIndexBackground: Drawable? =
        indexedFastScrollerFactoryWatch.popupBackgroundShape ?: context.getDrawable(
            R.drawable.default_bottom_popup_background
        )?.mutate()
    popupIndexBackground?.setTint(indexedFastScrollerFactoryWatch.popupBackgroundTint)

    bottomFastScrollerIndexViewWatchBinding.popupIndex.background = popupIndexBackground
    bottomFastScrollerIndexViewWatchBinding.popupIndex.typeface = indexedFastScrollerFactoryWatch.popupTextFont

    bottomFastScrollerIndexViewWatchBinding.popupIndex.setTextColor(indexedFastScrollerFactoryWatch.popupTextColor)
    bottomFastScrollerIndexViewWatchBinding.popupIndex.setTextSize(
        TypedValue.COMPLEX_UNIT_SP,
        indexedFastScrollerFactoryWatch.popupTextSize
    )

    return this@setupBottomIndex
}