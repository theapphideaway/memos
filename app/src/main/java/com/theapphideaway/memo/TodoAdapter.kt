package com.theapphideaway.memo

import android.app.AlertDialog
import android.content.ContentValues
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
import android.widget.CheckBox
import android.widget.RemoteViews
import com.theapphideaway.memo.R.id.check_box_todo
import com.theapphideaway.memo.R.id.list_card_text
import kotlinx.android.synthetic.main.content_todo.*
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

        holder.itemView.check_box_todo.setOnClickListener {

                holder.itemView.list_card_text.apply {

                    if (paintFlags == 1281 || paintFlags ==0)paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    else paintFlags = 0
                }

        }

//        holder.itemView.setOnClickListener {
//            holder.itemView.check_box_todo.isChecked = !holder.itemView.check_box_todo.isChecked
//
//            holder.itemView.list_card_text.apply {
//
//                if (paintFlags == 1281 || paintFlags ==0)paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                else paintFlags = 0
//            }
//
//        }

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

        holder.itemView.check_box_todo.setOnCheckedChangeListener { buttonView, isChecked ->

            var dbManager = TodoDbManager(context)

            println(isChecked)
            val isCheckedValue = if (isChecked) 1 else 0
            println(isCheckedValue)

            var values = ContentValues()
            values.put("ListTitle", holder.itemView.list_card_text.text.toString())
            values.put("IsChecked", isCheckedValue)


            var id = position + 1
            var selectionArgs = arrayOf(id.toString())
            val Id = dbManager.update(values, "Id=?", selectionArgs)
            if (Id > 0) {
                Toast.makeText(context, "Check is added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Didnt Add List", Toast.LENGTH_SHORT).show()
            }
        }

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindNote( todo: ToDo){
            var titleText: TextView = itemView.findViewById(R.id.list_card_text) as TextView
            var checkBox: CheckBox = itemView.findViewById(R.id.check_box_todo) as CheckBox

            var checked = todo.IsChecked!! > 0
            if (checked) titleText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            titleText.text = todo.ListTitle
            checkBox.isChecked = checked
        }

    }
}