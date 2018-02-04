package com.dev.nihitb06.lightningnote.notes

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.notes.addnotes.AddNoteFragment
import kotlinx.android.synthetic.main.fragment_notes.view.*

class NotesFragment : Fragment() {

    private lateinit var fragmentContext: Context
    private lateinit var sharedPreferences: SharedPreferences

    private var showOnlyStarred = false
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        if(sharedPreferences.getBoolean(isListLinear, false)) {
            layoutManager = LinearLayoutManager(activity)
        } else {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            (layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val itemView = inflater.inflate(R.layout.fragment_notes, container, false)
        val rvNotesList = itemView.rvNotesList

        rvNotesList.layoutManager = layoutManager
        rvNotesList.adapter = NotesRecyclerAdapter()

        rvNotesList.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                try {
                    (rvNotesList.layoutManager as StaggeredGridLayoutManager).invalidateSpanAssignments()
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }
            }
        })

        itemView.fabAddNotes.setOnClickListener {
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, AddNoteFragment()).commit()
        }

        return itemView
    }

    companion object {
        private const val isListLinear = "IsListLinear"

        fun newInstance(context: Context, showOnlyStarred: Boolean): NotesFragment {
            val fragment = NotesFragment()

            fragment.fragmentContext = context
            fragment.showOnlyStarred = showOnlyStarred

            return NotesFragment()
        }
    }
}
