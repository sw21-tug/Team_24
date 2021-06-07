package at.tugraz.ist.guessingwords.ui.multiplayer

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.data.entity.Word
import at.tugraz.ist.guessingwords.data.service.Callback
import at.tugraz.ist.guessingwords.data.service.WordService
import at.tugraz.ist.guessingwords.networking.WordTransport
import at.tugraz.ist.guessingwords.ui.multiplayer.adapters.HostAdapter
import com.adroitandroid.near.connect.NearConnect
import com.adroitandroid.near.discovery.NearDiscovery
import com.adroitandroid.near.model.Host
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class JoinFragment : Fragment() {

    private lateinit var root: View

    private lateinit var nearDiscovery: NearDiscovery
    private lateinit var nearConnect: NearConnect

    private var transport: WordTransport? = null
    private var sentWords = false

    lateinit var joinWordService: WordService

    companion object {
        const val TAG = "JoinActivity"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_join, container, false)

        joinWordService = WordService(requireActivity())

        var userNameJoin = requireActivity().intent.getStringExtra("UserNameJoin")
        if (userNameJoin!!.isBlank()){
            userNameJoin = "UserJoin"+(1..5000).random()
        }
        root.findViewById<TextView>(R.id.text_username_join).text = userNameJoin.trim()
        Toast.makeText(activity, userNameJoin, Toast.LENGTH_LONG).show()

        val name = "GW-join"+(1..5000).random()
        val searchingFilter = "GW-host.*"
        val identifyingFilter = "GW-join.*"

        joinWordService.getAllWords(object : Callback<List<Word>> {
            override fun whenReady(data: List<Word>?) {
                if (data != null) {
                    transport = WordTransport(userNameJoin, data)
                    if (!sentWords && nearConnect.peers.isNotEmpty()) {
                        sendWords(nearConnect.peers)
                    }
                }
            }
        })
//
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
        nearDiscovery.makeDiscoverable(name, identifyingFilter)
        nearDiscovery.startDiscovery()

        return root
    }

        override fun onDestroy() {
        super.onDestroy()
        stopDiscovery()
    }

        private fun sendWords(host: Set<Host>) {
        sentWords = true
        stopDiscovery()
        val msg = Json.encodeToString(transport)
        host.forEach {
                h -> sendMessage(msg, h)
        }
        root.findViewById<TextView>(R.id.text_wordsSentMessage).setText(R.string.text_join_sent_message)
    }

    private fun sendMessage(msg: String, host: Host) {
        Log.d(TAG, "Sending message: ${msg}")
//        Toast.makeText(requireActivity(), "Sending message: ${msg}", Toast.LENGTH_LONG).show()
        nearConnect.send(msg.toByteArray(), host)
    }

    private val nearDiscoveryListener: NearDiscovery.Listener
        get() = object : NearDiscovery.Listener {
            override fun onPeersUpdate(host: Set<Host>) {
                Log.d(TAG, "Peer Update. Found ${host.size} hosts")
                val foundPeersMsg = "Found " + host.size + " hosts"
                root.findViewById<TextView>(R.id.text_found_peers_join).text = foundPeersMsg
                if (!sentWords && transport != null && host.isNotEmpty()) {
                    sendWords(host)
                }
            }

            override fun onDiscoveryTimeout() {
                Log.d(TAG, "No other participants found")
//                Toast.makeText(requireActivity(), "No other participants found", Toast.LENGTH_LONG).show()
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
//                Toast.makeText(requireActivity(), "Received text: ${String(bytes)}", Toast.LENGTH_LONG).show()
                Log.d(TAG, String(bytes))
            }

            override fun onSendComplete(jobId: Long) {}
            override fun onSendFailure(e: Throwable?, jobId: Long) {
                Toast.makeText(requireActivity(), "onSendFailure", Toast.LENGTH_LONG).show()
                Log.d(TAG, "onSendFailure")
            }
            override fun onStartListenFailure(e: Throwable?) {
                Toast.makeText(requireActivity(), "onStartListenFailure", Toast.LENGTH_LONG).show()
                Log.d(TAG, "onStartListenFailure")
            }
        }

    private fun stopDiscovery() {
        nearDiscovery.makeNonDiscoverable()
        nearDiscovery.stopDiscovery()
        Log.d(TAG, "Stopping discovery")
    }

}