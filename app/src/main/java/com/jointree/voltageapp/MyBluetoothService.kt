package com.jointree.voltageapp

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class MyBluetoothService(private val handler: Handler){

    companion object {
        private const val TAG = "MY_APP_DEBUG_TAG"
        const val MESSAGE_WRITE: Int = 1
        const val MESSAGE_TOAST: Int = 2
        const val MESSAGE_READ: Int = 0
    }

    public fun write(mmSocket: BluetoothSocket, bytes: ByteArray){
        lateinit var r : ConnectedThread

        synchronized(this){
            if (!mmSocket.isConnected) return

            r = ConnectedThread(mmSocket)
        }

        r.write(bytes)
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket): Thread(){
        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024)

        override fun run() {
            var numBytes: Int

            while (true){
                numBytes = try {
                    mmInStream.read(mmBuffer)
                }catch (e: IOException){
                    Log.d(TAG,"read buffer data: ", e)
                    break
                }

                val readMsg = handler.obtainMessage(
                    Companion.MESSAGE_READ, numBytes, -1,
                    mmBuffer)
                readMsg.sendToTarget()
            }
        }

        fun write(bytes: ByteArray){
            try{
                mmOutStream.write(bytes)
            }catch (e: IOException){
                Log.e(TAG, "데이터 전송 실패", e)

                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }

                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE, -1, -1, mmBuffer)
            writtenMsg.sendToTarget()
        }

        fun cancel(){
            try {
                mmSocket.close()
            }catch (e: IOException){
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }


}