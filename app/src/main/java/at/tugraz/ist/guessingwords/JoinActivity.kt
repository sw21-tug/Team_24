package at.tugraz.ist.guessingwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.adroitandroid.near.connect.NearConnect
import com.adroitandroid.near.discovery.NearDiscovery
import com.adroitandroid.near.model.Host

class JoinActivity : AppCompatActivity() {

    private lateinit var nearDiscovery: NearDiscovery
    private lateinit var nearConnect: NearConnect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        customizeActionBar()

        val name = "GW-join"+(1..5000).random()
        val searchingFilter = "GW-host.*"
        val identifyingFilter = "GW-join.*"

        nearDiscovery = NearDiscovery.Builder()
                .setContext(this)
                .setDiscoverableTimeoutMillis(60000)
                .setDiscoveryTimeoutMillis(60000)
                .setDiscoverablePingIntervalMillis(1000)
                .setDiscoveryListener(nearDiscoveryListener, Looper.getMainLooper())
                .setFilter(Regex(searchingFilter))
                .build()

        nearConnect = NearConnect.Builder()
                .fromDiscovery(nearDiscovery)
                .setContext(this)
                .setListener(nearConnectListener, Looper.getMainLooper())
                .build()


        Log.d("JoinActivity", "Starting discovery... as $name")
        Toast.makeText(this@JoinActivity, "Starting discovery... as $name", Toast.LENGTH_LONG).show()
        nearDiscovery.makeDiscoverable(name, identifyingFilter)
        nearDiscovery.startDiscovery()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopDiscovery()
    }

    private fun sendMessage(msg: String, host: Host) {
        Log.d("JoinActivity", "Sending message: ${msg}")
        Toast.makeText(this@JoinActivity, "Sending message: ${msg}", Toast.LENGTH_LONG).show()
        nearConnect.send(msg.toByteArray(), host)
    }

    private val nearDiscoveryListener: NearDiscovery.Listener
        get() = object : NearDiscovery.Listener {
            override fun onPeersUpdate(host: Set<Host>) {
                Log.d("JoinActivity", "Peer Update. Found ${host.size} hosts")
                Toast.makeText(this@JoinActivity, "Peer Update. Found ${host.size} hosts", Toast.LENGTH_LONG).show()
                stopDiscovery()
                host.forEach {
                    h -> sendMessage("hallo"+(1..5000).random(), h)
                }
            }

            override fun onDiscoveryTimeout() {
                Log.d("JoinActivity", "No other participants found")
                Toast.makeText(this@JoinActivity, "No other participants found", Toast.LENGTH_LONG).show()
            }

            override fun onDiscoveryFailure(e: Throwable) {
                Log.d("JoinActivity", "Something went wrong while searching for participants")
                Toast.makeText(this@JoinActivity, "Something went wrong while searching for participants", Toast.LENGTH_LONG).show()
            }

            override fun onDiscoverableTimeout() {
                Log.d("JoinActivity", "You're not discoverable anymore")
                Toast.makeText(this@JoinActivity, "You're not discoverable anymore", Toast.LENGTH_LONG).show()
            }
        }

    private val nearConnectListener: NearConnect.Listener
        get() = object : NearConnect.Listener {
            override fun onReceive(bytes: ByteArray, sender: Host) {
                Toast.makeText(this@JoinActivity, "Received text: ${String(bytes)}", Toast.LENGTH_LONG).show()
                Log.d("RECEIVED", String(bytes))
            }

            override fun onSendComplete(jobId: Long) {}
            override fun onSendFailure(e: Throwable?, jobId: Long) {
                Toast.makeText(this@JoinActivity, "onSendFailure", Toast.LENGTH_LONG).show()
                Log.d("JoinActivity", "onSendFailure")
            }
            override fun onStartListenFailure(e: Throwable?) {
                Toast.makeText(this@JoinActivity, "onStartListenFailure", Toast.LENGTH_LONG).show()
                Log.d("JoinActivity", "onStartListenFailure")
            }
        }

    private fun stopDiscovery() {
        nearDiscovery.makeNonDiscoverable()
        nearDiscovery.stopDiscovery()
        Log.d("JoinActivity", "Stopping discovery")
    }


    private fun customizeActionBar() {
        supportActionBar?.title = getString(R.string.gameplay)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}