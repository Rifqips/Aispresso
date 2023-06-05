package com.bangkit.aispresso.data.sqlite

import android.provider.BaseColumns

class ClassifybaseRegsiter {
    internal class ClassifyColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "classification_image"
            const val ID  = "id"
            const val CLASSIFIED = "classified"
            const val CONSIDER = "consider"
            const val IMAGE = "image"
        }
    }
}