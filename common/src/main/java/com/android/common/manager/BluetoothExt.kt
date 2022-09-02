package com.android.common.manager

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import com.android.common.utils.RxUtils
import java.util.concurrent.TimeUnit


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/1
 */
class BluetoothExt private constructor(){
    companion object{
        val instance: BluetoothExt by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BluetoothExt()
        }
        const val BLUETOOTH_RESULT = 10
    }

    private var bluetoothManager : BluetoothManager? = null
    private var bluetoothAdapter : BluetoothAdapter? = null


    private val BluetoothAdapter.isDisabled: Boolean get() = !isEnabled
    private var mScanning: Boolean = false


   private  val period: Long = 100


    private fun init(context: Activity){
        bluetoothManager =  context.getSystemService(Context.BLUETOOTH_SERVICE )  as BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter
        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            if (!checkPermission(context)){
                context.startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),BLUETOOTH_RESULT)
            }
        }
    }

    private fun checkPermission(context: Context):Boolean{
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun scanLeDevice(enable: Boolean,context: Context){
        if (!checkPermission(context)){
            return
        }
        when (enable) {
            true -> {
                RxUtils.timer(period, TimeUnit.SECONDS
                ) {
                    mScanning = false
                    bluetoothAdapter?.bluetoothLeScanner.stopScan(object :ScanCallback(){})
                }
                mScanning = true
                bluetoothAdapter?.bluetoothLeScanner?.startScan(leScanCallback)
            }
            else -> {
                mScanning = false
                bluetoothAdapter?.bluetoothLeScanner.stopScan(leScanCallback)
            }
        }
    }



}