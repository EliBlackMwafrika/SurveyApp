package com.example.surveyapp

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


// Start by creating QSLite DATABASE
class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {


//  Create an object to hold static fields
    companion object {
        val DATABASE_NAME = "user.db"
        val TABLE_NAME = "survey_table"
        val COL_1 = "ID"
        val COL_2 = "question_type"
        val COL_3 = "answer_type"
        val COL_4 = "question_text"
        val COL_5 = "options"
    }


    // called to create our database for the first time
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME (ID TEXT PRIMARY KEY " +
                ",NAME TEXT,GALAXY TEXT,TYPE TEXT)")
    }


//     this methode is called when we want to upgrade the database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }


    // This will insert data to the SQLIte DB
    fun insertData(id: String,question_type: String, ans_type: String, question_text: String,options:String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_1, id)
        contentValues.put(COL_2, question_type)
        contentValues.put(COL_3, ans_type)
        contentValues.put(COL_4, question_text)
        contentValues.put(COL_5, options)
        db.insert(TABLE_NAME, null, contentValues)
    }

   // UPDATE table
    fun updateData(id: String,question_type: String, ans_type: String, question_text: String,options: String):
            Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
       contentValues.put(COL_1, id)
       contentValues.put(COL_2, question_type)
       contentValues.put(COL_3, ans_type)
       contentValues.put(COL_4, question_text)
       contentValues.put(COL_5, options)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }


    // get all data from the DB

    val allData : Cursor
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
            return res
        }


}
//end