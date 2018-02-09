package com.dev.nihitb06.lightningnote.notes

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*

import com.dev.nihitb06.lightningnote.R
import kotlinx.android.synthetic.main.fragment_notes.view.*

class NotesFragment : Fragment() {

    private lateinit var fragmentContext: Context
    private lateinit var sharedPreferences: SharedPreferences

    private var showOnlyStarred = false
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    private lateinit var rvNotesList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        linearLayoutManager = LinearLayoutManager(activity)

        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val itemView = inflater.inflate(R.layout.fragment_notes, container, false)
        rvNotesList = itemView.rvNotesList

        rvNotesList.layoutManager = if(sharedPreferences.getBoolean(isListLinear, false))
             linearLayoutManager
        else
            staggeredGridLayoutManager

        rvNotesList.adapter = NotesRecyclerAdapter(activity)

        setScroll()

        return itemView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_list_style, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        if(sharedPreferences.getBoolean(isListLinear, false)) {
            menu?.findItem(R.id.list_style)?.isVisible = false
            menu?.findItem(R.id.grid_style)?.isVisible = true
        } else {
            menu?.findItem(R.id.list_style)?.isVisible = true
            menu?.findItem(R.id.grid_style)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.list_style -> rvNotesList.layoutManager = linearLayoutManager
            R.id.grid_style -> rvNotesList.layoutManager = staggeredGridLayoutManager
        }

        setScroll()
        sharedPreferences.edit().putBoolean(isListLinear, item?.itemId == R.id.list_style).apply()

        return super.onOptionsItemSelected(item)
    }

    private fun setScroll() {
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
