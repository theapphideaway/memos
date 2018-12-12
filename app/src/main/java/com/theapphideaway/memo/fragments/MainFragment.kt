package com.theapphideaway.memo.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.theapphideaway.memo.AddNote
import com.theapphideaway.memo.Database.DbManager
import com.theapphideaway.memo.Model.Note
import com.theapphideaway.memo.NoteAdapter
import com.theapphideaway.memo.R
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment: Fragment() {

    private var noteAdapter: NoteAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var noteList: ArrayList<Note>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        rootView.fab.setOnClickListener { view ->
            var intent = Intent(rootView.context, AddNote::class.java)
            startActivityForResult(intent, 1)
        }



        noteList = ArrayList()
        layoutManager = LinearLayoutManager(rootView.context)
        noteAdapter = NoteAdapter(noteList!!, rootView.context)

        rootView.note_recycler_view.adapter = noteAdapter
        rootView.note_recycler_view.layoutManager = layoutManager

        loadQuery("%")

        noteAdapter!!.notifyDataSetChanged()
        return rootView

    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
        noteAdapter!!.notifyDataSetChanged()
    }

    fun loadQuery(title:String){
        var dbManager= DbManager(this.context!!)
        val projections= arrayOf("Id","Title","Content")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.query(projections,"Title like ?",selectionArgs,"Title")
        noteList!!.clear()
        if(cursor.moveToFirst()){

            do{
                //try writing this with the no constructor in the notes class
                val ID=cursor.getInt(cursor.getColumnIndex("Id"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Content"))

                noteList!!.add(Note(ID,Title,Description))

            }while (cursor.moveToNext())
        }
    }
}