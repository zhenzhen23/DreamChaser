package com.example.dreamchaser.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamchaser.Database.TodoDatabaseHandler
import com.example.dreamchaser.Adapter.TodoAdapter
import com.example.dreamchaser.R
import com.example.dreamchaser.Model.TodoModelClass
import kotlinx.android.synthetic.main.fragment_todo.*

/**
 * Fragment class for todo page
 */
class TodoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddTodo.setOnClickListener { view ->
            addRecord(view)
        }
        setupListofDataIntoRecyclerView()
    }

    /* Add record to database*/
    private fun addRecord(view: View?) {
        val todo = etTodo.text.toString()
        val todoDatabaseHandler: TodoDatabaseHandler? = TodoDatabaseHandler(context!!)

        if (todo.isNotEmpty()) {
            val status = todoDatabaseHandler?.addTodo(TodoModelClass(0, todo))
            if (status != null) {
                if (status > -1) {
                    Toast.makeText(activity, "Added to Todo List", Toast.LENGTH_SHORT).show()
                    etTodo.text.clear()

                    setupListofDataIntoRecyclerView()
                }
            }
        } else {
            Toast.makeText(activity, "Todo cannot be blank", Toast.LENGTH_SHORT).show()
        }
    }

    /* set up recycler view in todo list page*/
    private fun setupListofDataIntoRecyclerView() {

        if (getItemList().size > 0) {
            rvTodos.visibility = View.VISIBLE
            rvTodos.layoutManager = LinearLayoutManager(activity)
            rvTodos.adapter = TodoAdapter(context!!, getItemList())
        } else {
            rvTodos.visibility = View.GONE
        }
    }

    private fun getItemList(): ArrayList<TodoModelClass> {
        val todoDatabaseHandler: TodoDatabaseHandler = TodoDatabaseHandler(context!!)

        return todoDatabaseHandler.viewTodo()
    }
}