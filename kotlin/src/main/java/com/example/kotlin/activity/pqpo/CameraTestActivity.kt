package com.example.kotlin.activity.pqpo

import android.Manifest
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import com.android.common.base.BaseActivity
import com.example.kotlin.databinding.ActivityTestCameraBinding
//import com.google.android.cameraview.CameraImpl
//import me.pqpo.smartcameralib.MaskView
//import me.pqpo.smartcameralib.SmartScanner


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/8/23
 */
class CameraTestActivity : BaseActivity<CameraTestContract.Presenter, ActivityTestCameraBinding>(),
    CameraTestContract.View,
    LifecycleOwner {
    override lateinit var presenter: CameraTestContract.Presenter
    override fun getLayoutId(): Int {
        return 0
    }

    override fun setBinding(): ActivityTestCameraBinding {
        return ActivityTestCameraBinding.inflate(layoutInflater)
    }

    override fun initView() {
        presenter = CameraTestPresenter(this, this, this)
        initScannerParams()
        initMaskView()
    }


    override fun initData() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                //用户同意了该权限
//                binding.cameraView.start()
//                binding.cameraView.startScan()
            }else{
                //用户拒绝了该权限
            }

        }.launch(Manifest.permission.CAMERA)
    }

    override fun initListener() {
//        binding.cameraView.getSmartScanner().setPreview(true)
//        binding.cameraView.setOnScanResultListener { smartCameraView, result, yuvData ->
//            val previewBitmap = smartCameraView?.previewBitmap
//            if (previewBitmap != null) {
//                //                    ivPreview.setImageBitmap(previewBitmap)
//            }
//            false
//        }
//        binding.cameraView.addCallback(object : CameraImpl.Callback(){
//            override fun onPictureTaken(camera: CameraImpl?, data: ByteArray?) {
//                super.onPictureTaken(camera, data)
//            }
//        })
//        binding.btnTakePhoto.setOnClickListener {
//            binding.cameraView.start()
//            binding.cameraView.startScan()
//        }
    }

    private fun initScannerParams() {
//        SmartScanner.DEBUG = true
//        SmartScanner.detectionRatio = 0.1f
//        SmartScanner.checkMinLengthRatio = 0.8f
//        SmartScanner.cannyThreshold1 = 20
//        SmartScanner.cannyThreshold2 = 50
//        SmartScanner.houghLinesThreshold = 130
//        SmartScanner.houghLinesMinLineLength = 80
//        SmartScanner.houghLinesMaxLineGap = 10
////        SmartScanner.firstGaussianBlurRadius = 3;
////        SmartScanner.secondGaussianBlurRadius = 3;
//        SmartScanner.maxSize = 300f
//        SmartScanner.angleThreshold = 5f
//        // don't forget reload params
//        SmartScanner.reloadParams()

    }

    private fun initMaskView() {
//        val maskView = binding.cameraView.maskView as MaskView
//        maskView.setMaskLineColor(-0xff524b)
//        maskView.setShowScanLine(true)
//        maskView.setScanLineGradient(-0xff524b, 0x0000adb5)
//        maskView.setMaskLineWidth(2)
//        maskView.setMaskRadius(5)
//        maskView.setScanSpeed(6)
//        maskView.setScanGradientSpread(80)
//        binding.cameraView.post({
//            val width: Int = binding.cameraView.getWidth()
//            val height: Int = binding.cameraView.getHeight()
//            if (width < height) {
//                maskView.setMaskSize((width * 0.6f).toInt(), (width * 0.6f / 0.63).toInt())
//                maskView.setMaskOffset(0, -(width * 0.1).toInt())
//            } else {
//                maskView.setMaskSize((width * 0.6f).toInt(), (width * 0.6f * 0.63).toInt())
//            }
//        })
//        binding.cameraView.maskView = maskView
    }


    override fun onResume() {
        super.onResume()
//        binding.cameraView.start()
//        binding.cameraView.startScan()
    }

    override fun onPause() {
//        binding.cameraView.stop()
        super.onPause()
//        binding.cameraView.stopScan()
    }
}