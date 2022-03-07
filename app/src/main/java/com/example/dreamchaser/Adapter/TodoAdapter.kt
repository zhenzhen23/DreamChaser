package com.example.dreamchaser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamchaser.Database.TodoDatabaseHandler
import com.example.dreamchaser.Model.TodoModelClass
import com.example.dreamchaser.R
import kotlinx.android.synthetic.main.todo_row.view.*

/**
 * Adapter for todo recycle view
 */
class TodoAdapter(val context: Context, val items: ArrayList<TodoModelClass>) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private val todoDatabase: TodoDatabaseHandler = TodoDatabaseHandler(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTodoTitle = view.tvTodoTitle
        val ivDelete = view.ivDelete
        val ivCheck = view.ivCheck
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.tvTodoTitle.text = item.todo

        /* When click delete button, delete item in database and display a toast*/
        holder.ivDelete.setOnClickListener {
            todoDatabase.deleteTodo(item)
            Toast.makeText(context, "Todo Deleted.", Toast.LENGTH_SHORT).show()
            items.removeAt(position)
            notifyDataSetChanged()
        }

        /* When click check button, remove item in database and display a toast*/
        holder.ivCheck.setOnClickListener {
            todoDatabase.deleteTodo(item)
            Toast.makeText(context, "Todo finished.", Toast.LENGTH_SHORT).show()
            items.removeAt(position)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}