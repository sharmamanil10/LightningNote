package com.dev.nihitb06.lightningnote.notes.operations


import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.LightningNoteDatabase
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import kotlinx.android.synthetic.main.fragment_add_note.view.*

class ShowNoteFragment : Fragment() {

    private var noteId: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val itemView = inflater.inflate(R.layout.fragment_add_note, container, false)

        itemView.noteTitle.setText(thisNote.title)
        itemView.noteBody.setText(thisNote.body)

        setNoteTextChangeListeners(itemView)

        itemView.starred.setOnClickListener {
            ShowNoteFragment.thisNote.isStarred = !ShowNoteFragment.thisNote.isStarred

            itemView.starred.setImageResource(
                    if(ShowNoteFragment.thisNote.isStarred)
                        R.drawable.ic_star_black_24dp
                    else
                        R.drawable.ic_star_border_black_24dp
            )
        }

        return itemView
    }

    private fun setNoteTextChangeListeners(itemView: View) {
        (itemView.noteTitle as TextInputEditText).addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s?.toString() != oldNote.title)
                    ShowNoteFragment.thisNote.title = s?.toString() ?: ""
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Do Nothing
            }
        })

        (itemView.noteBody as TextInputEditText).addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s?.toString() != oldNote.body)
                    ShowNoteFragment.thisNote.body = s?.toString() ?: ""
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Do Nothing
            }
        })
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
