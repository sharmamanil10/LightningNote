package com.dev.nihitb06.lightningnote.databaseutils.dao

import android.arch.persistence.room.*
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import com.dev.nihitb06.lightningnote.databaseutils.extramodels.NoteListDetail

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNote(note: Note): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNote(note: Note): Int

    /*fun upsertNote(note: Note) {
        insertNote(note)
        updateNote(note)
    }*/

    @Delete
    fun deleteNote(note: Note): Int

    @Query("SELECT * FROM Notes WHERE id = :noteId")
    fun getNoteById(noteId: String): Note

    /*@Query("SELECT id, title, body, dateModified FROM Notes")
    fun getNotesForList(): ArrayList<NoteListDetail>*/

    @Query("SELECT * FROM Notes")
    fun getAllNotes(): List<Note>
}
