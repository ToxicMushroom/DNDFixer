package com.example.dndfixer

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import androidx.preference.PreferenceManager


@SuppressLint("SpecifyJobSchedulerIdRange")
class DNDFixService : JobService() {

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d("BGcheckService: ", "starting job");

        try {
            val nm =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            Log.d(
                "BGcheckService: ",
                "dndautotoggle: ${preferences.getBoolean("dndautotoggle", false)}"
            );
            if (preferences.getBoolean("dndautotoggle", false))
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS) // DND
            Util.scheduleJob(applicationContext)
        } catch (t: Throwable) {
            Log.d("BGcheckService: ", t.message!!);
        }

        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }
}