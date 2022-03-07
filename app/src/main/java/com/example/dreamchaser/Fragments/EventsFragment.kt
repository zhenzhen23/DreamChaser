package com.example.dreamchaser.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamchaser.Adapter.EventAdapter
import com.example.dreamchaser.Database.EventDatabaseHandler
import com.example.dreamchaser.Model.EventModelClass
import com.example.dreamchaser.R
import kotlinx.android.synthetic.main.fragment_events.*

/**
 * Fragment class for event page
 */
class EventsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        btnAddEvent.setOnClickListener { view ->
            addRecord(view)
        }
    }

    /**
     * Set recycler view for event page
     */
    private fun initRecyclerView() {
        if (getItemList().size > 0) {
            rvEvents.visibility = View.VISIBLE
            rvEvents.layoutManager = LinearLayoutManager(activity)
            rvEvents.adapter = EventAdapter(context!!, getItemList())
        } else {
            rvEvents.visibility = View.GONE
        }
    }

    /**
     * Add records to database
     */
    private fun addRecord(view: View?) {
        val event = etEvent.text.toString()
        val goal = etGoal.text.toString()
        val maxValue = etMaxValue.text.toString()
        val dailyValue = etDailyValue.text.toString()
        val eventDatabaseHandler: EventDatabaseHandler = EventDatabaseHandler(context!!)

        /* Check input box is not empty*/
        if (event.isNotEmpty() && goal.isNotEmpty() && maxValue.isNotEmpty() && dailyValue.isNotEmpty()) {
            val status = eventDatabaseHandler.addEvent(
                EventModelClass(
                    0,
                    event,
                    goal,
                    Integer.parseInt(maxValue),
                    Integer.parseInt(dailyValue),
                    0
                )
            )
            if (status > -1) {
                Toast.makeText(activity, "Added to Event List", Toast.LENGTH_SHORT).show()
                etEvent.text.clear()
                etGoal.text.clear()
                etMaxValue.text.clear()
                etDailyValue.text.clear()

                initRecyclerView()
            }
        } else {
            Toast.makeText(activity, "Values cannot be blank", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getItemList(): ArrayList<EventModelClass> {
        val eventDatabaseHandler: EventDatabaseHandler = EventDatabaseHandler(context!!)

        return eventDatabaseHandler.viewEvent()
    }
}