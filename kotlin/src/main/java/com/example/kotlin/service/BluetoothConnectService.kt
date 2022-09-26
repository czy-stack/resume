package com.example.kotlin.service

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.IBinder
import com.android.common.manager.BluetoothExt
import com.android.common.utils.LogUtils
import com.example.kotlin.constants.SampleGattAttributes.WRITE_CHARACTER_UUID
import java.io.IOException

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/9
 */
class BluetoothConnectService : Service() {
    private val TAG = "BluetoothConnectService"
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(WRITE_CHARACTER_UUID)
        }

        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothExt.instance.getAdapter()?.cancelDiscovery()

            mmSocket?.use { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket)
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                LogUtils.e(TAG, "Could not close the client socket$e")
            }
        }
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {

    }
}