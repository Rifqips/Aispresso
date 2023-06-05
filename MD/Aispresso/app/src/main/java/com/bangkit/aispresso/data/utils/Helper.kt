package com.bangkit.aispresso.data.utils

import android.database.Cursor
import com.bangkit.aispresso.data.model.history.coffe.ClassifyCoffeModel
import com.bangkit.aispresso.data.sqlite.ClassifybaseRegsiter

object Helper {

    const val EXTRA_REGISTRATION = "extra_registration"
    const val EXTRA_POSITION = "extra_position"
    const val REQUEST_ADD = 100
    const val RESULT_ADD = 101
    const val REQUEST_UPDATE = 200
    const val RESULT_UPDATE = 201
    const val RESULT_DELETE = 301
    const val ALERT_DIALOG_CLOSE = 10
    const val ALERT_DIALOG_DELETE = 20

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<ClassifyCoffeModel> {
        val classifyList = ArrayList<ClassifyCoffeModel>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(ClassifybaseRegsiter.ClassifyColumns.ID))
                val classified = getString(getColumnIndexOrThrow(ClassifybaseRegsiter.ClassifyColumns.CLASSIFIED))
                val consider = getString(getColumnIndexOrThrow(ClassifybaseRegsiter.ClassifyColumns.CONSIDER))
                val image = getBlob(getColumnIndexOrThrow(ClassifybaseRegsiter.ClassifyColumns.IMAGE))
                classifyList.add(ClassifyCoffeModel(id, classified, consider, image))
            }
        }
        return classifyList
    }
}