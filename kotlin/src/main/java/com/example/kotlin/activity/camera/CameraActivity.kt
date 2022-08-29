package com.example.kotlin.activity.camera

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import com.android.common.base.BaseActivity
import com.bumptech.glide.Glide
import com.example.kotlin.BuildConfig
import com.example.kotlin.databinding.ActivityCameraBinding
import java.io.File


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/8/23
 */
class CameraActivity : BaseActivity<CameraContract.Presenter, ActivityCameraBinding>(),
    CameraContract.View,
    LifecycleOwner {
    override lateinit var presenter: CameraContract.Presenter
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
        uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "图片名称.jpg")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }else{
            FileProvider.getUriForFile(this, "com.example.kotlin.provider",File(externalCacheDir!!.absolutePath+"img.jpg"))
        }

        registerForActivityResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
                if (it)
                    Glide.with(this).load(uri).into(binding.imageView)
            }
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.btnTakePhoto.setOnClickListener {
            registerForActivityResult.launch(uri)
        }
    }
}