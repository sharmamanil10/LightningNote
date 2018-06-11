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

    @Delete
    fun deleteNote(note: Note): Int

    @Query("SELECT * FROM Notes WHERE id = :noteId")
    fun getNoteById(noteId: Long): Note

    /*@Query("SELECT id, title, body, dateModified FROM Notes")
    fun getNotesForList(): ArrayList<NoteListDetail>*/

    @Query("SELECT * FROM Notes")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM Notes WHERE isStarred = 1 AND isDeleted = 0")
    fun getStarredNotes(): List<Note>

    @Query("SELECT * FROM Notes WHERE isDeleted = 1")
    fun getDeletedNotes(): List<Note>

    @Query("SELECT * FROM Notes WHERE isDeleted = 0")
    fun getUnDeletedNotes(): List<Note>
}
