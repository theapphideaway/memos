package com.theapphideaway.memo.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager{

    val dbName = "MyNotes"
    val dbTable = "MyTable"
    val colId = "Id"
    val colTitle = "Title"
    val colContent = "Content"
    val dbVersion = 1
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colId + " INTEGER PRIMARY KEY," +
            colTitle + " TEXT, " + colContent + " TEXT);"

    var sqliteDB: SQLiteDatabase? = null


    constructor(context: Context){
        var db = DatabaseHelperNotes(context)
        sqliteDB = db.writableDatabase

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

    fun Insert(values: ContentValues):Long{
        val ID = sqliteDB!!.insert(dbTable, "", values)

        return ID
    }

    fun query(projection:Array<String>, selection: String, selectionArgs: Array<String>, sortOrder:String): Cursor {

        val qb= SQLiteQueryBuilder()
        qb.tables=dbTable
        val cursor=qb.query(sqliteDB,projection,selection,selectionArgs,null,null,sortOrder)
        return cursor

    }

    fun delete(selection: String, selectionArgs: Array<String>): Int{
        return sqliteDB!!.delete(dbTable, selection, selectionArgs)
    }

    fun update(values: ContentValues, selection:String, selectionArgs:Array<String>):Int{
        return sqliteDB!!.update(dbTable, values, selection, selectionArgs)
    }


}