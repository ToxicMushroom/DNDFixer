package com.example.dndfixer

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context


object Util {
    // schedule the start of the service every 10 - 30 seconds
    fun scheduleJob(context: Context) {
        val serviceComponent = ComponentName(context, DNDFixService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency(3_000L) // wait at least
        builder.setOverrideDeadline(60_000L) // maximum delay

        val jobScheduler: JobScheduler = context.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())
    }
}