package com.dev.nihitb06.lightningnote.notes.operations

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import com.dev.nihitb06.lightningnote.notes.noteutils.AddShowFunctionality

class AddNoteFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thisNote = Note("", "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val itemView = inflater.inflate(R.layout.fragment_add_note, container, false)

        val addShowFunctionality = AddShowFunctionality(activity, itemView)
        addShowFunctionality.initializeAttachments()
        addShowFunctionality.setNoteChangeListeners(thisNote, null)
        addShowFunctionality.setStarred(thisNote)

        return itemView
    }

    companion object {
        private var thisNote = Note("", "")

        fun returnNote() = thisNote
    }
}
