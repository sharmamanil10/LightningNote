package com.dev.nihitb06.lightningnote.notes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note

class NotesRecyclerAdapter : RecyclerView.Adapter<NotesRecyclerAdapter.NoteViewHolder> () {

    inner class NoteViewHolder (private val thisView: View) : RecyclerView.ViewHolder (thisView) {

        fun bindNote(note: Note) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = NoteViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.layout_note, parent, false))

    override fun onBindViewHolder(holder: NoteViewHolder?, position: Int) {
        //holder?.bindNote()
    }

    override fun getItemCount() = 0
}