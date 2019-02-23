package com.thumbs.android.thumbsAndroid.api

import com.thumbs.android.thumbsAndroid.model.Thumb
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ThumbsApi {

    @POST("users/{userId}/thumbs")
    fun createThumbs(@Path("userId") userId: Int, @Body body: HashMap<String, Any>): Completable

    @GET("thumbs/{thumbId}")
    fun loadThumb(@Path("thumbId") thumbId: Int
    ): Single<Thumb>
}