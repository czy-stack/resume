package com.example.kotlin.activity.test

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.kotlin.constants.SampleGattAttributes
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/6
 */

const val LIST_NAME = "name"
const val LIST_UUID = "uuid"
class DeviceControlActivity : Activity() {

    lateinit var bluetoothGatt: BluetoothGatt
    lateinit var characteristic: BluetoothGattCharacteristic
    var enabled: Boolean = true
    private lateinit var mGattCharacteristics:MutableList<BluetoothGattCharacteristic>


    // Demonstrates how to iterate through the supported GATT
    // Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the
    // ExpandableListView on the UI.
    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null) return
        var uuid: String?
        val unknownServiceString: String = "unknown_service"
        val unknownCharaString: String = "unknown_characteristic"
        val gattServiceData: MutableList<HashMap<String, String>> = mutableListOf()
        val gattCharacteristicData: MutableList<ArrayList<HashMap<String, String>>> =
            mutableListOf()
        mGattCharacteristics = mutableListOf()

        // Loops through available GATT Services.
        gattServices.forEach { gattService ->
            val currentServiceData = HashMap<String, String>()
            uuid = gattService.uuid.toString()
            currentServiceData[LIST_NAME] = SampleGattAttributes.lookup(uuid, unknownServiceString)
            currentServiceData[LIST_UUID] = uuid!!
            gattServiceData += currentServiceData

            val gattCharacteristicGroupData: ArrayList<HashMap<String, String>> = arrayListOf()
            val gattCharacteristics = gattService.characteristics
            val charas: MutableList<BluetoothGattCharacteristic> = mutableListOf()

            // Loops through available Characteristics.
            gattCharacteristics.forEach { gattCharacteristic ->
                charas += gattCharacteristic
                val currentCharaData: HashMap<String, String> = hashMapOf()
                uuid = gattCharacteristic.uuid.toString()
                currentCharaData[LIST_NAME] = SampleGattAttributes.lookup(uuid, unknownCharaString)
                currentCharaData[LIST_UUID] = uuid!!
                gattCharacteristicGroupData += currentCharaData
            }
            mGattCharacteristics += charas
            gattCharacteristicData += gattCharacteristicGroupData
        }
    }

    private fun setCharacteristicNotification(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled)
        val uuid: UUID = UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)
        val descriptor = characteristic.getDescriptor(uuid).apply {
            value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        }
        bluetoothGatt.writeDescriptor(descriptor)
    }

    fun close() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        bluetoothGatt.close()
//        bluetoothGatt = null
    }

}