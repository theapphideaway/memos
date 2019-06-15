package com.theapphideaway.memo.fragments

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.theapphideaway.memo.AddNote
import com.theapphideaway.memo.Database.DbManager
import com.theapphideaway.memo.Database.TodoDbManager
import com.theapphideaway.memo.Model.Note
import com.theapphideaway.memo.Model.ToDo
import com.theapphideaway.memo.NoteAdapter
import com.theapphideaway.memo.R
import com.theapphideaway.memo.TodoAdapter
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.content_todo.*
import kotlinx.android.synthetic.main.content_todo.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_todo_list.view.*
import kotlinx.android.synthetic.main.list_card.view.*

class TodoListFragment: Fragment() {

    var index = 0

    private var todoAdapter: TodoAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var todoList: ArrayList<ToDo>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_todo_list, container, false)

//        MobileAds.initialize(rootView.context, "ca-app-pub-2688427047309255~1198112356");
//        val mAdView = rootView.findViewById(R.id.ad_view) as AdView
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)

        var dbManager = TodoDbManager(rootView.context)

        todoList = ArrayList()
        layoutManager = LinearLayoutManager(rootView.context)
        todoAdapter = TodoAdapter(todoList!!, rootView.context)

        rootView.delete_button.setOnClickListener {

            val builder = AlertDialog.Builder(context)

            builder.setTitle("Delete All")

            builder.setMessage("Are you sure you want to delete your list?")

            builder.setPositiveButton("YES"){ _, _ ->
                dbManager.deleteAll()
                loadQuery("%")
                todoAdapter!!.notifyDataSetChanged()
                true
            }

            builder.setNegativeButton("No"){ _, _ ->
                Toast.makeText(context,"No Changes Made", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()

            dialog.show()

        }

        rootView.fab_todo.setOnClickListener { view ->
            var values = ContentValues()
            values.put("ListTitle", edit_text_todo_list.text.toString())
            values.put("IsChecked", 0)

            if (index == 0) {
                val ID = dbManager.ListInsert(values)
                if (ID > 0) {
                    Toast.makeText(rootView.context, "Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(rootView.context, "Error, Couldn't Add", Toast.LENGTH_SHORT).show()
                }

                edit_text_todo_list.text = null

            } else {
                var selectionArgs = arrayOf(id.toString())
                val Id = dbManager.update(values, "Id=?", selectionArgs)
                if (Id > 0) {
                    Toast.makeText(rootView.context, "Updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(rootView.context, "Error: Couldn't Update", Toast.LENGTH_SHORT).show()
                }
            }

            loadQuery("%")


            todoAdapter!!.notifyDataSetChanged()

        }

        rootView.list_recycler_view.adapter = todoAdapter
        rootView.list_recycler_view.layoutManager = layoutManager

        loadQuery("%")

        todoAdapter!!.notifyDataSetChanged()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        //loadQuery("%")
        todoAdapter!!.notifyDataSetChanged()
    }

    fun loadQuery(title:String){
        var dbManager= TodoDbManager(this.context!!)
        val projections= arrayOf("Id","ListTitle", "IsChecked")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.query(projections,"ListTitle like ?",selectionArgs)
        todoList!!.clear()
        if(cursor.moveToFirst()){

            do{
                //try writing this with the no constructor in the notes class
                val ID=cursor.getInt(cursor.getColumnIndex("Id"))
                val Title=cursor.getString(cursor.getColumnIndex("ListTitle"))
                val IsChecked = cursor.getInt(cursor.getColumnIndex("IsChecked"))


                todoList!!.add(ToDo(ID,Title, IsChecked))

            }while (cursor.moveToNext())
        }
    }
}