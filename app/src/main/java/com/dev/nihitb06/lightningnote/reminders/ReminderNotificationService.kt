package com.dev.nihitb06.lightningnote.reminders

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.RemoteInput
import android.support.v4.content.ContextCompat
import android.util.Log
import com.dev.nihitb06.lightningnote.MainActivity
import com.dev.nihitb06.lightningnote.R
import com.dev.nihitb06.lightningnote.databaseutils.LightningNoteDatabase
import com.dev.nihitb06.lightningnote.databaseutils.entities.Note
import com.dev.nihitb06.lightningnote.reminders.ReminderCreator.Companion.MESSAGE
import com.dev.nihitb06.lightningnote.reminders.ReminderCreator.Companion.NOTE_ID

class ReminderNotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            var thisNote: Note? = null

            try {
                thisNote = LightningNoteDatabase.getDatabaseInstance(this).noteDao().getNoteById(intent!!.getLongExtra(NOTE_ID, -1))
            } catch (e: Exception) {
                Log.e("Notification", "Message: "+e.message)
            }

            thisNote?.let {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notificationBuilder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(
                            NotificationChannel(CHANNEL_ID, NAME, NotificationManager.IMPORTANCE_DEFAULT)
                    )
                    NotificationCompat.Builder(this, CHANNEL_ID)
                } else {
                    NotificationCompat.Builder(this)
                }

                val color = ContextCompat.getColor(this, getColorPrimary(PreferenceManager
                        .getDefaultSharedPreferences(this)
                        .getString(getString(R.string.key_theme), getString(R.string.theme_default))))

                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_foreground)
                        .setContentTitle(thisNote.title)
                        .setContentText(intent?.getStringExtra(MESSAGE))
                        .setContentIntent(PendingIntent.getActivity(
                                this,
                                REQUEST_CODE,
                                Intent(this, MainActivity::class.java),
                                PendingIntent.FLAG_ONE_SHOT
                        ))
                        .setAutoCancel(true)
                if(thisNote.body != "")
                    notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(thisNote.body))

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    notificationBuilder.addAction(NotificationCompat.Action.Builder(
                            R.drawable.ic_note_black_24dp,
                            ACTION_ONE,
                            PendingIntent.getService(
                                    this,
                                    REQUEST_CODE_DISMISS+(count-300),
                                    Intent(this, ManageNotificationActionService::class.java)
                                            .putExtra(ACTION, NOTIFICATION_CANCEL)
                                            .putExtra(NOTIFICATION_ID, count),
                                    PendingIntent.FLAG_CANCEL_CURRENT
                            )
                    ).build()).setColor(color).setGroup(GROUP_KEY)

                    if(count > 300) {
                        Thread {
                            val style = NotificationCompat.InboxStyle()
                                    .setBigContentTitle(getString(R.string.app_name))
                                    .setSummaryText(GROUP_SUMMARY_TEXT)
                            for(index in notificationTexts.indices) {
                                style.addLine(notificationTexts[index])
                            }
                            val groupNotification = NotificationCompat.Builder(this, CHANNEL_ID)
                                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                                    .setContentTitle(getString(R.string.app_name))
                                    .setContentText(GROUP_SUMMARY_TEXT)
                                    .setStyle(style)
                                    .setColor(color)
                                    .setGroup(GROUP_KEY)
                                    .setGroupSummary(true)
                                    .setAutoCancel(true)
                                    .build()

                            notificationManager.notify(GROUP_SUMMARY_ID, groupNotification)
                        }.start()
                    }
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    notificationBuilder.addAction(
                            NotificationCompat.Action.Builder(
                                    R.drawable.ic_note_black_24dp,
                                    ACTION_TWO,
                                    PendingIntent.getService(
                                            this,
                                            REQUEST_CODE_UPDATE+(count-300)+1,
                                            Intent(this, ManageNotificationActionService::class.java)
                                                    .putExtra(ACTION, NOTIFICATION_NOTE_UPDATE)
                                                    .putExtra(NOTIFICATION_ID, count)
                                                    .putExtra(NOTE_ID, thisNote.id)
                                                    .putExtra("Color", color),
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    )
                            ).addRemoteInput(
                                    RemoteInput.Builder(UPDATE_NOTE_KEY)
                                            .setLabel("Append to Note Body")
                                            .build()
                            ).build()
                    )
                }

                notificationManager.notify(count++, notificationBuilder.build())
            }
        }.start()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun getColorPrimary(currentTheme: String): Int {
        var color = R.color.colorPrimary

        when (currentTheme) {
            getString(R.string.theme_dark) -> color = R.color.colorDarkPrimary
            getString(R.string.theme_red) -> color = R.color.colorRedPrimary
            getString(R.string.theme_teal) -> color = R.color.colorTealPrimary
            getString(R.string.theme_green) -> color = R.color.colorGreenPrimary
            getString(R.string.theme_blue) -> color = R.color.colorBluePrimary
            getString(R.string.theme_purple) -> color = R.color.colorPurplePrimary
        }

        return color
    }

    companion object {
        const val CHANNEL_ID = "NotificationLightningNote"
        private const val NAME = "LightningNoteNotifications"
        private const val ACTION_ONE = "Dismiss"
        private const val ACTION_TWO = "Update Note"
        private const val REQUEST_CODE = 201
        private const val REQUEST_CODE_DISMISS = 202
        private const val REQUEST_CODE_UPDATE = 203

        private const val GROUP_KEY = "LightningNotesGroup"
        private const val GROUP_SUMMARY_ID = 0
        private const val GROUP_SUMMARY_TEXT = "You have the following reminders"
        var count = 300

        const val NOTIFICATION_ID = "NotificationId"
        const val ACTION = "Action"
        const val NOTIFICATION_CANCEL = "Cancel Notification"
        const val NOTIFICATION_NOTE_UPDATE = "Update Note"
        private val notificationTexts = ArrayList<String>()

        const val UPDATE_NOTE_KEY = "Update Note Body"
    }
}
