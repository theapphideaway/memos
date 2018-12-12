package com.theapphideaway.memo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.theapphideaway.memo.Database.DbManager
import com.theapphideaway.memo.Database.TodoDbManager
import com.theapphideaway.memo.Model.Note
import com.theapphideaway.memo.Model.ToDo
import kotlinx.android.synthetic.main.list_card.view.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.widget.RemoteViews
import com.theapphideaway.memo.R.id.list_card_text
import java.lang.reflect.Array.setInt



class TodoAdapter (private val todoList: ArrayList<ToDo>, private val context: Context):
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {

        var noteCard = LayoutInflater.from(context).inflate(R.layout.list_card, viewGroup, false)

        return ViewHolder(noteCard)
    }



    override fun getItemCount(): Int {
        return todoList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindNote(todoList[position])

        holder.itemView.setOnClickListener {
            holder.itemView.check_box_todo.isChecked = !holder.itemView.check_box_todo.isChecked

            //holder.itemView.list_card_text.font
            holder.itemView.list_card_text.apply {

                if (paintFlags == 1281 || paintFlags ==0)paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else paintFlags = 0
            }

            //remoteViews.setInt(R.id.list_card_text, "setPaintFlags", Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
        }

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Delete Note")

            builder.setMessage("Are you sure you want to delete this note?")

            builder.setPositiveButton("YES"){dialog, which ->

                var dbManager = TodoDbManager(this.context!!)
                val selectionArgs= arrayOf(todoList[position].Id.toString())
                dbManager.delete("Id=?", selectionArgs )
                todoList.removeAt(position)
                notifyItemRemoved(position)
                true

            }

            builder.setNegativeButton("No"){dialog,which ->
                Toast.makeText(context,"No changes made", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()

            dialog.show()

            true
        }

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindNote( todo: ToDo){
            var titleText: TextView = itemView.findViewById(R.id.list_card_text) as TextView

            titleText.text = todo.ListTitle
        }

    }
}