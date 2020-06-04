/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/4/20 6:40 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.geeksempire.indexedfastscroller.library.CurveUtils

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.geeksempire.indexedfastscroller.library.Factory.IndexSide
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactoryWatch
import net.geeksempire.indexedfastscroller.library.R
import java.util.*

class IndexCurveItemAdapter(private val context: Context,
                            private val indexedFastScrollerFactoryWatchWatch: IndexedFastScrollerFactoryWatch,
                            private val curveData: ArrayList<CurveData>) : RecyclerView.Adapter<IndexCurveItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        when (viewType) {
            IndexSide.RIGHT -> {

                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.right_curve_fast_scroller_side_index_item_watch, viewGroup, false))

            }
            IndexSide.LEFT -> {

                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.left_curve_fast_scroller_side_index_item_watch, viewGroup, false))

            }
            IndexSide.BOTTOM -> {

                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.bottom_curve_fast_scroller_side_index_item_watch, viewGroup, false))

            }
            else -> {

                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.right_curve_fast_scroller_side_index_item_watch, viewGroup, false))

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)

        return curveData[position].indexSide
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(viewHolderBinder: ViewHolder, position: Int) {

        viewHolderBinder.itemIndexView.text = curveData[position].indexText.toUpperCase(Locale.getDefault())

        viewHolderBinder.itemIndexView.typeface = indexedFastScrollerFactoryWatchWatch.indexItemFont
        viewHolderBinder.itemIndexView.setTextColor(indexedFastScrollerFactoryWatchWatch.indexItemTextColor)
        viewHolderBinder.itemIndexView.setTextSize(TypedValue.COMPLEX_UNIT_SP, indexedFastScrollerFactoryWatchWatch.indexItemSize)

    }

    override fun getItemCount(): Int {

        return curveData.size
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var itemIndexView: TextView = view.findViewById<View>(R.id.itemIndexView) as TextView
    }
}