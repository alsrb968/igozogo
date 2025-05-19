package io.jacob.igozogo.core.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VisitKoreaClient {
    private const val BASE_URL = "https://apis.data.go.kr/B551011/"
    private var _retrofit: Retrofit? = null

    val retrofit: Retrofit =
        _retrofit ?: synchronized(this) {
            _retrofit ?: Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().also {
                    _retrofit = it
                }
        }
}