package me.melijn.dndfixer

import android.Manifest
import android.app.job.JobScheduler
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StatusFragment : Fragment() {
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_status, container, false)

        // buttons
        view.findViewById<Button>(R.id.button).setOnClickListener {
            view.findNavController().navigate(R.id.action_statusFragment_to_settingsFragment)
        }
        view.findViewById<Button>(R.id.button2).setOnClickListener {
            Util.scheduleJob(it.context)
        }
        view.findViewById<Button>(R.id.grant_permissions).setOnClickListener {
            val dndAccess: Boolean = ContextCompat.checkSelfPermission(it.context, Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS) == PackageManager.PERMISSION_GRANTED

            if (!dndAccess) {
                Toast.makeText(context, "Please grant dnd access", Toast.LENGTH_SHORT).show()
                resultLauncher.launch(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            }
        }

        // status updater
        val textBox = view.findViewById<TextView>(R.id.textView)
        val jobScheduler: JobScheduler = view.context.getSystemService(JobScheduler::class.java)
        CoroutineScope(Dispatchers.Default).launch {
            repeat(60) {
                withContext(Dispatchers.Main) {
                    textBox.text = jobScheduler.allPendingJobs.toString()
                }
                delay(1000)
            }
        }

        // this does nothing afaik, ACCESS_NOTIFICATION_POLICY will look like it's granted but actually not
        // use button3 to open the settings activity and grant manually :)
        val contract = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(view.context, "Sad", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(view.context, "Happiness", Toast.LENGTH_LONG).show()
            }
        }
        contract.launch(Manifest.permission.ACCESS_NOTIFICATION_POLICY)

        return view
    }
}