package com.jointree.voltageapp

import android.app.Activity
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.io.IOError
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    lateinit var select_device_refresh: Button
    lateinit var select_device_list: ListView

    lateinit var btAdapter: BtAdapter

    companion object{
        val EXTRA_ADDRESS: String = "Devices_address"
        val BT_ADAPTER: String = "Bluetooth_Adapter"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        select_device_refresh = findViewById(com.jointree.voltageapp.R.id.select_device_refresh)
        select_device_list = findViewById(R.id.select_device_list)

        btAdapter = BtAdapter()

        btAdapter.m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if(btAdapter.m_bluetoothAdapter == null){
            Toast.makeText(this, "This device not support bluetooth.", Toast.LENGTH_SHORT).show()
            return
        }
        if(!btAdapter.m_bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        select_device_refresh.setOnClickListener{ pairedDeviceList() }

    }

    private fun pairedDeviceList(){
        m_pairedDevices = btAdapter.m_bluetoothAdapter!!.bondedDevices

        val list: ArrayList<BluetoothDevice> = ArrayList()

        if(!m_pairedDevices.isEmpty()){
            for (device: BluetoothDevice in m_pairedDevices){
                list.add(device)
                Log.i("device", ""+device)
            }
        }else{
            Toast.makeText(this, "기기를 찾지 못했습니다.", Toast.LENGTH_SHORT).show()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device : BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            intent.putExtra(BT_ADAPTER, btAdapter)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode == Activity.RESULT_OK){
                if(btAdapter.m_bluetoothAdapter!!.isEnabled){
                    Toast.makeText(this, "Bluetooth has been enable", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "has been canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String? = intent?.action
            when(action){
                BluetoothDevice.ACTION_FOUND -> {
                    //val device: BluetoothDevice =

                }
            }
        }
    }
}