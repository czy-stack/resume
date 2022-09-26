package com.example.kotlin.activity.camera

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import com.android.common.base.BaseActivity
import com.android.common.manager.BluetoothExt
import com.android.common.manager.BluetoothExt.Companion.REQUEST_ENABLE_BT
import com.android.common.utils.LogUtils
import com.android.common.utils.toast
import com.bumptech.glide.Glide
import com.example.kotlin.R
import com.example.kotlin.activity.device.DeviceListActivity
import com.example.kotlin.databinding.ActivityCameraBinding
import com.example.kotlin.service.BluetoothService

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/8/23
 */
class CameraActivity : BaseActivity<CameraContract.Presenter, ActivityCameraBinding>(),
    CameraContract.View,
    LifecycleOwner {
    private lateinit var registerForActivityResult : ActivityResultLauncher<Uri>
    private var uri : Uri? = null
    override fun getLayoutId(): Int {
        return 0
    }

    override fun setBinding(): ActivityCameraBinding {
        return ActivityCameraBinding.inflate(layoutInflater)
    }

    override fun initView() {
        presenter = CameraPresenter(this, this, this)
        uri = presenter.getPhotoUri()
        registerForActivityResult = registerForActivityResult(ActivityResultContracts.TakePicture() ) {
            if (it)
                Glide.with(this).load(uri).into(binding.imageView)
        }
    }

    override fun initData() {
        BluetoothExt.instance.init(this)

    }

    override fun initListener() {
        binding.btnTakePhoto.setOnClickListener {  // 拍照
            registerForActivityResult.launch(uri)
        }

        binding.btStartBluetooth.setOnClickListener {  // 打开蓝牙
            if (!BluetoothExt.instance.checkBluetooth(this)){
              registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                    if (it.resultCode == RESULT_OK){
                        presenter.startBluetooth()
                    }else {
                        LogUtils.i(this@CameraActivity.localClassName,"BT not enabled")
                        this.toast( R.string.bt_not_enabled_leaving)
                    }
                }.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }

        binding.btScanSecureBluetooth.setOnClickListener { //安全扫描
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                presenter.connectDevice(it.data,true)
            }.launch(Intent(this, DeviceListActivity::class.java ))
        }

        binding.btScanInsecureBluetooth.setOnClickListener { //非安全扫描
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                presenter.connectDevice(it.data,false)
            }.launch(Intent(this, DeviceListActivity::class.java ))
        }

        binding.btDiscoverable.setOnClickListener {  //可检测
            BluetoothExt.instance.discoverable(this)
        }
    }

}