package com.thumbs.android.thumbsAndroid.ui.intro

import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import com.thumbs.android.thumbsAndroid.R
import com.thumbs.android.thumbsAndroid.model.Thumb
import com.thumbs.android.thumbsAndroid.services.ControllerService
import com.thumbs.android.thumbsAndroid.ui.base.BaseActivity
import com.thumbs.android.thumbsAndroid.ui.register.RegisterActivity
import com.thumbs.android.thumbsAndroid.ui.status.StatusActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject


class SplashActivity : BaseActivity(), SplashContract.SplashView {


    val presenter by inject<SplashContract.SplashUserActionListerner>()

    private val CODE_OVERLAY_PERMISSION = 2002

    override fun startInject() {
        presenter.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startActivityIfPermissionPass()
        // 1
// 2


    }

    private fun startActivityIfPermissionPass() {

        val runnable = Runnable {

            ValueAnimator.ofFloat(0f, -0.3f).apply {
                duration = 10000
                addUpdateListener {
                    it.interpolator = AccelerateInterpolator()
                    splash_iv.y = it.animatedValue as Float
                    it.repeatMode = REVERSE
                    it.repeatCount = 2
                }
                start()
            }

            when {
                Settings.canDrawOverlays(this) -> {
                    presenter.loadThumbsData()
                }
                else -> {
                    checkPermission()
                    Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }
        Handler().apply {
            postDelayed(runnable, 10000)
        }
    }

    private fun checkPermission() {
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            .let {
                startActivityForResult(it, CODE_OVERLAY_PERMISSION)
            }
    }

    override fun success(thumbs: Thumb) {
        val intent = Intent(this, StatusActivity::class.java)
        startService(Intent(this, ControllerService::class.java))
        startActivity(intent)
        finish()
    }

    override fun fail() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}
