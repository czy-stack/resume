package com.example.kotlin.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.android.common.manager.BluetoothExt
import com.example.kotlin.constants.SampleGattAttributes.SERVICE_UUID
import java.io.IOException

/**
 * @作者 陈忠岳
 * @主要功能  蓝牙服务器服务
 * @创建日期  2022/9/8
 */
class BluetoothAcceptService :Service(){
    private val TAG = "BluetoothAcceptService"
    private var acceptThread :Thread? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        acceptThread = AcceptThread()
        acceptThread?.start()
        return super.onStartCommand(intent, flags, startId)
    }


    @SuppressLint("MissingPermission")
    private inner class AcceptThread() : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            BluetoothExt.instance.getAdapter()?.listenUsingInsecureRfcommWithServiceRecord("BluetoothAcceptService", SERVICE_UUID)
        }

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e(TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    manageMyConnectedSocket(it)
                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }

    private fun manageMyConnectedSocket(it: BluetoothSocket) {

    }
}