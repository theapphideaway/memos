package com.theapphideaway.memo

import android.content.ContentValues
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




class AddNote : AppCompatActivity() {

    var id = 0
    var deletePressed = false

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

        try{
            var bundle:Bundle=intent.extras
            id=bundle.getInt("Id",0)
            if(id!=0) {
                edit_text_title.setText(bundle.getString("Title") )
                edit_text_content.setText(bundle.getString("Content") )
            }
        }catch (ex:Exception){}

//        val text = "My text <ul><li>bullet one</li><li>bullet two</li></ul>"
//
//        edit_text_content.text = Html.fromHtml(text) as Editable?

        edit_text_content.addTextChangedListener(noteTextWatcher)


//        edit_text_content.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
//            // If the event is a key-down event on the "enter" button
//
//            var foo = event.action
//
//            if (keyCode == KeyEvent.KEYCODE_DEL) {
//                deletePressed = true
//
//                //Toast.makeText(this@HelloFormStuff, edittext.getText(), Toast.LENGTH_SHORT).show()
//                return@OnKeyListener true
//            }
//
//            false
//        })





       // edit_text_content.setOnEditorActionListener(listener)


//        edit_text_content.setOnEditorActionListener { v, actionId, event ->
//
//            var foo = event
//            var foob = v
//
//            if (actionId == 123321|| actionId == EditorInfo.IME_NULL) {
//                val oldString = edit_text_content.text.toString()
//                val newString = oldString + "\n\u25CF"
//                edit_text_content.setText(newString)
//                edit_text_content.setSelection(edit_text_content.text.length)
//
//            }
//
//            if (actionId == EditorInfo.IME_NULL
//                && event.action == KeyEvent.ACTION_DOWN) {
//                //example_confirm();//match this behavior to your 'Send' (or Confirm) button
//            }
//
//            if (actionId == EditorInfo.IME_NULL || event == null) {
//                val oldString = edit_text_content.text.toString()
//                val newString = oldString + "\n\u25CF"
//                edit_text_content.setText(newString)
//                edit_text_content.setSelection(edit_text_content.text.length)
//            }
//            else{
//                true
//            }
//            false
//        }

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
//            if (s.toString().takeLast(1) == "\n") {
//                println("I pressed enter")
//                //edit_text_content.text = edit_text_content.text.insert(edit_text_content.selectionStart, "\t\u2022 ")
//
//            }
            deletePressed = false
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            //edit_text_content.removeTextChangedListener(this)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            var count = 0
            for (index in s) {
                count++
            }
            if(deletePressed && start == 0)
            {
                deletePressed = false
            }

           

            if (!deletePressed) {
                if (count != 0 && edit_text_content.selectionEnd > 0) {
                    if (s[edit_text_content.selectionEnd - 1] == '\n') {
                        println("I Pressed enter")
                        edit_text_content.text.insert(edit_text_content.selectionStart, "\u25CF ")
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
