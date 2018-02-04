package com.dev.nihitb06.lightningnote.notes.addnotes

import android.os.Bundle
import android.os.Handler
import android.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dev.nihitb06.lightningnote.R
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton
import kotlinx.android.synthetic.main.fragment_add_note.view.*

class AddNoteFragment : Fragment() {

    private lateinit var itemView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actions = arrayOf(
                "Click Photo",
                "Record Video",
                "Record Audio",
                "Add Photo ",
                "Add Video ",
                "Add Audio",
                "Geo Location"
        )
        val icons = arrayOf(
                R.drawable.ic_photo_camera_black_24dp,
                R.drawable.ic_videocam_black_24dp,
                R.drawable.ic_keyboard_voice_black_24dp,
                R.drawable.ic_photo_black_24dp,
                R.drawable.ic_local_movies_black_24dp,
                R.drawable.ic_audiotrack_black_24dp,
                R.drawable.ic_place_black_24dp
        )
        val colors = arrayOf(
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorDarkAccent),
                ContextCompat.getColor(activity, R.color.colorRedAccent),
                ContextCompat.getColor(activity, R.color.colorTealAccent),
                ContextCompat.getColor(activity, R.color.colorGreenAccent),
                ContextCompat.getColor(activity, R.color.colorBlueAccent),
                ContextCompat.getColor(activity, R.color.colorRedAccent)
        )

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
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_add_note, container, false)

        itemView.attachmentButton.animate().scaleX(1f).scaleY(1f).start()

        return itemView
    }


    private fun setBuilders(builder: TextOutsideCircleButton.Builder) {
        if(::itemView.isInitialized) {
            itemView.attachmentButton.addBuilder(builder)
        }
    }
}
