package com.dev.nihitb06.lightningnote.utils

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.design.widget.Snackbar
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import kotlinx.android.synthetic.main.layout_note.view.*

class NoteLongClickListenerSingleSelection (
        private val context: Context,
        private val note: Note,
        private val thisView: View,
        private val visibleViews: ArrayList<View>,
        private val deleteNotes: (note: Note) -> Unit
) : View.OnLongClickListener {

    fun onLongClick(): Boolean {
        return onLongClick(thisView)
    }

    override fun onLongClick(v: View?): Boolean {
        val popupMenu = PopupMenu(context, thisView)
        popupMenu.inflate(R.menu.menu_note)
        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            when(item?.itemId) {
                R.id.noteShare -> { shareNote(note) }
                R.id.noteCopy -> { copyNote(note) }
                R.id.noteDelete -> { deleteNotes(note) }
            }

            return@setOnMenuItemClickListener false
        }

        popupMenu.setOnDismissListener {
            unBlur()
            setSelected(24f, 6f)
        }

        Log.d("Blur", visibleViews.size.toString())

        blur()
        setSelected(0f, 24f)

        popupMenu.show()

        return true
    }

    private fun blur() {
        Thread {
            visibleViews.filter { it != thisView }.map { view: View ->
                Log.d("Blur", view.toString())
                (context as Activity).runOnUiThread {
                    (view.frameLayout).foreground = BitmapDrawable(
                            context.resources,
                            ImageProcessingUtils.blurScreenshot(
                                    context,
                                    view.frameLayout
                            )
                    )
                }
            }
        }.start()
    }

    private fun unBlur() {
        Thread {
            visibleViews.filter { it != thisView }.map { view: View ->
                (context as Activity).runOnUiThread { (view.frameLayout).foreground = null }
            }
        }.start()
    }

    private fun setSelected(start: Float, end: Float) = ObjectAnimator.ofFloat(
            thisView.root as CardView,
            "cardElevation",
            start,
            end
    ).start()

    private fun shareNote(note: Note) {}
    private fun copyNote(note: Note) {
        val clipBoardText = (if(note.title != "") note.title else "") + (if(note.body != "") "\n\n" + note.body else "")

        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(
                note.title,
                clipBoardText
        )

        Snackbar.make(thisView, R.string.text_copied, Snackbar.LENGTH_SHORT).show()
    }
}