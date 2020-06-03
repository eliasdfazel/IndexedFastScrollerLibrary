/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/3/20 2:56 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Sides.Right

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import net.geeksempire.indexedfastscroller.library.Factory.calculateNavigationBarHeight
import net.geeksempire.indexedfastscroller.library.Factory.calculateStatusBarHeight
import net.geeksempire.indexedfastscroller.library.Factory.convertToDp
import net.geeksempire.indexedfastscroller.library.Factory.indexedFastScrollerFactoryWatch
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Right.Extensions.setupRightIndex
import net.geeksempire.indexedfastscroller.library.databinding.RightFastScrollerIndexViewBinding
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Enabling ViewBinding Is Highly Recommended.
 * Just Add The Below Configuration To Your App Level Gradle In Android Section
 * viewBinding { enabled = true }
 *
 *
 * @param rootView Instance Of Root (Base) View In Your Layout
 * @param nestedScrollView Follow This Hierarchy: ScrollView -> RelativeLayout -> RecyclerView
 * @param recyclerView Instance Of A RecyclerView That You Want To Populate With Items
 *
 *
 * @param indexedFastScrollerFactoryWatchWatch Change Default Value Or Just Pass IndexedFastScrollerFactory()
 **/
class RightSideIndexedFastScrollerWatch(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val rootView: ViewGroup,
    private val nestedScrollView: ScrollView,
    private val recyclerView: RecyclerView,
    private val indexedFastScrollerFactoryWatchWatch: indexedFastScrollerFactoryWatch) {

    private val rightFastScrollerIndexViewBinding: RightFastScrollerIndexViewBinding = RightFastScrollerIndexViewBinding.inflate(layoutInflater)

    private val statusBarHeight = calculateStatusBarHeight(context.resources)
    private val navigationBarBarHeight = calculateNavigationBarHeight(context.resources)

    private val finalPopupVerticalOffset: Int =
        indexedFastScrollerFactoryWatchWatch.popupVerticalOffset.convertToDp(context)

    private val finalPopupHorizontalOffset: Int =
        indexedFastScrollerFactoryWatchWatch.popupHorizontalOffset.convertToDp(context)

    init {
        Log.d(this@RightSideIndexedFastScrollerWatch.javaClass.simpleName, "*** Indexed Fast Scroller Initialized ***")
    }

    fun initializeIndexView(): Deferred<RightSideIndexedFastScrollerWatch> = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        rightFastScrollerIndexViewBinding.indexView.removeAllViews()

        setupRightIndex(
            context,
            rootView,
            layoutInflater,
            rightFastScrollerIndexViewBinding,
            indexedFastScrollerFactoryWatchWatch,
            finalPopupHorizontalOffset
        ).loadIndexData(indexedFastScrollerFactoryWatchWatch.listOfNewCharOfItemsForIndex).await()

        this@RightSideIndexedFastScrollerWatch
    }

    /**
     * When Populating Your List Get First Char Of Each Item Title By itemTextTitle.substring(0, 1).toUpperCase(Locale.getDefault()).
     * & Add It To A ArrayList<String>.
     * Then Pass It As...
     *
     * @param listOfNewCharOfItemsForIndex ArrayList<String>
     **/
    private fun loadIndexData(listOfNewCharOfItemsForIndex: ArrayList<String>) = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        val mapIndexFirstItem: LinkedHashMap<String, Int> = LinkedHashMap<String, Int>()
        val mapIndexLastItem: LinkedHashMap<String, Int> = LinkedHashMap<String, Int>()

        val mapRangeIndex: LinkedHashMap<Int, String> = LinkedHashMap<Int, String>()

        withContext(Dispatchers.Default) {

            listOfNewCharOfItemsForIndex.forEachIndexed { indexNumber, indexText ->

                val finalIndexText = indexText.toUpperCase(Locale.getDefault())

                /*Avoid Duplication*/
                if (mapIndexFirstItem[finalIndexText] == null) {
                    mapIndexFirstItem[finalIndexText] = indexNumber
                }

                mapIndexLastItem[finalIndexText] = indexNumber

            }

        }

        var sideIndexItem = layoutInflater.inflate(R.layout.right_fast_scroller_side_index_item, null) as TextView

        mapIndexFirstItem.keys.forEach { indexText ->
            sideIndexItem = layoutInflater.inflate(R.layout.right_fast_scroller_side_index_item, null) as TextView
            sideIndexItem.text = indexText.toUpperCase(Locale.getDefault())
            sideIndexItem.setTextColor(Color.TRANSPARENT)

            rightFastScrollerIndexViewBinding.indexView.addView(sideIndexItem)
        }

        val finalTextView = sideIndexItem

        /* *** */
        delay(777)
        /* *** */

        var upperRange = (rightFastScrollerIndexViewBinding.indexView.y - finalTextView.height).toInt()

        for (number in 0 until rightFastScrollerIndexViewBinding.indexView.childCount) {
            val indexText = (rightFastScrollerIndexViewBinding.indexView.getChildAt(number) as TextView).text.toString()
            val indexRange = (rightFastScrollerIndexViewBinding.indexView.getChildAt(number).y + rightFastScrollerIndexViewBinding.indexView.y + finalTextView.height).toInt()

            for (jRange in upperRange..indexRange) {
                mapRangeIndex[jRange] = indexText
            }

            upperRange = indexRange
        }

        this@async.launch {

            setupFastScrollingIndexing(
                mapIndexFirstItem,
                    mapRangeIndex
            )
        }.join()
    }

    /**
     * Setup Popup View Of Index With Touch On List Of Index
     **/
    @SuppressLint("ClickableViewAccessibility")
    private fun setupFastScrollingIndexing(
        mapIndexFirstItem: LinkedHashMap<String, Int>,
        mapRangeIndex: LinkedHashMap<Int, String>) {

        rightFastScrollerIndexViewBinding.nestedIndexScrollView.visibility = View.VISIBLE

        val popupIndexOffsetY = (
                statusBarHeight
                        + navigationBarBarHeight
                        + finalPopupVerticalOffset).toFloat()

        rightFastScrollerIndexViewBinding.nestedIndexScrollView.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (indexedFastScrollerFactoryWatchWatch.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.y.toInt()]

                        if (indexText != null) {
                            rightFastScrollerIndexViewBinding.popupIndex.y = motionEvent.rawY - popupIndexOffsetY
                            rightFastScrollerIndexViewBinding.popupIndex.text = indexText
                            rightFastScrollerIndexViewBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_in
                                )
                            )
                            rightFastScrollerIndexViewBinding.popupIndex.visibility = View.VISIBLE
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (indexedFastScrollerFactoryWatchWatch.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.y.toInt()]

                        if (indexText != null) {
                            if (!rightFastScrollerIndexViewBinding.popupIndex.isShown) {
                                rightFastScrollerIndexViewBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                                )
                                rightFastScrollerIndexViewBinding.popupIndex.visibility = View.VISIBLE
                            }

                            rightFastScrollerIndexViewBinding.popupIndex.y =
                                motionEvent.rawY - popupIndexOffsetY
                            rightFastScrollerIndexViewBinding.popupIndex.text = indexText

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem[mapRangeIndex[motionEvent.y.toInt()]] ?: 0
                                ).y.toInt()
                            )

                        } else {
                            if (rightFastScrollerIndexViewBinding.popupIndex.isShown) {
                                rightFastScrollerIndexViewBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
                                )
                                rightFastScrollerIndexViewBinding.popupIndex.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (indexedFastScrollerFactoryWatchWatch.popupEnable) {
                        if (rightFastScrollerIndexViewBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            rightFastScrollerIndexViewBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            rightFastScrollerIndexViewBinding.popupIndex.visibility = View.INVISIBLE
                        }
                    } else {

                        nestedScrollView.smoothScrollTo(
                            0,
                            recyclerView.getChildAt(
                                mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                            ).y.toInt()
                        )
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (indexedFastScrollerFactoryWatchWatch.popupEnable) {
                        if (rightFastScrollerIndexViewBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            rightFastScrollerIndexViewBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            rightFastScrollerIndexViewBinding.popupIndex.visibility = View.INVISIBLE
                        }
                    } else {

                        nestedScrollView.smoothScrollTo(
                            0,
                            recyclerView.getChildAt(
                                mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                            ).y.toInt()
                        )
                    }
                }
            }

            true
        }
    }
}