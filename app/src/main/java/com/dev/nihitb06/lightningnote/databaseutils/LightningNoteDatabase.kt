package com.dev.nihitb06.lightningnote.databaseutils

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.dev.nihitb06.lightningnote.databaseutils.dao.AttachmentDao
import com.dev.nihitb06.lightningnote.databaseutils.dao.NoteDao
import com.dev.nihitb06.lightningnote.databaseutils.dao.ReminderDao
import com.dev.nihitb06.lightningnote.databaseutils.entities.Attachment
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import com.dev.nihitb06.lightningnote.databaseutils.entities.Reminder

@Database(entities = [Note::class, Reminder::class, Attachment::class], version = 1)
abstract class LightningNoteDatabase : RoomDatabase () {

    abstract fun noteDao(): NoteDao
    abstract fun reminderDao(): ReminderDao
    abstract fun attachmentDao(): AttachmentDao

    companion object {
        private var databaseInstance: LightningNoteDatabase? = null

        @Synchronized
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