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

class NotesRecyclerAdapter (private val context: Context, private val onlyStarred: Boolean, private val onlyDeleted: Boolean)
    : RecyclerView.Adapter<NotesRecyclerAdapter.NoteViewHolder> () {

    private lateinit var notes: List<Note>
    private val visibleViews = ArrayList<View>()
    init {
        Thread {
            setNotes()
        }.start()
    }

    private fun setNotes() {
        notes = when {
            onlyStarred -> LightningNoteDatabase.getDatabaseInstance(context).noteDao().getStarredNotes()

            onlyDeleted -> LightningNoteDatabase.getDatabaseInstance(context).noteDao().getDeletedNotes()

            else -> LightningNoteDatabase.getDatabaseInstance(context).noteDao().getUnDeletedNotes()
        }

        (context as Activity).runOnUiThread { notifyDataSetChanged() }
    }

    inner class NoteViewHolder (private val thisView: View) : RecyclerView.ViewHolder (thisView) {

        init {
            visibleViews.add(thisView)
        }

        fun bindNote(note: Note, position: Int) {
            thisView.tvNoteTitle.text = note.title
            thisView.tvNoteBody.text = note.body

            thisView.contentArea.setOnClickListener {  }
            thisView.contentArea.setOnLongClickListener {
                NoteLongClickListenerSingleSelection(context, note, thisView, visibleViews, {
                    Thread {
                        if(onlyDeleted)
                            LightningNoteDatabase.getDatabaseInstance(context).noteDao().deleteNote(note)
                        else {
                            note.isDeleted = true
                            LightningNoteDatabase.getDatabaseInstance(context).noteDao().updateNote(note)
                        }

                        removeNote(note, position)
                    }.start()
                }).onLongClick()
            }

            if(!onlyDeleted)
                setSymbols(note, position)
        }

        private fun setSymbols(note: Note, position: Int) {
            initialize()
            var view = thisView.un_starred
            if(note.isStarred) {
                thisView.starred.visibility = View.VISIBLE
                view = thisView.starred
            } else
                thisView.un_starred.visibility = View.VISIBLE

            view.setOnClickListener {
                note.isStarred = !note.isStarred
                Thread({
                    LightningNoteDatabase.getDatabaseInstance(context).noteDao().updateNote(note)
                    (context as Activity).runOnUiThread {
                        if(onlyStarred) {
                            removeNote(note, position)
                        } else {
                            notifyItemChanged(position)
                        }
                    }
                }).start()
            }
        }

        private fun initialize() {
            thisView.starred.visibility = View.GONE
            thisView.un_starred.visibility = View.GONE
        }

        private fun removeNote(note: Note, position: Int) {
            visibleViews.remove(thisView)
            (notes as ArrayList).remove(note)
            (context as Activity).runOnUiThread { notifyItemRemoved(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = NoteViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.layout_note, parent, false))

    override fun onBindViewHolder(holder: NoteViewHolder?, position: Int) {
        holder?.bindNote(notes[position], position)
    }

    override fun getItemCount() = if(::notes.isInitialized) notes.size else 0
}