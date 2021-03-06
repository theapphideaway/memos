package com.theapphideaway.memo

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.theapphideaway.memo.Database.DbManager
import kotlinx.android.synthetic.main.activity_add_note.*
import android.text.Spannable
import android.text.style.BulletSpan
import android.text.TextUtils
import android.text.SpannableStringBuilder
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.support.annotation.NonNull
import com.theapphideaway.memo.Database.TodoDbManager
import com.theapphideaway.memo.Model.FileManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddNote : AppCompatActivity() {

    var id = 0
    var deletePressed = false
    var bulletPressed = false
    var numberedPressed = false
    var numbersInList = 1
    var oldTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatted = current.format(formatter)

        bottom_title.text =formatted

        try{
            var bundle:Bundle=intent.extras
            id=bundle.getInt("Id",0)
            //if(id!=0) {
                edit_text_title.setText(bundle.getString("Title") )
                edit_text_content.setText(bundle.getString("Content") )

                oldTitle = edit_text_title.text.toString()
            //}
        }catch (ex:Exception){}

        edit_text_content.addTextChangedListener(noteTextWatcher)

        note_types_button.setOnClickListener {
            bulletPressed = !bulletPressed
            if(bulletPressed){
                numberedPressed = false
                note_types_button.setBackgroundColor(Color.LTGRAY)
                note_actions_button.setBackgroundColor(resources.getColor(R.color.colorBackground))
            }
            else{
                note_types_button.setBackgroundColor(resources.getColor(R.color.colorBackground))
            }
        }

        note_actions_button.setOnClickListener {
            numberedPressed = !numberedPressed
            if(numberedPressed){
                bulletPressed = false
                note_actions_button.setBackgroundColor(Color.LTGRAY)
                note_types_button.setBackgroundColor(resources.getColor(R.color.colorBackground))
            }
            else{
                note_actions_button.setBackgroundColor(resources.getColor(R.color.colorBackground))
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_DEL) {
            if (event.action == KeyEvent.ACTION_UP) {
                return true
            }
        }

        deletePressed = true
        return super.dispatchKeyEvent(event)
    }


    var noteTextWatcher: TextWatcher = object : TextWatcher {


        override fun afterTextChanged(s: Editable) {
            deletePressed = false
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            var thisCount = 0

            for (index in s) {
                thisCount++
            }
            if (deletePressed && start == 0) {
                deletePressed = false
            }


            if (!deletePressed) {
                if (count != 0 && edit_text_content.selectionEnd > 0) {
                    if (s[edit_text_content.selectionEnd - 1] == '\n') {
                        println("I Pressed enter")


                        if(bulletPressed){
                            edit_text_content.text.insert(edit_text_content.selectionStart, "\u25CF ")
                        }

                        if(!numberedPressed){
                            numbersInList = 1
                        }
                        else if (numberedPressed){
                            edit_text_content.text.insert(edit_text_content.selectionStart, "$numbersInList. ")
                            numbersInList++
                        }


                    }
                    deletePressed = false
                }
                deletePressed = false
            }
            deletePressed = false
        }



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
                Toast.makeText(this, "Note is Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error: Couldn't Add", Toast.LENGTH_SHORT).show()
            }
        } else {
            var selectionArgs = arrayOf(id.toString())
            val Id = dbManager.update(values, "Id=?", selectionArgs)
            if (Id > 0) {
                Toast.makeText(this, "Note is Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error: Couldn't Update", Toast.LENGTH_SHORT).show()
            }
        }



        finish()

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {


        if (edit_text_title.text.toString() != "" && edit_text_content.text.toString() != "") {

            var dbManager = DbManager(this)

            var values = ContentValues()
            values.put("Title", edit_text_title.text.toString())
            values.put("Content", edit_text_content.text.toString())

            if (id == 0) {
                val ID = dbManager.Insert(values)
                if (ID > 0) {
                    Toast.makeText(this, "Note is Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: Couldn't Add", Toast.LENGTH_SHORT).show()
                }
            } else {
                var selectionArgs = arrayOf(id.toString())
                val Id = dbManager.update(values, "Id=?", selectionArgs)
                if (Id > 0) {
                    Toast.makeText(this, "Note is Update", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: Couldn't Update", Toast.LENGTH_SHORT).show()
                }
            }

            super.onBackPressed()

        } else{

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Warning")

            builder.setMessage("Both title and content need to have values or this note will be deleted. " +
                    "Are you sure you want to delete this note?")

            builder.setPositiveButton("Yes"){dialog, which ->
                super.onBackPressed()
                Toast.makeText(this, "Didn't Save", Toast.LENGTH_LONG).show()
                true
            }

            builder.setNegativeButton("No"){dialog,which ->
                Toast.makeText(this,"No changes made", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()

            dialog.show()
        }
    }
}


