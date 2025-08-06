package io.jacob.igozogo.core.data.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MockWebServerRule : TestWatcher()  {
    lateinit var mockWebServer: MockWebServer
        private set

    lateinit var odiiApi: OdiiApi
        private set

    lateinit var gson: Gson
        private set

    override fun starting(description: Description?) {
        mockWebServer = MockWebServer()
        gson = Gson()

        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        odiiApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OdiiApi::class.java)
    }

    override fun finished(description: Description?) {
        mockWebServer.shutdown()
    }
}