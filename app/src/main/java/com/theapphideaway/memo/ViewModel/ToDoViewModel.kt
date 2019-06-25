package com.theapphideaway.memo.ViewModel

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import com.theapphideaway.memo.Database.TodoDbManager
import com.theapphideaway.memo.Model.ToDo
import kotlinx.android.synthetic.main.content_todo.*

class ToDoViewModel {

    fun loadTodo(title:String, todoList: ArrayList<ToDo>, context: Context){
        var dbManager= TodoDbManager(context)
        val projections= arrayOf("Id","ListTitle", "IsChecked")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.query(projections,"ListTitle like ?",selectionArgs)
        todoList!!.clear()
        if(cursor.moveToFirst()){
            do{
                val ID=cursor.getInt(cursor.getColumnIndex("Id"))
                val Title=cursor.getString(cursor.getColumnIndex("ListTitle"))
                val IsChecked = cursor.getInt(cursor.getColumnIndex("IsChecked"))

                todoList!!.add(ToDo(ID,Title, IsChecked))

            } while (cursor.moveToNext())
        }
    }


}