package com.example.dreamchaser.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.dreamchaser.Model.TodoModelClass

class TodoDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MyDatabase2"
        private const val TABLE_CONTACTS = "TodoTable"

        private const val KEY_ID = "id"
        private const val KEY_TODO = "todo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addTodo(todo: TodoModelClass): Long {

        var db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TODO, todo.todo)

        val succes = db.insert(TABLE_CONTACTS, null, contentValues)

        db.close()
        return succes
    }

    @SuppressLint("Range")
    fun viewTodo(): ArrayList<TodoModelClass> {
        val todoList: ArrayList<TodoModelClass> = ArrayList<TodoModelClass>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var todo: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                todo = cursor.getString(cursor.getColumnIndex(KEY_TODO))

                val todo = TodoModelClass(
                    id = id,
                    todo = todo
                )
                todoList.add(todo)
            } while (cursor.moveToNext())
        }
        return todoList
    }

    fun updateTodo(todo: TodoModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TODO, todo.todo)

        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + todo.id, null)

        db.close()
        return success
    }

    fun deleteTodo(todo: TodoModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, todo.id)

        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + todo.id, null)
        db.close()
        return success
    }

    fun dropTable(){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_CONTACTS")
        db.close()
    }
}