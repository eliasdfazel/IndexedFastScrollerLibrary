/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/4/20 6:08 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library.Sides.Bottom

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
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactoryWatch
import net.geeksempire.indexedfastscroller.library.R
import net.geeksempire.indexedfastscroller.library.Sides.Bottom.Extensions.setupBottomIndex
import net.geeksempire.indexedfastscroller.library.databinding.BottomFastScrollerIndexViewWatchBinding
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
 * @param indexedFastScrollerFactoryWatch Change Default Value Or Just Pass IndexedFastScrollerFactory()
 **/
class BottomSideIndexedFastScrollerWatch(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val rootView: ViewGroup,
    private val nestedScrollView: ScrollView,
    private val recyclerView: RecyclerView,
    private val indexedFastScrollerFactoryWatch: IndexedFastScrollerFactoryWatch
) {

    private val bottomFastScrollerIndexViewWatchBinding: BottomFastScrollerIndexViewWatchBinding = BottomFastScrollerIndexViewWatchBinding.inflate(layoutInflater)

    init {
        Log.d(this@BottomSideIndexedFastScrollerWatch.javaClass.simpleName, "*** Indexed Fast Scroller Initialized ***")
    }

    fun initializeIndexView(): Deferred<BottomSideIndexedFastScrollerWatch> = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        bottomFastScrollerIndexViewWatchBinding.indexView.removeAllViews()

        setupBottomIndex(
            context,
            rootView,
            layoutInflater,
            bottomFastScrollerIndexViewWatchBinding,
            indexedFastScrollerFactoryWatch
        ).loadIndexData(indexedFastScrollerFactoryWatch.listOfNewCharOfItemsForIndex).await()

        this@BottomSideIndexedFastScrollerWatch
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

        var sideIndexItem = layoutInflater.inflate(R.layout.bottom_fast_scroller_side_index_item_watch, null) as TextView

        mapIndexFirstItem.keys.forEach { indexText ->
            sideIndexItem = layoutInflater.inflate(R.layout.bottom_fast_scroller_side_index_item_watch, null) as TextView
            sideIndexItem.text = indexText.toUpperCase(Locale.getDefault())
            sideIndexItem.setTextColor(Color.TRANSPARENT)

            bottomFastScrollerIndexViewWatchBinding.indexView.addView(sideIndexItem)
        }

        val finalTextView = sideIndexItem

        /* *** */
        delay(777)
        /* *** */

        var upperRange = (bottomFastScrollerIndexViewWatchBinding.indexView.x - finalTextView.width).toInt()

        for (number in 0 until bottomFastScrollerIndexViewWatchBinding.indexView.childCount) {
            val indexText = (bottomFastScrollerIndexViewWatchBinding.indexView.getChildAt(number) as TextView).text.toString()
            val indexRange = (bottomFastScrollerIndexViewWatchBinding.indexView.getChildAt(number).x + bottomFastScrollerIndexViewWatchBinding.indexView.x + finalTextView.width).toInt()

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

        bottomFastScrollerIndexViewWatchBinding.nestedIndexScrollView.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                android.R.anim.fade_in
            )
        )
        bottomFastScrollerIndexViewWatchBinding.nestedIndexScrollView.visibility = View.VISIBLE

        bottomFastScrollerIndexViewWatchBinding.nestedIndexScrollView.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (indexedFastScrollerFactoryWatch.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.x.toInt()]

                        if (indexText != null) {
                            bottomFastScrollerIndexViewWatchBinding.popupIndex.text = indexText
                            bottomFastScrollerIndexViewWatchBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_in
                                )
                            )
                            bottomFastScrollerIndexViewWatchBinding.popupIndex.visibility = View.VISIBLE
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (indexedFastScrollerFactoryWatch.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.x.toInt()]

                        if (indexText != null) {
                            if (!bottomFastScrollerIndexViewWatchBinding.popupIndex.isShown) {
                                bottomFastScrollerIndexViewWatchBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                                )
                                bottomFastScrollerIndexViewWatchBinding.popupIndex.visibility = View.VISIBLE
                            }

                            bottomFastScrollerIndexViewWatchBinding.popupIndex.text = indexText

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem[mapRangeIndex[motionEvent.x.toInt()]] ?: 0
                                ).y.toInt()
                            )

                        } else {
                            if (bottomFastScrollerIndexViewWatchBinding.popupIndex.isShown) {
                                bottomFastScrollerIndexViewWatchBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
                                )
                                bottomFastScrollerIndexViewWatchBinding.popupIndex.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (indexedFastScrollerFactoryWatch.popupEnable) {
                        if (bottomFastScrollerIndexViewWatchBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.x.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            bottomFastScrollerIndexViewWatchBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            bottomFastScrollerIndexViewWatchBinding.popupIndex.visibility = View.INVISIBLE
                        }
                    } else {

                        nestedScrollView.smoothScrollTo(
                            0,
                            recyclerView.getChildAt(
                                mapIndexFirstItem.get(mapRangeIndex[motionEvent.x.toInt()]) ?: 0
                            ).y.toInt()
                        )
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (indexedFastScrollerFactoryWatch.popupEnable) {
                        if (bottomFastScrollerIndexViewWatchBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.x.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            bottomFastScrollerIndexViewWatchBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            bottomFastScrollerIndexViewWatchBinding.popupIndex.visibility = View.INVISIBLE
                        }
                    } else {

                        nestedScrollView.smoothScrollTo(
                            0,
                            recyclerView.getChildAt(
                                mapIndexFirstItem.get(mapRangeIndex[motionEvent.x.toInt()]) ?: 0
                            ).y.toInt()
                        )
                    }
                }
            }

            true
        }
    }
}