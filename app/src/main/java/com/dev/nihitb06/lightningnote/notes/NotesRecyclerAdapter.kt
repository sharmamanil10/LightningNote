package com.dev.nihitb06.lightningnote.notes

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.LightningNoteDatabase
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import com.dev.nihitb06.lightningnote.utils.NoteLongClickListenerSingleSelection
import kotlinx.android.synthetic.main.layout_note.view.*

class NotesRecyclerAdapter (private val context: Context) : RecyclerView.Adapter<NotesRecyclerAdapter.NoteViewHolder> () {

    private lateinit var notes: List<Note>
    private val visibleViews = ArrayList<View>()
    init {
        Thread {
            setNotes()
        }.start()
    }

    private fun setNotes() {
        notes = LightningNoteDatabase.getDatabaseInstance(context).noteDao().getAllNotes()
        (context as Activity).runOnUiThread { notifyDataSetChanged() }
    }

    inner class NoteViewHolder (private val thisView: View) : RecyclerView.ViewHolder (thisView) {

        init {
            visibleViews.add(thisView)
        }

        fun bindNote(note: Note) {
            thisView.tvNoteTitle.text = note.title
            thisView.tvNoteBody.text = note.body

            thisView.contentArea.setOnClickListener {  }
            thisView.contentArea.setOnLongClickListener {
                NoteLongClickListenerSingleSelection(context, note, thisView, visibleViews, {
                    Thread {
                        LightningNoteDatabase.getDatabaseInstance(context).noteDao().deleteNote(note)
                        visibleViews.remove(thisView)
                        setNotes()
                    }.start()
                }).onLongClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = NoteViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.layout_note, parent, false))

    override fun onBindViewHolder(holder: NoteViewHolder?, position: Int) {
        holder?.bindNote(notes[position])
    }

    override fun getItemCount() = if(::notes.isInitialized) notes.size else 0
}