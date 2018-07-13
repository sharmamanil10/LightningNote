package com.dev.nihitb06.lightningnote.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Environment
import android.widget.ImageButton
import android.widget.Toast
import com.dev.nihitb06.lightningnote.MainActivity
import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.LightningNoteDatabase
import com.dev.nihitb06.lightningnote.databaseutils.entities.Attachment
import com.dev.nihitb06.lightningnote.notes.AttachmentRecyclerAdapter
import com.dev.nihitb06.lightningnote.notes.operations.AddNoteFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AudioUtils {

    companion object {
        private lateinit var mediaRecorder: MediaRecorder

        fun recordAudio(context: Context, noteId: Long, adapter: AttachmentRecyclerAdapter?, isNoteBeingAdded: Boolean) {
            mediaRecorder = MediaRecorder()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            val audioFilePath = createAudioFile(context)?.path
            mediaRecorder.setOutputFile(audioFilePath)
            try {
                mediaRecorder.prepare()
            } catch (e: IOException) {
                Toast.makeText(context, "Something Went Wrong during Audio Recording", Toast.LENGTH_SHORT).show()
            }

            val recorderDialog = Dialog(context)
            recorderDialog.setContentView(R.layout.layout_record_audio_dialog)

            recorderDialog.findViewById<ImageButton>(R.id.btnStopRecording).setOnClickListener {
                mediaRecorder.stop()
                mediaRecorder.release()

                recorderDialog.dismiss()

                Thread {
                    val lightningNoteDatabase = LightningNoteDatabase.getDatabaseInstance(context)
                    if(isNoteBeingAdded) (context as MainActivity).saveNote(AddNoteFragment.returnNote(), false).let {
                        lightningNoteDatabase.attachmentDao().createAttachment(
                                Attachment(Uri.fromFile(File(audioFilePath)).toString(), noteId, Attachment.AUDIO)
                        )
                    }.let { (context as Activity).runOnUiThread { adapter?.notifyAttachments(true) } }
                }.start()
            }

            mediaRecorder.start()
            recorderDialog.show()
        }

        private fun createAudioFile(context: Context): File? {
            try {
                return File.createTempFile(
                        "Attachment_" + SimpleDateFormat.getDateTimeInstance().format(Date()),
                        ".3gpp",
                        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                )
            } catch (e: FileAlreadyExistsException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }
    }
}