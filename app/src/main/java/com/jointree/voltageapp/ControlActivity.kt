package com.jointree.voltageapp

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.SyncStateContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.util.*

class   ControlActivity : AppCompatActivity() {

    companion object {
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
    }

    lateinit var m_Adapater : BtAdapter

    //var btService : MyBluetoothService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        m_Adapater = intent.getParcelableExtra<BtAdapter>("Bluetooth_Adapter")!!

    }

    private fun sendCommend(input: String){

    }

    private fun disconnect(){

    }


    private inner class ConnectToDevice(device: BluetoothDevice): Thread(){

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE){
            device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
        }

        public override fun run() {
            m_Adapater.m_bluetoothAdapter?.cancelDiscovery()

            mmSocket?.use { socket ->

                socket.connect()

                //btService = MyBluetoothService()
            }
        }

        fun cancel(){
            try{
                mmSocket?.close()
            } catch (e: IOException){
                Log.e("connect: ", "Could not close the client socket", e)
            }
        }
    }

//    val mHandler = Handler(Looper.getMainLooper()){
//        when(it.what){
//            Constants.MESSAGE_STATE_CHANGE ->
//                when(it.arg1){
//                    MyBluetoothService.
//                }
//        }
//        true
//    }

}