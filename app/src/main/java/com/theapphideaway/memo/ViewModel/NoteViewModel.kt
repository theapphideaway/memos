package com.theapphideaway.memo.ViewModel

import android.content.Context
import com.theapphideaway.memo.Database.DbManager
import com.theapphideaway.memo.Model.Note

class NoteViewModel {

    fun loadNote(title:String, noteList: ArrayList<Note>, context: Context){
        val dbManager= DbManager(context)
        val projections= arrayOf("Id","Title","Content")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.query(projections,"Title like ?",selectionArgs)
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