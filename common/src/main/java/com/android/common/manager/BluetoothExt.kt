package com.android.common.manager

import android.Manifest
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.android.common.inter.DiscoveryCallback
import com.android.common.utils.LogUtils
import com.android.common.utils.RxUtils
import okio.ByteString.Companion.toByteString
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/1
 */
class BluetoothExt private constructor() : ScanCallback(){
    companion object{
        val instance: BluetoothExt by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BluetoothExt()
        }
        const val REQUEST_ENABLE_BT = 10
    }

    private var bluetoothManager : BluetoothManager? = null
    private var bluetoothAdapter : BluetoothAdapter? = null
    private var discoveryCallback : DiscoveryCallback? = null

    private val BluetoothAdapter.isDisabled: Boolean get() = !isEnabled
    private var mScanning: Boolean = false

    private  val period: Long = 100

    fun init(context: Context){
        bluetoothManager =  context.getSystemService(Context.BLUETOOTH_SERVICE )  as BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter
//        this.discoveryCallback = discoveryCallback
//        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
//            if (!checkPermission(context)){
//                context.startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),BLUETOOTH_RESULT)
//            }
//        }
    }

    fun getAdapter():BluetoothAdapter?{
        return bluetoothAdapter
    }

    fun checkBluetooth(context: Context) :Boolean{
//        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
//            if(ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.BLUETOOTH_CONNECT
//                ) != PackageManager.PERMISSION_GRANTED){
//                return false
//            }
//            return true
//        }
        return bluetoothAdapter?.isEnabled!!
    }


    /**
     * 扫描低功耗蓝牙
     */
    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    private fun startScanLe(context: Context) {
        RxUtils.timer(period, TimeUnit.SECONDS) {
            stopScanLe(context)
        }
        mScanning = true
        bluetoothAdapter?.bluetoothLeScanner?.startScan(this)
    }

    /**
     * 特定扫描低功耗蓝牙
     */
    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    private fun startScanLe(context: Context, settings: ScanSettings , filters : List<ScanFilter>) {
        RxUtils.timer(period, TimeUnit.SECONDS) {
            stopScanLe(context)
        }
        mScanning = true
        bluetoothAdapter?.bluetoothLeScanner?.startScan(filters,settings,this)
    }

    /**
     * 暂停扫描低功耗蓝牙
     */
    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    private fun stopScanLe(context: Context){
        mScanning  = false
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(this)
    }

    /**
     * 获取已配对蓝牙列表
     */
    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun getBondedDevices(context: Context) : List<BluetoothDevice>?{
        return  bluetoothAdapter?.bondedDevices?.toList()
    }

    /**
     * 启用可检测性
     */
    fun discoverable(context: Context){
        context.startActivity( Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, period)
        })
    }


    /**
     * 连接低功耗蓝牙
     */
    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    private fun connectLe(context: Context,device : BluetoothDevice, gattCallback:BluetoothGattCallback){
        device.connectGatt(context,true,gattCallback)
    }

    fun getDevice(address : String?) : BluetoothDevice?{
       return bluetoothAdapter?.getRemoteDevice(address)
    }

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        if (result != null)
            discoveryCallback?.onFindBle(callbackType,result)
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        discoveryCallback?.onNotFind()
    }


    override fun onBatchScanResults(results: MutableList<ScanResult>?) {

    }
}