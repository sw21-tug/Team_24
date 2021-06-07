package at.tugraz.ist.guessingwords.ui.multiplayer

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.StartGameActivity
import at.tugraz.ist.guessingwords.data.service.Callback
import at.tugraz.ist.guessingwords.data.service.WordService
import at.tugraz.ist.guessingwords.networking.WordTransport
import at.tugraz.ist.guessingwords.ui.multiplayer.adapters.HostAdapter
import com.adroitandroid.near.connect.NearConnect
import com.adroitandroid.near.discovery.NearDiscovery
import com.adroitandroid.near.model.Host
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class HostFragment : Fragment() {

    private lateinit var hostViewModel: HostViewModel
    private lateinit var root: View

    private lateinit var nearDiscovery: NearDiscovery
    private lateinit var nearConnect: NearConnect

    private lateinit var hostWordService: WordService

    private var lstJoinedUser = mutableListOf<String>()

    companion object {
        const val TAG = "HostActivity"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val hostFactory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        hostViewModel = ViewModelProvider(this, hostFactory).get(HostViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_host, container, false)

        var userNameHost = requireActivity().intent.getStringExtra("UserNameHost")
        if (userNameHost!!.isBlank()){
            userNameHost = "UserHost"+(1..5000).random()
        }
        Toast.makeText(activity, userNameHost, Toast.LENGTH_LONG).show()

        val name = "GW-host"+(1..5000).random()
        val searchingFilter = "GW-join.*"
        val identifyingFilter = "GW-host.*"

        hostWordService = WordService(requireActivity())
        hostWordService.createNewMultiplayerWordPool(object : Callback<List<Long>> {
            override fun whenReady(data: List<Long>?) {
                // TODO: add own name to list of names ------------------------ prob done
                lstJoinedUser.add(userNameHost)
                updateView(lstJoinedUser)
            }
        })

        nearDiscovery = NearDiscovery.Builder()
            .setContext(requireActivity())
            .setDiscoverableTimeoutMillis(60_000)
            .setDiscoveryTimeoutMillis(60_000)
            .setDiscoverablePingIntervalMillis(1000)
            .setDiscoveryListener(nearDiscoveryListener, Looper.getMainLooper())
            .setFilter(Regex(searchingFilter))
            .build()

        nearConnect = NearConnect.Builder()
            .fromDiscovery(nearDiscovery)
            .setContext(requireActivity())
            .setListener(nearConnectListener, Looper.getMainLooper())
            .build()

        Log.d(TAG, "Starting discovery... as $name")
        Toast.makeText(requireActivity(), "Starting discovery... as $name", Toast.LENGTH_LONG).show()
        nearDiscovery.makeDiscoverable(name, identifyingFilter)
        nearDiscovery.startDiscovery()
        nearConnect.startReceiving()


        var btn_ready = root.findViewById<Button>(R.id.btn_ready)

        btn_ready.setOnClickListener {
            val intent = Intent(context, StartGameActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        stopNearServices()
        // TODO: IMPORTANT!
        // TODO: add removeMultiplayerWordPool once returning to home screen
        // TODO: from game prototype AND next round screen AND on back from host
        // TODO: NOT on ready because we need the word pool in game prototype
        // wordService.removeMultiplayerWordPool()
    }

    private val nearDiscoveryListener: NearDiscovery.Listener
        get() = object : NearDiscovery.Listener {
            override fun onPeersUpdate(host: Set<Host>) {
                Log.d(TAG, "Peer Update. Found ${host.size} hosts")
                Toast.makeText(requireActivity(), "Peer Update. Found ${host.size} hosts", Toast.LENGTH_LONG).show()
            }

            override fun onDiscoveryTimeout() {
                Log.d(TAG, "No other participants found")
                Toast.makeText(requireActivity(), "No other participants found", Toast.LENGTH_LONG).show()
            }

            override fun onDiscoveryFailure(e: Throwable) {
                Log.d(TAG, "Something went wrong while searching for participants")
                Toast.makeText(requireActivity(), "Something went wrong while searching for participants", Toast.LENGTH_LONG).show()
            }

            override fun onDiscoverableTimeout() {
                Log.d(TAG, "You're not discoverable anymore")
                Toast.makeText(requireActivity(), "You're not discoverable anymore", Toast.LENGTH_LONG).show()
            }
        }

    private val nearConnectListener: NearConnect.Listener
        get() = object : NearConnect.Listener {
            override fun onReceive(bytes: ByteArray, sender: Host) {
                val msg = String(bytes)
                Toast.makeText(requireActivity(), "Received text: $msg", Toast.LENGTH_LONG).show()
                Log.d(TAG, msg)
                val transport: WordTransport = Json.decodeFromString(msg)
                hostWordService.mergeIntoDatabase(transport.words, object : Callback<List<Long>> {
                    override fun whenReady(data: List<Long>?) {
                        // TODO: add transport.name to list (to see who sent their words) - done -----------------------------------------------
                        lstJoinedUser.add(transport.name)
                        updateView(lstJoinedUser)
                    }
                })
            }

            override fun onSendComplete(jobId: Long) {}
            override fun onSendFailure(e: Throwable?, jobId: Long) {
                Toast.makeText(requireActivity(), "onSendFailure", Toast.LENGTH_LONG).show()
                Log.d(TAG, "onSendFailure")
            }
            override fun onStartListenFailure(e: Throwable?) {
                Toast.makeText(requireActivity(), "onStartListenFailure", Toast.LENGTH_LONG).show()
                Log.e(TAG, "onStartListenFailure")
                Log.e(TAG, e.toString())
            }
        }

    private fun stopDiscovery() {
        nearDiscovery.makeNonDiscoverable()
        nearDiscovery.stopDiscovery()
    }

    private fun stopNearServices() {
        nearConnect.stopReceiving(true)
        stopDiscovery()
    }

    fun updateView (joinedUser: MutableList<String>) {
        requireActivity().runOnUiThread {
            displayJoinedUserList(joinedUser)
        }
    }

    private fun displayJoinedUserList(joinedUser: MutableList<String>) {
        val lv_joined_user = root.findViewById<ListView>(R.id.lst_joined_user)
        lv_joined_user.adapter = HostAdapter(requireContext(), joinedUser)
    }
}