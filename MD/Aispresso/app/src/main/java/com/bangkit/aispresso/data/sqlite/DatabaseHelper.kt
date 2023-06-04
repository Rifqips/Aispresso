package com.bangkit.aispresso.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bangkit.aispresso.data.sqlite.ClassifybaseRegsiter.ClassifyColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME,
    null, DATABASE_VERSION,
) {
    companion object {
        private const val DATABASE_NAME = "imageclassify"
        private const val DATABASE_VERSION = 5
        private const val SQL_CREATE_TABLE_CLASSIFY = "CREATE TABLE $TABLE_NAME" +
                " (${ClassifybaseRegsiter.ClassifyColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${ClassifybaseRegsiter.ClassifyColumns.CLASSIFIED} TEXT NOT NULL," +
                " ${ClassifybaseRegsiter.ClassifyColumns.CONSIDER} TEXT NOT NULL," +
                " ${ClassifybaseRegsiter.ClassifyColumns.IMAGE} BLOB NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL(SQL_CREATE_TABLE_CLASSIFY)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        } else{
            onCreate(db)
        }
    }
}