package com.example.kotlin.activity.camera

import android.bluetooth.BluetoothDevice
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.android.common.lifecycle.LifecyclePresenter
import com.android.common.manager.BluetoothExt
import com.android.common.utils.toast
import com.example.kotlin.R
import com.example.kotlin.activity.device.DeviceListActivity
import com.example.kotlin.activity.device.EXTRA_DEVICE_ADDRESS
import com.example.kotlin.activity.test.TestContract
import com.example.kotlin.constants.SampleGattAttributes
import com.example.kotlin.service.BluetoothService
import java.io.File

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/8/23
 */
class CameraPresenter (private val context: Context, private val view: CameraContract.View, lifecycleOwner: LifecycleOwner)  : LifecyclePresenter(
    lifecycleOwner
), CameraContract.Presenter {

    private var mConnectedDeviceName : String = "111"

    override fun startBluetooth() {
        BluetoothService(BluetoothExt.instance.getAdapter()!!,mHandler)
    }

    override fun getPhotoUri() : Uri? {
       val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "图片名称.jpg")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }else{
            FileProvider.getUriForFile(context, "com.example.kotlin.provider",
                File(context.externalCacheDir!!.absolutePath+"img.jpg")
            )
        }
        return uri
    }

    override fun connectDevice(data: Intent?, secure: Boolean) {
        BluetoothExt.instance.getDevice(data?.extras?.getString(EXTRA_DEVICE_ADDRESS))
    }

    override fun start() {

    }
    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SampleGattAttributes.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
                    SampleGattAttributes.STATE_CONNECTED -> {
                        setStatus(context.getString(R.string.title_connected_to, mConnectedDeviceName))
//                        mConversationArrayAdapter.clear()
                    }
                    SampleGattAttributes.STATE_CONNECTING ->
                        setStatus(R.string.title_connecting)
                    SampleGattAttributes.STATE_LISTEN, SampleGattAttributes.STATE_NONE ->
                        setStatus("not connected")
                }
                SampleGattAttributes.MESSAGE_WRITE -> {
                    val writeBuf = msg.obj as ByteArray
                    // construct a string from the buffer
                    val writeMessage = String(writeBuf)
//                    mConversationArrayAdapter.add("Me:  $writeMessage")
                }
                SampleGattAttributes.MESSAGE_READ -> {
                    val readBuf = msg.obj as ByteArray
                    // construct a string from the valid bytes in the buffer
                    val readMessage = String(readBuf, 0, msg.arg1)
//                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage)
                }
                SampleGattAttributes.MESSAGE_DEVICE_NAME -> {
                    // save the connected device's name
//                    mConnectedDeviceName = msg.data.getString(SampleGattAttributes.DEVICE_NAME)
                    "Connected to $mConnectedDeviceName".toast(context)
                }
                SampleGattAttributes.MESSAGE_TOAST ->
                    msg.data.getString(SampleGattAttributes.TOAST)?.toast(context)
            }
        }

        private fun setStatus(toast: Int) {
            context.getString(toast).toast(context)
        }

        private fun setStatus(toast: String) {
            toast.toast(context)
        }
    }
}