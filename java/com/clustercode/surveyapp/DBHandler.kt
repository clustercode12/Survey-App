package com.andresnp.surveyapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.ajts.androidmads.library.SQLiteToExcel
import android.os.Environment


class DBHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        var CREATE_SURVEY_TABLE = ("CREATE TABLE " +
                TABLE_SURVEYS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_COLONY_NAME
                + " TEXT," + COLUMN_INTERVIEWED_NAME + " TEXT," + COLUMN_AGE + " INTEGER," + COLUMN_SEX + " TEXT")
        for (i in 1..48) {
            CREATE_SURVEY_TABLE += ",$COLUMN_QUESTION$i TEXT"
        }
        CREATE_SURVEY_TABLE += ")"
        db.execSQL(CREATE_SURVEY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SURVEYS")
        onCreate(db)
    }

    fun addSurvey(survey: Survey) {
        val values = ContentValues()
        values.put(COLUMN_COLONY_NAME, survey.colonyName)
        values.put(COLUMN_INTERVIEWED_NAME, survey.interviewedName)
        values.put(COLUMN_AGE, survey.age)
        values.put(COLUMN_SEX, survey.sex)
        for (i in survey.questions.indices) {
            values.put(COLUMN_QUESTION + (i + 1), survey.questions[i])
        }

        val db = this.writableDatabase

        db.insert(TABLE_SURVEYS, null, values)
        db.close()
    }

    fun exportTable(context: Context) {
        val sqliteToExcel = SQLiteToExcel(context, DATABASE_NAME,
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString())
        sqliteToExcel.exportSingleTable(TABLE_SURVEYS, "$TABLE_SURVEYS.xls", object : SQLiteToExcel.ExportListener {
            override fun onStart() {}
            override fun onCompleted(filePath: String) {
                Toast.makeText(context, context.getString(R.string.result_exported_spreadsheet), Toast.LENGTH_LONG).show()
                }
            override fun onError(e: Exception) {
                Toast.makeText(context, context.getString(R.string.result_exported_spreadsheet_error), Toast.LENGTH_LONG).show()
            }
        })
    }


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "surveyDB.db"
        const val TABLE_SURVEYS = "surveys"

        const val COLUMN_ID = "_id"
        const val COLUMN_COLONY_NAME = "colonyname"
        const val COLUMN_INTERVIEWED_NAME = "interviewedname"
        const val COLUMN_AGE = "age"
        const val COLUMN_SEX = "sex"
        const val COLUMN_QUESTION = "question"
    }


}