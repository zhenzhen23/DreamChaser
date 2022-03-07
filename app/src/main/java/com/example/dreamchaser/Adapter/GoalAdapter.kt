package com.example.dreamchaser.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamchaser.Model.EventModelClass
import com.example.dreamchaser.R
import kotlinx.android.synthetic.main.goal_row.view.*
import kotlin.math.roundToInt

/**
 * Adapter for goal recycle view
 */
class GoalAdapter(val context: Context, val items: ArrayList<EventModelClass>) :
    RecyclerView.Adapter<GoalAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGoalTitle = view.tvGoalTitle
        val pbGoalBar = view.pbGoalBar
        val tvGoalValue = view.tvGoalValue
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalAdapter.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.goal_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.tvGoalTitle.text = item.goal
        holder.pbGoalBar.max = item.maxValue
        holder.pbGoalBar.progress = item.currentValue
        /* set percentage of maxValue and currentValue*/
        holder.tvGoalValue.text = (item.currentValue * 1f / item.maxValue * 100).roundToInt().toString() + "%"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}