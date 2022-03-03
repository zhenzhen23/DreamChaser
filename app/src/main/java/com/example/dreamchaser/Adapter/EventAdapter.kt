package com.example.dreamchaser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamchaser.Database.EventDatabaseHandler
import com.example.dreamchaser.Model.EventModelClass
import com.example.dreamchaser.R
import kotlinx.android.synthetic.main.event_row.view.*

class EventAdapter(val context: Context, val items: ArrayList<EventModelClass>) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private val eventDatabase: EventDatabaseHandler = EventDatabaseHandler(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llEventItem = view.llEventItem
        val tvEventTitle = view.tvEventTitle
        val ivEventDelete = view.ivEventDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.event_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
        val item = items.get(position)

        holder.tvEventTitle.text = item.goal

        holder.ivEventDelete.setOnClickListener {
            eventDatabase.deleteEvent(item)
            Toast.makeText(context, "Event Deleted.", Toast.LENGTH_SHORT).show()
            items.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}