package com.dev.nihitb06.lightningnote.databaseutils.extramodels

import android.arch.persistence.room.ColumnInfo

data class NoteListDetail (
        @ColumnInfo(name = "id")
        val id: String,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "body")
        val body: String,

        @ColumnInfo(name = "dateModified")
        val dateModified: Long
)