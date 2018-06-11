package com.dev.nihitb06.lightningnote.notes.noteutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AttachmentUriManager (private val context: Context) {

    fun createIntent(index: Int) {
        var intent: Intent? = null
        var requestCode = 0
        when(index) {
            0 -> {
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                requestCode = REQUEST_TAKE_IMAGE
            }
            1 -> {
                intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                requestCode = REQUEST_TAKE_VIDEO
            }
            3 -> {
                intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                requestCode = REQUEST_SELECT_IMAGE
            }
            4 -> {
                intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                requestCode = REQUEST_SELECT_VIDEO
            }
            5 -> {
                intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                requestCode = REQUEST_SELECT_AUDIO
            }
            else -> {}
        }

        Log.d("attachment", "startActivityForResult")
        (context as Activity).startActivityForResult(intent, requestCode)
    }

    fun copyFileToStorage(fileDirectory: String, uri: Uri?) {
        val imageFile = context.getExternalFilesDir(fileDirectory)
        if(imageFile.isDirectory) {
            Log.d("StoreImage", "We are in Image Directory: "+imageFile)
            val fileExtension = getFileExtension(uri)

            val fileName = "TDSImage" + imageFile.listFiles().size + fileExtension

            try {
                Log.d("StoreImage", "We are in try ")

                getTempFile(uri, fileExtension)?.copyTo(File(imageFile, fileName), false, DEFAULT_BUFFER_SIZE)

                Log.d("StoreImage", "We are in try ")
            } catch (e: NoSuchFileException) {
                e.printStackTrace()
                Log.d("StoreImage", "We are in catch "+e.message)
            } catch (e: FileAlreadyExistsException) {
                e.printStackTrace()
                Log.d("StoreImage", "We are in try "+e.message)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("StoreImage", "We are in try "+e.message)
            }
        }
    }

    private fun getTempFile(uri: Uri?, extension: String): File? {
        val newFile = File.createTempFile("NewFile", extension)
        newFile.deleteOnExit()

        val inputStream = context.contentResolver.openInputStream(uri)

        if(inputStream != null) {
            try {
                val outputStream = FileOutputStream(newFile)

                inputStream.use { it.copyTo(outputStream) }

                inputStream.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return newFile
    }

    private fun getFileExtension(uri: Uri?): String {
        var fileExtension = ""

        val filePathColumn: Array<out String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)

        if(cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val filePath = cursor.getString(columnIndex)

            fileExtension = filePath.substring(filePath.lastIndexOf("."))
        }

        cursor.close()
        return fileExtension
    }

    companion object {
        const val REQUEST_TAKE_IMAGE = 101
        const val REQUEST_TAKE_VIDEO = 102
        const val REQUEST_SELECT_IMAGE = 201
        const val REQUEST_SELECT_VIDEO = 202
        const val REQUEST_SELECT_AUDIO = 203
    }
}