/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/9/20 12:31 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.library

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import net.geeksempire.indexedfastscroller.library.Extensions.setupBottomIndex
import net.geeksempire.indexedfastscroller.library.Extensions.setupLeftIndex
import net.geeksempire.indexedfastscroller.library.Extensions.setupRightIndex
import net.geeksempire.indexedfastscroller.library.Factory.*
import net.geeksempire.indexedfastscroller.library.databinding.FastScrollerIndexViewBinding
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
 * @param indexedFastScrollerFactory Change Default Value Or Just Pass IndexedFastScrollerFactory()
 **/
class IndexedFastScroller(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val rootView: ViewGroup,
    private val nestedScrollView: ScrollView,
    private val recyclerView: RecyclerView,
    private val indexedFastScrollerFactory: IndexedFastScrollerFactory
) {

    private val fastScrollerIndexViewBinding: FastScrollerIndexViewBinding = FastScrollerIndexViewBinding.inflate(layoutInflater)

    private val statusBarHeight = calculateStatusBarHeight(context.resources)
    private val navigationBarBarHeight = calculateNavigationBarHeight(context.resources)

    private val finalPopupVerticalOffset: Int =
        indexedFastScrollerFactory.popupVerticalOffset.convertToDp(context)

    private val finalPopupHorizontalOffset: Int =
        indexedFastScrollerFactory.popupHorizontalOffset.convertToDp(context)

    init {
        Log.d(this@IndexedFastScroller.javaClass.simpleName, "*** Indexed Fast Scroller Initialized ***")
    }

    fun initializeIndexView(): Deferred<IndexedFastScroller> = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

        fastScrollerIndexViewBinding.indexView.removeAllViews()

        when (indexedFastScrollerFactory.indexSide) {
            IndexSide.RIGHT -> {
                Log.d(this@IndexedFastScroller.javaClass.simpleName, "*** Right Side Index ***")

                setupRightIndex(
                    context,
                    rootView,
                    fastScrollerIndexViewBinding,
                    indexedFastScrollerFactory,
                    finalPopupHorizontalOffset
                )

            }
            IndexSide.LEFT -> {
                Log.d(this@IndexedFastScroller.javaClass.simpleName, "*** Left Side Index ***")

                setupLeftIndex(
                    context,
                    rootView,
                    fastScrollerIndexViewBinding,
                    indexedFastScrollerFactory,
                    finalPopupHorizontalOffset
                )
            }
            IndexSide.BOTTOM -> {
                Log.d(this@IndexedFastScroller.javaClass.simpleName, "*** Bottom Side Index ***")

                setupBottomIndex(
                    context,
                    rootView,
                    fastScrollerIndexViewBinding,
                    indexedFastScrollerFactory,
                    finalPopupHorizontalOffset
                )
            }
            else -> {

                setupRightIndex(
                    context,
                    rootView,
                    fastScrollerIndexViewBinding,
                    indexedFastScrollerFactory,
                    finalPopupHorizontalOffset
                )
            }
        }

        this@IndexedFastScroller
    }

    /**
     * When Populating Your List Get First Char Of Each Item Title By itemTextTitle.substring(0, 1).toUpperCase(Locale.getDefault()).
     * & Add It To A ArrayList<String>.
     * Then Pass It As...
     *
     * @param listOfNewCharOfItemsForIndex ArrayList<String>
     **/
    fun loadIndexData(listOfNewCharOfItemsForIndex: ArrayList<String>) = CoroutineScope(SupervisorJob() + Dispatchers.Main).async {

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

        var sideIndexItem = layoutInflater.inflate(R.layout.fast_scroller_side_index_item, null) as TextView

        mapIndexFirstItem.keys.forEach { indexText ->
            sideIndexItem = layoutInflater.inflate(R.layout.fast_scroller_side_index_item, null) as TextView
            sideIndexItem.text = indexText.toUpperCase(Locale.getDefault())

            sideIndexItem.typeface = indexedFastScrollerFactory.indexItemFont
            sideIndexItem.setTextColor(indexedFastScrollerFactory.indexItemTextColor)
            sideIndexItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, indexedFastScrollerFactory.indexItemSize)

            fastScrollerIndexViewBinding.indexView.addView(sideIndexItem)
        }

        val finalTextView = sideIndexItem

        /* *** */
        delay(777)
        /* *** */

        var upperRange = (fastScrollerIndexViewBinding.indexView.y - finalTextView.height).toInt()

        for (number in 0 until fastScrollerIndexViewBinding.indexView.childCount) {
            val indexText = (fastScrollerIndexViewBinding.indexView.getChildAt(number) as TextView).text.toString()
            val indexRange = (fastScrollerIndexViewBinding.indexView.getChildAt(number).y + fastScrollerIndexViewBinding.indexView.y + finalTextView.height).toInt()

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

        fastScrollerIndexViewBinding.nestedIndexScrollView.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                android.R.anim.fade_in
            )
        )
        fastScrollerIndexViewBinding.nestedIndexScrollView.visibility = View.VISIBLE

        val popupIndexOffsetY = (
                statusBarHeight
                        + navigationBarBarHeight
                        + finalPopupVerticalOffset).toFloat()

        fastScrollerIndexViewBinding.nestedIndexScrollView.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (indexedFastScrollerFactory.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.y.toInt()]

                        if (indexText != null) {
                            fastScrollerIndexViewBinding.popupIndex.y = motionEvent.rawY - popupIndexOffsetY
                            fastScrollerIndexViewBinding.popupIndex.text = indexText
                            fastScrollerIndexViewBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_in
                                )
                            )
                            fastScrollerIndexViewBinding.popupIndex.visibility = View.VISIBLE
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (indexedFastScrollerFactory.popupEnable) {
                        val indexText = mapRangeIndex[motionEvent.y.toInt()]

                        if (indexText != null) {
                            if (!fastScrollerIndexViewBinding.popupIndex.isShown) {
                                fastScrollerIndexViewBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                                )
                                fastScrollerIndexViewBinding.popupIndex.visibility = View.VISIBLE
                            }

                            fastScrollerIndexViewBinding.popupIndex.y =
                                motionEvent.rawY - popupIndexOffsetY
                            fastScrollerIndexViewBinding.popupIndex.text = indexText

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem[mapRangeIndex[motionEvent.y.toInt()]] ?: 0
                                ).y.toInt()
                            )

                        } else {
                            if (fastScrollerIndexViewBinding.popupIndex.isShown) {
                                fastScrollerIndexViewBinding.popupIndex.startAnimation(
                                    AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
                                )
                                fastScrollerIndexViewBinding.popupIndex.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (indexedFastScrollerFactory.popupEnable) {
                        if (fastScrollerIndexViewBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            fastScrollerIndexViewBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            fastScrollerIndexViewBinding.popupIndex.visibility = View.INVISIBLE
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
                    if (indexedFastScrollerFactory.popupEnable) {
                        if (fastScrollerIndexViewBinding.popupIndex.isShown) {

                            nestedScrollView.smoothScrollTo(
                                0,
                                recyclerView.getChildAt(
                                    mapIndexFirstItem.get(mapRangeIndex[motionEvent.y.toInt()]) ?: 0
                                ).y.toInt()
                            )

                            fastScrollerIndexViewBinding.popupIndex.startAnimation(
                                AnimationUtils.loadAnimation(
                                    context,
                                    android.R.anim.fade_out
                                )
                            )
                            fastScrollerIndexViewBinding.popupIndex.visibility = View.INVISIBLE
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