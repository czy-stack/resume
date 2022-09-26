package com.example.kotlin.constants

import java.util.*
import kotlin.collections.HashMap

/**
 * @作者 陈忠岳
 * @主要功能 低功耗蓝牙设备连接常量
 * @创建日期  2022/9/6
 */
object SampleGattAttributes {

    const val HEART_RATE_MEASUREMENT= "01ff0100-ba5e-f4ee-5ca1-eb1e5e4b1ce1"
    const val CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805F9B34FB"

    val SERVICE_UUID = UUID.fromString("01ff0100-ba5e-f4ee-5ca1-eb1e5e4b1ce1")
    val WRITE_CHARACTER_UUID = UUID.fromString("01ff0101-ba5e-f4ee-5ca1-eb1e5e4b1ce1")
    val DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")


    /**
     *@see com.hykj.identify.service.BluetoothService
     */
    // Message types sent from the BluetoothChatService Handler
    const val MESSAGE_STATE_CHANGE: Int = 1
    const val MESSAGE_READ = 2
    const val MESSAGE_WRITE = 3
    const val MESSAGE_DEVICE_NAME = 4
    const val MESSAGE_TOAST = 5

    // Key names received from the BluetoothChatService Handler
    const val DEVICE_NAME = "device_name"
    const val TOAST = "toast"

    // Constants that indicate the current connection state
    const val STATE_NONE = 0 // we're doing nothing

    const val STATE_LISTEN = 1 // now listening for incoming connections

    const val STATE_CONNECTING = 2 // now initiating an outgoing connection

    const val STATE_CONNECTED = 3 // now connected to a remote device

    fun lookup(uuid :String?,unknownServiceString:String) : String{
        return ""
    }
}