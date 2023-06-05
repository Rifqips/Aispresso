package com.bangkit.aispresso.data.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.bangkit.aispresso.data.sqlite.ClassifybaseRegsiter.ClassifyColumns.Companion.ID
import com.bangkit.aispresso.data.sqlite.ClassifybaseRegsiter.ClassifyColumns.Companion.TABLE_NAME

class ClassifyHelper(context: Context) {
    companion object {
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: ClassifyHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): ClassifyHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ClassifyHelper(context)
            }
    }

    init {
        databaseHelper= DatabaseHelper(context)
    }
    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }


    fun queryAll(): Cursor {
        return database.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$ID ASC"
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(TABLE_NAME, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(TABLE_NAME, values, "$ID = ?", arrayOf(id))
    }


    fun deleteById(id: String): Int {
        return database.delete(TABLE_NAME, "$ID = '$id'", null)
    }
}