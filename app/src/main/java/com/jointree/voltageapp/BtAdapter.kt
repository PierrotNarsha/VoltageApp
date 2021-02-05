package com.jointree.voltageapp

import android.bluetooth.BluetoothAdapter
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class BtAdapter(): Parcelable{

    var m_bluetoothAdapter: BluetoothAdapter? = null

    constructor(parcel:Parcel): this(){
        parcel.run {
            var m_bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        }
    }
}
