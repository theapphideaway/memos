package com.theapphideaway.memo

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.theapphideaway.memo.R.id.edit_text_title
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNote : AppCompatActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        try{
            var bundle:Bundle=intent.extras
            id=bundle.getInt("Id",0)
            if(id!=0) {
                edit_text_title.setText(bundle.getString("Title") )
                edit_text_content.setText(bundle.getString("Content") )
            }
        }catch (ex:Exception){}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        var dbManager = DbManager(this)

        var values = ContentValues()
        values.put("Title", edit_text_title.text.toString())
        values.put("Content", edit_text_content.text.toString())

        if (id == 0) {
            val ID = dbManager.Insert(values)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Couldn't added", Toast.LENGTH_SHORT).show()
            }
        } else {
            var selectionArgs = arrayOf(id.toString())
            val Id = dbManager.update(values, "Id=?", selectionArgs)
            if (Id > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Couldn't added", Toast.LENGTH_SHORT).show()
            }
        }



        finish()

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        var dbManager = DbManager(this)

        var values = ContentValues()
        values.put("Title", edit_text_title.text.toString())
        values.put("Content", edit_text_content.text.toString())

        if (id == 0) {
            val ID = dbManager.Insert(values)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Couldn't added", Toast.LENGTH_SHORT).show()
            }
        } else {
            var selectionArgs = arrayOf(id.toString())
            val Id = dbManager.update(values, "Id=?", selectionArgs)
            if (Id > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Couldn't added", Toast.LENGTH_SHORT).show()
            }
        }

        finish()
    }

}
