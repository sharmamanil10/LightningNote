package com.dev.nihitb06.lightningnote.databaseutils.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Notes")
data class Note (
        @PrimaryKey ( autoGenerate = true )
        val id: String,

        var title: String,
        var body: String,
        val dateCreated: Long,
        var dateModified: Long
)