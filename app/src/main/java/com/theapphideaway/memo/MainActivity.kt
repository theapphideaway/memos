package com.theapphideaway.memo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.theapphideaway.memo.Model.Note

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {


    val REQUEST_CODE = 1

    private var noteAdapter: NoteAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var noteList: ArrayList<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this, AddNote::class.java)
            startActivityForResult(intent, 1)
        }

        noteList = ArrayList()
        layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(noteList!!, this)

        note_recycler_view.adapter = noteAdapter
        note_recycler_view.layoutManager = layoutManager

        loadQuery("%")

        noteAdapter!!.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
        noteAdapter!!.notifyDataSetChanged()
    }


    fun loadQuery(title:String){
        var dbManager=DbManager(this)
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE){
//            if(resultCode == Activity.RESULT_OK){
//                var title = data!!.extras.get("title")
//                var content = data!!.extras.get("content")
//                val note = Note()
//                note.Title = title.toString()
//                note.Content = content.toString()
//                noteList!!.add(note)
//            }
//        }
//    }

}
