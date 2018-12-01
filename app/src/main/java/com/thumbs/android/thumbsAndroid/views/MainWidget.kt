package com.thumbs.android.thumbsAndroid.views

import android.app.Service
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import com.thumbs.android.thumbsAndroid.R
import com.thumbs.android.thumbsAndroid.modules.domain.model.StatusRequestParam
import com.thumbs.android.thumbsAndroid.modules.domain.repositories.UserRepositoryImpl
import com.thumbs.android.thumbsAndroid.modules.domain.repositories.UserRepository
import com.thumbs.android.thumbsAndroid.modules.network.NetworkConnector
import com.thumbs.android.thumbsAndroid.modules.network.api.UserApi

class MainWidget {
  var singleTabConfirm: GestureDetector? = null
  val userRepository : UserRepository by lazy {
    UserRepositoryImpl(NetworkConnector.createRetrofit(UserApi::class.java))
  }

  constructor(service: Service, windowManager: WindowManager) {
    singleTabConfirm = GestureDetector(service, SingleTapConfirm());

    val view = LayoutInflater.from(service)
      .inflate(R.layout.layout_floating_widget, null)

    val layoutParams = createLayoutParams(-100, -100)
    val image = view.findViewById<ImageView>(R.id.icon_thu)
    image.setBackgroundResource(R.drawable.thu_cat)

    windowManager.addView(view, layoutParams);

    setOnTouch(
      view,
      layoutParams,
      singleTabConfirm!!,
      windowManager,
      this::handleSingleClick
    )
  }

  fun handleSingleClick(view: View) {
    userRepository.getStatus(StatusRequestParam(
      123412341234,
      "wash"
    ))
      .subscribe({ result ->
        Toast.makeText(
          view.context,
          result.property + " " + result.request_id,
          Toast.LENGTH_SHORT
        )
          .show()
      }, { throwable ->
        throwable.printStackTrace()
      })

  }
}
