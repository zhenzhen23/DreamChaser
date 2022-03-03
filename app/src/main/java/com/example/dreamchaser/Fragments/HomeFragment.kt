package com.example.dreamchaser.Fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamchaser.Adapter.GoalAdapter
import com.example.dreamchaser.Database.EventDatabaseHandler
import com.example.dreamchaser.Model.EventModelClass
import com.example.dreamchaser.R
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var eventList = getItemList()

        initTitle()
        initGridLayout(eventList)
        initRecyclerView(eventList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTitle() {
        val fileName = "userInfo.txt"
        var file = File(context?.filesDir, fileName)

        if (file.exists()) {
            var info: List<String>?

            context?.openFileInput(fileName).use { stream ->
                info = stream?.bufferedReader().use {
                    it?.readLines()
                }
            }

            tvFinalGoal.text = info?.get(1) ?: ""
            tvDays.text =
                ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(info?.get(2))).toString()
        } else {

            context?.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                output?.write("Username\n".toByteArray())
                output?.write("Set Your Goal\n".toByteArray())
                output?.write((LocalDate.now().toString() + "\n").toByteArray())
            }
        }
    }

    private fun initGridLayout(eventList: ArrayList<EventModelClass>) {

        val goalDatabaseHandler: EventDatabaseHandler = EventDatabaseHandler(context!!)
        val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        param.leftMargin = 30
        param.rightMargin = 30
        param.topMargin = 30
        param.bottomMargin = 30

        if (eventList.isEmpty()){
            val tv: TextView = TextView(context)
            tv.text = "Enter you daily event in event page"
            tv.textSize = 24F
            glDailyEvents.addView(tv, param)
        }else{
            for (item in eventList) {
                val cb: CheckBox = CheckBox(context)
                cb.text = item.event
                cb.textSize = 18F

                cb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                    cb.isEnabled = false
                    goalDatabaseHandler.addValue(item)
                    initRecyclerView(getItemList())
                })
                glDailyEvents.addView(cb, param)
            }
        }
    }

    private fun initRecyclerView(eventList: ArrayList<EventModelClass>) {

        if (eventList.size > 0) {
            rvGoals.visibility = View.VISIBLE
            rvGoals.layoutManager = LinearLayoutManager(activity)
            rvGoals.adapter = GoalAdapter(context!!, eventList)
        } else {
            rvGoals.visibility = View.GONE
        }
    }

    private fun getItemList(): ArrayList<EventModelClass> {
        val goalDatabaseHandler: EventDatabaseHandler = EventDatabaseHandler(context!!)

        return goalDatabaseHandler.viewEvent()
    }
}