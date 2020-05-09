/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/8/20 5:11 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.indexedfastscroller.indexedpopupfastscrollersample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class SampleRecyclerViewAdapter (private val context: Context,
                                 private var adapterItemData: ArrayList<String>) : RecyclerView.Adapter<SampleRecyclerViewAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleRecyclerViewAdapter.ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_item_sample_views, parent, false))
    }

    override fun onBindViewHolder(viewHolderBinder: SampleRecyclerViewAdapter.ViewHolder, position: Int) {

        viewHolderBinder.textItemView.text = adapterItemData[position]

        viewHolderBinder.fullItemView.setOnClickListener {

            Toast.makeText(context,
                adapterItemData[position],
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {

        return adapterItemData.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var fullItemView: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.fullItemView) as ConstraintLayout
        var textItemView: TextView = view.findViewById<TextView>(R.id.textItemView) as TextView
    }
}