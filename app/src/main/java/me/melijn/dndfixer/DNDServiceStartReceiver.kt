package me.melijn.dndfixer

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import me.melijn.dndfixer.Util.scheduleJob


class DNDServiceStartReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent?) {
        scheduleJob(context)
    }
}