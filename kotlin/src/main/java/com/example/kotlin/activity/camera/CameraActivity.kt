package com.example.kotlin.activity.camera

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.android.common.base.BaseActivity
import com.android.common.manager.BluetoothExt
import com.android.common.utils.LogUtils
import com.android.common.utils.toast
import com.bumptech.glide.Glide
import com.example.kotlin.R
import com.example.kotlin.activity.device.DeviceListActivity
import com.example.kotlin.databinding.ActivityCameraBinding

/**
 * @作者 陈忠岳
 * @主要功能 拍照与 BLE 设备调试页
 * @创建日期  2022/8/23
 *
 * 修正：
 * 1. 拍照前申请 CAMERA 运行时权限。manifest 声明了 CAMERA 之后，即使只是调起
 *    系统相机的 IMAGE_CAPTURE，也会被要求先授权，否则 SecurityException 直接闪退；
 * 2. 所有 ActivityResultLauncher 改为字段级注册。原实现把
 *    registerForActivityResult(...) 写在点击回调里，此时 Activity 已处于 RESUMED，
 *    会抛 IllegalStateException（“attempting to register while current state is RESUMED”）；
 * 3. 拍照输出 Uri 从 initView 挪到点击时创建：每次拍照生成新文件，取消后也不会留下空图。
 */
class CameraActivity : BaseActivity<CameraContract.Presenter, ActivityCameraBinding>(),
    CameraContract.View {

    /** 本次拍照的输出目标，拍完在回调里读取 */
    private var pendingPhotoUri: Uri? = null

    /** 安全 / 非安全扫描共用同一个结果回调，用标记区分 */
    private var secureScan = true

    // ---- Launcher 必须在 STARTED 之前注册，因此放在属性初始化阶段 ----

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val target = pendingPhotoUri
            if (success && target != null) {
                Glide.with(this).load(target).into(binding.imageView)
            } else {
                LogUtils.w(msg = "拍照取消或失败")
            }
            pendingPhotoUri = null
        }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openSystemCamera()
            } else {
                toast(R.string.camera_permission_denied)
            }
        }

    private val enableBtLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                presenter.startBluetooth()
            } else {
                LogUtils.i(this.localClassName, "BT not enabled")
                toast(R.string.bt_not_enabled_leaving)
            }
        }

    private val deviceListLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            presenter.connectDevice(result.data, secureScan)
        }

    override fun setBinding(): ActivityCameraBinding {
        return ActivityCameraBinding.inflate(layoutInflater)
    }

    override fun initView() {
        presenter = CameraPresenter(this, this, this)
    }

    override fun initData() {
        BluetoothExt.instance.init(this)
    }

    override fun initListener() {
        binding.btnTakePhoto.setOnClickListener { // 拍照
            takePhotoWithPermission()
        }

        binding.btStartBluetooth.setOnClickListener { // 打开蓝牙
            if (!BluetoothExt.instance.checkBluetooth(this)) {
                enableBtLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }

        binding.btScanSecureBluetooth.setOnClickListener { // 安全扫描
            secureScan = true
            deviceListLauncher.launch(Intent(this, DeviceListActivity::class.java))
        }

        binding.btScanInsecureBluetooth.setOnClickListener { // 非安全扫描
            secureScan = false
            deviceListLauncher.launch(Intent(this, DeviceListActivity::class.java))
        }

        binding.btDiscoverable.setOnClickListener { // 可检测
            BluetoothExt.instance.discoverable(this)
        }
    }

    private fun takePhotoWithPermission() {
        val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        if (granted) {
            openSystemCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openSystemCamera() {
        val target = presenter.getPhotoUri()
        if (target == null) {
            // MediaStore.insert 失败时会返回 null，直接 launch(null) 会抛 IllegalArgumentException
            toast(R.string.camera_create_file_failed)
            return
        }
        pendingPhotoUri = target
        takePictureLauncher.launch(target)
    }
}
