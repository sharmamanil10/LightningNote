package com.dev.nihitb06.lightningnote.databaseutils

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.dev.nihitb06.lightningnote.databaseutils.dao.NoteDao
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note

@Database(entities = [Note::class], version = 1)
abstract class LightningNoteDatabase : RoomDatabase () {

    abstract fun noteDao(): NoteDao

    companion object {
        private var databaseInstance: LightningNoteDatabase? = null

        fun getDatabaseInstance(context: Context): LightningNoteDatabase {
            if (databaseInstance == null) {
                databaseInstance = Room.databaseBuilder(
                        context.applicationContext,
                        LightningNoteDatabase::class.java,
                        "LightningNoteDatabase"
                ).build()
            }

            return databaseInstance!!
        }
    }
}