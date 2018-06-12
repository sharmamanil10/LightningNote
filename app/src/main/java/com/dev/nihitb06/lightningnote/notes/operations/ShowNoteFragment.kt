package com.dev.nihitb06.lightningnote.notes.operations


import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.LightningNoteDatabase
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import com.dev.nihitb06.lightningnote.notes.noteutils.AddShowFunctionality
import kotlinx.android.synthetic.main.fragment_add_note.view.*

class ShowNoteFragment : Fragment() {

    private var noteId: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val itemView = inflater.inflate(R.layout.fragment_add_note, container, false)

        itemView.noteTitle.setText(thisNote.title)
        itemView.noteBody.setText(thisNote.body)

        val addShowFunctionality = AddShowFunctionality(activity, itemView)
        addShowFunctionality.initializeAttachments()
        addShowFunctionality.setNoteChangeListeners(thisNote, oldNote)
        addShowFunctionality.setStarred(thisNote)

        return itemView
    }

    companion object {
        fun newInstance(context: Context, noteId: Long?): ShowNoteFragment {
            val fragment = ShowNoteFragment()
            fragment.noteId = noteId

            Thread {
                thisNote = LightningNoteDatabase.getDatabaseInstance(context).noteDao().getNoteById(noteId!!)
                oldNote = thisNote
            }.start()

            return fragment
        }

        private lateinit var thisNote: Note
        private lateinit var oldNote: Note

        fun returnNote() = thisNote
        fun isNoteChanged(): Boolean {
            return !(thisNote.title != oldNote.title || thisNote.body != oldNote.body || thisNote.isStarred != oldNote.isStarred)
        }
    }
}
