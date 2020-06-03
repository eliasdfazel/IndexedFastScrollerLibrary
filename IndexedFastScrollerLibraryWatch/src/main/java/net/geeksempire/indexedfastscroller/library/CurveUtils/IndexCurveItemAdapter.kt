/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 6/3/20 1:43 AM
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
import net.geeksempire.indexedfastscroller.library.Factory.IndexedFastScrollerFactory
import net.geeksempire.indexedfastscroller.library.R
import java.util.*

class IndexCurveItemAdapter(private val context: Context,
                            private val indexedFastScrollerFactoryWatch: IndexedFastScrollerFactory,
                            private val itemsIndex: ArrayList<String>) : RecyclerView.Adapter<IndexCurveItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.fast_scroller_side_index_item, parent, false))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(viewHolderBinder: ViewHolder, position: Int) {

        viewHolderBinder.itemIndexView.text = itemsIndex[position].toUpperCase(Locale.getDefault())

        viewHolderBinder.itemIndexView.typeface = indexedFastScrollerFactoryWatch.indexItemFont
        viewHolderBinder.itemIndexView.setTextColor(indexedFastScrollerFactoryWatch.indexItemTextColor)
        viewHolderBinder.itemIndexView.setTextSize(TypedValue.COMPLEX_UNIT_SP, indexedFastScrollerFactoryWatch.indexItemSize)

    }

    override fun getItemCount(): Int {

        return itemsIndex.size
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var itemIndexView: TextView = view.findViewById<View>(R.id.itemIndexView) as TextView
    }
}