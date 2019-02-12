package com.theapphideaway.memo.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class TodoDbManager {

    val dbName = "MyLists"
    val dbTable = "MyListsTable"
    val colListId = "Id"
    val colListTitle = "ListTitle"
    val colIsChecked = "IsChecked"
    val dbVersion = 1
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colListId + " INTEGER PRIMARY KEY," +
            colListTitle + " TEXT," + colIsChecked + " INTEGER);"


    var sqliteDBList: SQLiteDatabase? = null


    constructor(context: Context){
        var db = DatabaseHelperNotes(context)
        sqliteDBList = db.writableDatabase

    }

    inner class DatabaseHelperNotes: SQLiteOpenHelper {

        var context: Context? = null

        constructor(context: Context):super(context, dbName,null, dbVersion){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, " database is created", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS " + dbTable)
        }

    }

    fun ListInsert(values: ContentValues):Long{
        val ID = sqliteDBList!!.insert(dbTable, "", values)
        return ID
    }

    fun query(projection:Array<String>, selection: String, selectionArgs: Array<String>): Cursor {

        val qb= SQLiteQueryBuilder()
        qb.tables=dbTable
        val cursor=qb.query(sqliteDBList,projection,selection,selectionArgs,null,null,null)
        return cursor

    }



    fun delete(selection: String, selectionArgs: Array<String>): Int{
        return sqliteDBList!!.delete(dbTable, selection, selectionArgs)
    }

    fun deleteAll(){
        sqliteDBList!!.delete(dbTable,null,null)
    }

    fun update(values: ContentValues, selection:String, selectionArgs:Array<String>):Int{
        return sqliteDBList!!.update(dbTable, values, selection, selectionArgs)
    }


}