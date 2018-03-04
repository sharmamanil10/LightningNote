package com.dev.nihitb06.lightningnote.notes.addnotes

import android.os.Bundle
import android.os.Handler
import android.app.Fragment
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import com.dev.nihitb06.lightningnote.utils.AnimationUtils
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton
import kotlinx.android.synthetic.main.fragment_add_note.view.*

class AddNoteFragment : Fragment() {

    private lateinit var itemView: View

    private lateinit var actions: Array<String>
    private lateinit var icons: Array<Int>
    private lateinit var colors: Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actions = arrayOf(
                "Click Photo",
                "Record Video",
                "Record Audio",
                "Add Photo ",
                "Add Video ",
                "Add Audio",
                "Geo Location"
        )

        icons = arrayOf(
                R.drawable.ic_photo_camera_black_24dp,
                R.drawable.ic_videocam_black_24dp,
                R.drawable.ic_keyboard_voice_black_24dp,
                R.drawable.ic_photo_black_24dp,
                R.drawable.ic_local_movies_black_24dp,
                R.drawable.ic_audiotrack_black_24dp,
                R.drawable.ic_place_black_24dp
        )

        colors = arrayOf(
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorDarkAccent),
                ContextCompat.getColor(activity, R.color.colorRedAccent),
                ContextCompat.getColor(activity, R.color.colorTealAccent),
                ContextCompat.getColor(activity, R.color.colorGreenAccent),
                ContextCompat.getColor(activity, R.color.colorBlueAccent),
                ContextCompat.getColor(activity, R.color.colorRedAccent)
        )

        thisNote = Note("", "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_add_note, container, false)

        Handler().post{
            for(index in 0..6) {
                setBuilders(
                        TextOutsideCircleButton.Builder()
                                .normalImageRes(icons[index])
                                .normalText(actions[index])
                                .normalColor(colors[index])
                                .shadowEffect(true)
                                .rippleEffect(true)
                )
            }

            itemView.attachmentButton.visibility = View.VISIBLE
        }

        AnimationUtils.scaleAnimate(itemView.attachmentButton, 1f)

        setNoteTextChangeListeners(itemView)

        itemView.starred.setOnClickListener {
            thisNote.isStarred = !thisNote.isStarred

            itemView.starred.setImageResource(
                    if(thisNote.isStarred)
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
                thisNote.title = s?.toString() ?: ""
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
                thisNote.body = s?.toString() ?: ""
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Do Nothing
            }
        })
    }

    private fun setBuilders(builder: TextOutsideCircleButton.Builder) {
        if(::itemView.isInitialized) {
            itemView.attachmentButton.addBuilder(builder)
        }
    }

    companion object {
        private var thisNote = Note("", "")

        fun returnNote() = thisNote
    }
}
