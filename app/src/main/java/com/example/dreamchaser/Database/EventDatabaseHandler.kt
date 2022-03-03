package com.example.dreamchaser.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.dreamchaser.Model.EventModelClass
import com.example.dreamchaser.Model.TodoModelClass

class EventDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MyDatabase"
        private const val TABLE_CONTACTS = "EventTable"

        private const val KEY_ID = "id"
        private const val KEY_EVENT = "event"
        private const val KEY_GOAL = "goal"
        private const val KEY_MAX_VALUE = "maxValue"
        private const val KEY_DAILY_VALUE = "dailyValue"
        private const val KEY_CURRENT_VALUE = "currentValue"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + EventDatabaseHandler.TABLE_CONTACTS + "("
                + EventDatabaseHandler.KEY_ID + " INTEGER PRIMARY KEY,"
                + EventDatabaseHandler.KEY_EVENT + " TEXT,"
                + EventDatabaseHandler.KEY_GOAL + " TEXT,"
                + EventDatabaseHandler.KEY_MAX_VALUE + " INTEGER,"
                + EventDatabaseHandler.KEY_DAILY_VALUE + " INTEGER,"
                + EventDatabaseHandler.KEY_CURRENT_VALUE + " INTEGER"
                + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${EventDatabaseHandler.TABLE_CONTACTS}")
        onCreate(db)
    }

    fun addEvent(event: EventModelClass): Long {
        var db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_EVENT, event.event)
        contentValues.put(KEY_GOAL, event.goal)
        contentValues.put(KEY_MAX_VALUE, event.maxValue)
        contentValues.put(KEY_DAILY_VALUE, event.dailyValue)
        contentValues.put(KEY_CURRENT_VALUE, 0)

        val succes = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return succes
    }

    @SuppressLint("Range")
    fun viewEvent(): ArrayList<EventModelClass> {
        val eventList: ArrayList<EventModelClass> = ArrayList<EventModelClass>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var event: String
        var goal: String
        var maxValue: Int
        var dailyValue: Int
        var currentValue: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                event = cursor.getString(cursor.getColumnIndex(KEY_EVENT))
                goal = cursor.getString(cursor.getColumnIndex(KEY_GOAL))
                maxValue = cursor.getInt(cursor.getColumnIndex(KEY_MAX_VALUE))
                dailyValue = cursor.getInt(cursor.getColumnIndex(KEY_DAILY_VALUE))
                currentValue = cursor.getInt(cursor.getColumnIndex(KEY_CURRENT_VALUE))

                val event = EventModelClass(
                    id = id,
                    event = event,
                    goal = goal,
                    maxValue = maxValue,
                    dailyValue = dailyValue,
                    currentValue = currentValue
                )
                eventList.add(event)
            } while (cursor.moveToNext())
        }
        return eventList
    }

    fun deleteEvent(event: EventModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, event.id)

        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + event.id, null)
        db.close()
        return success
    }

    fun addValue(event: EventModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_EVENT, event.event)
        contentValues.put(KEY_GOAL, event.goal)
        contentValues.put(KEY_MAX_VALUE, event.maxValue)
        contentValues.put(KEY_DAILY_VALUE, event.dailyValue)
        var value = event.currentValue + event.dailyValue
        if (value > event.maxValue) value = event.maxValue
        contentValues.put(KEY_CURRENT_VALUE, value)

        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + event.id, null)

        db.close()
        return success
    }

    fun dropTable(){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_CONTACTS")
        db.close()
    }
}