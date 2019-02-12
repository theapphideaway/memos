package com.theapphideaway.memo

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

        val b: String = "Tutorial"

        val str = StringBuilder(b)
        println("string = $str")

        // insert character at offset 8
        str.insert(8, 's')

        //How do I get the current index???

        // print StringBuilder after insertion
        print("After insertion = ")
        println(str.toString())



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
                note_actions_button.setBackgroundColor(Color.WHITE)
            }
            else{
                note_types_button.setBackgroundColor(Color.WHITE)
            }
        }

        note_actions_button.setOnClickListener {
            numberedPressed = !numberedPressed
            if(numberedPressed){
                bulletPressed = false
                note_actions_button.setBackgroundColor(Color.LTGRAY)
                note_types_button.setBackgroundColor(Color.WHITE)
            }
            else{
                note_actions_button.setBackgroundColor(Color.WHITE)
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
    };


    var noteTextWatcher: TextWatcher = object : TextWatcher {

        var newText = null

        override fun afterTextChanged(s: Editable) {
            deletePressed = false
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            //edit_text_content.removeTextChangedListener(this)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            var thisCount = 0

            for (index in s) {
                thisCount++
            }
            if (deletePressed && start == 0) {
                deletePressed = false
            }

//                if(!numberedPressed){
//                    numbersInList = 1
//                }



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


    fun setBulletText(myEdit: EditText, text: String) {
        val lines = TextUtils.split(text, "\n")
        val spannableStringBuilder = SpannableStringBuilder()
        var line: String? = null
        for (index in lines.indices) {
            line = lines[index]
            val length = spannableStringBuilder.length
            spannableStringBuilder.append(line)
            if (index != lines.size - 1) {
                spannableStringBuilder.append("\n")
            } else if (TextUtils.isEmpty(line)) {
                spannableStringBuilder.append("\u200B")
            }
            spannableStringBuilder.setSpan(
                BulletSpan(30), length, length + 1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        myEdit.setText(spannableStringBuilder)
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

        if (edit_text_title.text.toString() != "" && edit_text_content.text.toString() != "") {

            var dbManager = DbManager(this)

            var values = ContentValues()
            values.put("Title", edit_text_title.text.toString())
            values.put("Content", edit_text_content.text.toString())

            if (id == 0) {
                val ID = dbManager.Insert(values)
                if (ID > 0) {
                    Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Couldn't be added", Toast.LENGTH_SHORT).show()
                }
            } else {
                var selectionArgs = arrayOf(id.toString())
                val Id = dbManager.update(values, "Id=?", selectionArgs)
                if (Id > 0) {
                    Toast.makeText(this, "Note is update", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Couldn't be updated", Toast.LENGTH_SHORT).show()
                }
            }

        } else{
            Toast.makeText(this, "Didn't save note with empty Title and/or Content", Toast.LENGTH_LONG).show()
        }
        finish()
    }

}


