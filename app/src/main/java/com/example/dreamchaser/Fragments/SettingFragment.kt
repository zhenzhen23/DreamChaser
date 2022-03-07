package com.example.dreamchaser.Fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.dreamchaser.Database.EventDatabaseHandler
import com.example.dreamchaser.Database.TodoDatabaseHandler
import com.example.dreamchaser.R
import kotlinx.android.synthetic.main.fragment_setting.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment class for setting page
 */
class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todoDatabase: TodoDatabaseHandler = TodoDatabaseHandler(context!!)
        val eventDatabase: EventDatabaseHandler = EventDatabaseHandler(context!!)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        /**
         * Set click listener for change username button
         * when user click this button popup an alertDialog
         * when user enter username save it to info file
         */
        btnSettingUsername.setOnClickListener { view ->

            val alertDialog = AlertDialog.Builder(context)
            val editText = EditText(context)
            editText.maxLines = 1

            alertDialog.setTitle("Enter New Username")
            alertDialog.setView(editText)
            alertDialog.setPositiveButton("Add") { dialog, i ->

                editUserInfo(0, editText.text.toString())

                Toast.makeText(activity, "Updated Username", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            alertDialog.setNegativeButton("Cancel") { dialog, i ->
                dialog.cancel()
            }
            alertDialog.show()
        }

        /**
         * Set click listener for change goal button
         * when user click this button popup an alertDialog
         * when user enter new goal save it to info file
         */
        btnSettingGoal.setOnClickListener { view ->

            val alertDialog = AlertDialog.Builder(context)
            val editText = EditText(context)
            editText.maxLines = 1

            alertDialog.setTitle("Enter New Goal")
            alertDialog.setView(editText)
            alertDialog.setPositiveButton("Add") { dialog, i ->

                editUserInfo(1, editText.text.toString())

                Toast.makeText(activity, "Updated Goal", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            alertDialog.setNegativeButton("Cancel") { dialog, i ->
                dialog.cancel()
            }
            alertDialog.show()
        }

        /**
         * Set click listener for change expected date button
         * when user click this button popup an alertDialog
         * when user enter new expected date save it to info file
         */
        btnSettingDate.setOnClickListener { view ->

            val dpd = DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val calender: Calendar = Calendar.getInstance()
                    calender.set(year, monthOfYear, dayOfMonth)

                    val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

                    editUserInfo(2, sdf.format(calender.time))
                    Toast.makeText(activity, "Updated Expected Date", Toast.LENGTH_SHORT).show()
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        /**
         * Set click listener for change reset button
         * when user click this button reset whole app
         */
        btnSettingReset.setOnClickListener { view ->

            val alertDialog = AlertDialog.Builder(context)

            alertDialog.setTitle("Are you sure you want to reset app?")
            alertDialog.setPositiveButton("Yes") { dialog, i ->

                todoDatabase.dropTable()
                eventDatabase.dropTable()

                val fileName = "userInfo.txt"
                var file = File(context?.filesDir, fileName)
                file.delete()

                Toast.makeText(activity, "App reseted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            alertDialog.setNegativeButton("No") { dialog, i ->
                dialog.cancel()
            }
            alertDialog.show()
        }
    }

    /**
     * Get user input and update corresponding data
     */
    private fun editUserInfo(line: Int, data: String) {
        val fileName = "userInfo.txt"
        var info: List<String>?

        context?.openFileInput(fileName).use { stream ->
            info = stream?.bufferedReader().use {
                it?.readLines()
            }
        }

        /* change immutable list to mutable list*/
        var mutableInfo = info?.toMutableList()

        mutableInfo?.set(line, data)

        context?.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            for (item in mutableInfo!!) {
                output?.write((item + "\n").toByteArray())
            }
        }
    }
}