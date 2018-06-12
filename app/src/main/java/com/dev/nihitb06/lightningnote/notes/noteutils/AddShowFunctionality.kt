package com.dev.nihitb06.lightningnote.notes.noteutils

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Handler
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import com.dev.nihitb06.lightningnote.reminders.ReminderCreator
import com.dev.nihitb06.lightningnote.utils.PermissionManager
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton
import kotlinx.android.synthetic.main.fragment_add_note.view.*

class AddShowFunctionality (private val context: Context, private val itemView: View) {

    private val actions = arrayOf(
            "Click Photo",
            "Record Video",
            "Record Audio",
            "Add Photo ",
            "Add Video ",
            "Add Audio",
            "Geo Location"
    )

    private val icons = arrayOf(
            R.drawable.ic_photo_camera_black_24dp,
            R.drawable.ic_videocam_black_24dp,
            R.drawable.ic_keyboard_voice_black_24dp,
            R.drawable.ic_photo_black_24dp,
            R.drawable.ic_local_movies_black_24dp,
            R.drawable.ic_audiotrack_black_24dp,
            R.drawable.ic_place_black_24dp
    )

    private val colors = arrayOf(
            ContextCompat.getColor(context, R.color.colorDarkAccent),
            ContextCompat.getColor(context, R.color.colorAccent),
            ContextCompat.getColor(context, R.color.colorRedAccent),
            ContextCompat.getColor(context, R.color.colorTealAccent),
            ContextCompat.getColor(context, R.color.colorGreenAccent),
            ContextCompat.getColor(context, R.color.colorBlueAccent),
            ContextCompat.getColor(context, R.color.colorRedAccent)
    )

    private val attachmentUriManager = AttachmentUriManager(context)

    fun initializeAttachments() {
        Handler().post {
            for (i in 0..6) {
                itemView.attachmentButton.addBuilder(
                        TextOutsideCircleButton.Builder()
                                .normalImageRes(icons[i])
                                .normalText(actions[i])
                                .normalColor(colors[i])
                                .shadowEffect(true)
                                .rippleEffect(true)
                                .listener { index: Int ->
                                    PermissionManager.askForPermission(
                                            context,
                                            if (Build.VERSION.SDK_INT >= 16) {
                                                arrayOf(
                                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.CAMERA
                                                )
                                            } else {
                                                arrayOf(
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.CAMERA
                                                )
                                            }, object : PermissionManager.OnPermissionResultListener {
                                        override fun onGranted() {
                                            attachmentUriManager.createIntent(index)
                                        }

                                        override fun onDenied() {
                                            Toast.makeText(context, "Permission is Required", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    )
                                }
                )
            }

            itemView.attachmentButton.visibility = View.VISIBLE
        }
    }

    fun setNoteChangeListeners(thisNote: Note, oldNote: Note?) {
        (itemView.noteTitle as TextInputEditText).addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(((oldNote != null) && (s?.toString() != oldNote.title)) || oldNote == null)
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
                if(((oldNote != null) && (s?.toString() != oldNote.body)) || oldNote == null)
                    thisNote.body = s?.toString() ?: ""
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Do Nothing
            }
        })

        itemView.starred.setOnClickListener {
            thisNote.isStarred = !thisNote.isStarred

            setStarred(thisNote)
        }

        val reminderCreator = ReminderCreator(context, thisNote.id)
        itemView.btnAddReminder.setOnClickListener { Log.d("Reminder", "onClick: ")
            reminderCreator.createReminder() }
    }

    fun setStarred(thisNote: Note) {
        itemView.starred.setImageResource(
                if(thisNote.isStarred)
                    R.drawable.ic_star_black_24dp
                else
                    R.drawable.ic_star_border_black_24dp
        )
    }
}