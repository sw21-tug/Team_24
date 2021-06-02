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

class HostActivity : AppCompatActivity() {

    private lateinit var nearDiscovery: NearDiscovery
    private lateinit var nearConnect: NearConnect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        customizeActionBar()

        val name = "GW-host"+(1..5000).random()
        val searchingFilter = "GW-join.*"
        val identifyingFilter = "GW-host.*"

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

        Log.d("HostActivity", "Starting discovery... as $name")
        Toast.makeText(this@HostActivity, "Starting discovery... as $name", Toast.LENGTH_LONG).show()
        nearDiscovery.makeDiscoverable(name, identifyingFilter)
        nearDiscovery.startDiscovery()
        nearConnect.startReceiving()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopNearServices()
    }

    private val nearDiscoveryListener: NearDiscovery.Listener
        get() = object : NearDiscovery.Listener {
            override fun onPeersUpdate(host: Set<Host>) {
                Log.d("HostActivity", "Peer Update. Found ${host.size} hosts")
                Toast.makeText(this@HostActivity, "Peer Update. Found ${host.size} hosts", Toast.LENGTH_LONG).show()
            }

            override fun onDiscoveryTimeout() {
                Log.d("HostActivity", "No other participants found")
                Toast.makeText(this@HostActivity, "No other participants found", Toast.LENGTH_LONG).show()
            }

            override fun onDiscoveryFailure(e: Throwable) {
                Log.d("HostActivity", "Something went wrong while searching for participants")
                Toast.makeText(this@HostActivity, "Something went wrong while searching for participants", Toast.LENGTH_LONG).show()
            }

            override fun onDiscoverableTimeout() {
                Log.d("HostActivity", "You're not discoverable anymore")
                Toast.makeText(this@HostActivity, "You're not discoverable anymore", Toast.LENGTH_LONG).show()
            }
        }

    private val nearConnectListener: NearConnect.Listener
        get() = object : NearConnect.Listener {
            override fun onReceive(bytes: ByteArray, sender: Host) {
                Toast.makeText(this@HostActivity, "Received text: ${String(bytes)}", Toast.LENGTH_LONG).show()
                Log.d("HostActivity", String(bytes))
            }

            override fun onSendComplete(jobId: Long) {}
            override fun onSendFailure(e: Throwable?, jobId: Long) {
                Toast.makeText(this@HostActivity, "onSendFailure", Toast.LENGTH_LONG).show()
                Log.d("HostActivity", "onSendFailure")
            }
            override fun onStartListenFailure(e: Throwable?) {
                Toast.makeText(this@HostActivity, "onStartListenFailure", Toast.LENGTH_LONG).show()
                Log.e("HostActivity", "onStartListenFailure")
                Log.e("HostActivity", e.toString())
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