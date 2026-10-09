package com.licon.customanalogclocks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** Binds each [WatchFace] to a [CustomAnalogClock] with its name underneath. */
class ClockAdapter(private val watchFaces: List<WatchFace>) :
    RecyclerView.Adapter<ClockAdapter.ClockViewHolder>() {

    class ClockViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val clock: CustomAnalogClock = itemView.findViewById(R.id.clock)
        private val name: TextView = itemView.findViewById(R.id.clock_name)

        /** Shows [watchFace] on the clock and its name in the label. */
        fun bind(watchFace: WatchFace) {
            clock.watchFace = watchFace
            name.text = watchFace.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_clock, parent, false)
        return ClockViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClockViewHolder, position: Int) = holder.bind(watchFaces[position])

    override fun getItemCount(): Int = watchFaces.size
}
