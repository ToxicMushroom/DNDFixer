package me.melijn.dndfixer

import android.Manifest
import android.app.job.JobScheduler
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatusFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_status, container, false)
        view.findViewById<Button>(R.id.button).setOnClickListener {
            view.findNavController().navigate(R.id.action_statusFragment_to_settingsFragment)
        }

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

        view.findViewById<Button>(R.id.button2).setOnClickListener {
            Util.scheduleJob(it.context)
        }
        view.findViewById<Button>(R.id.button3).setOnClickListener {
            val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
            resultLauncher.launch(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
        }
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