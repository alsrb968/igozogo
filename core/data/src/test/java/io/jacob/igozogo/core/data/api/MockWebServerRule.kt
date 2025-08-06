package io.jacob.igozogo.core.data.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MockWebServerRule : TestWatcher() {
    private val mockWebServer = MockWebServer()
    private val gson = Gson()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    val odiiApi: OdiiApi
        get() = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OdiiApi::class.java)

    override fun starting(description: Description?) {
        mockWebServer.start()
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val url = request.requestUrl ?: return MockResponse().setResponseCode(400)

                return when (url.encodedPath) {
                    "/Odii/themeBasedList" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(gson.toJson(OdiiApiTestDataHelper.createSuccessfulThemeResponse()))
                        .addHeader("Content-Type", "application/json")

                    "/Odii/themeLocationBasedList" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(gson.toJson(OdiiApiTestDataHelper.createSuccessfulThemeResponse()))
                        .addHeader("Content-Type", "application/json")

                    "/Odii/themeSearchList" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(
                            gson.toJson(
                                OdiiApiTestDataHelper.createFilteredThemeResponse(
                                    titleFilter = "부산"
                                )
                            )
                        )
                        .addHeader("Content-Type", "application/json")

                    "/Odii/storyBasedList" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(gson.toJson(OdiiApiTestDataHelper.createSuccessfulStoryResponse()))
                        .addHeader("Content-Type", "application/json")

                    "/Odii/storyLocationBasedList" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(gson.toJson(OdiiApiTestDataHelper.createSuccessfulStoryResponse()))
                        .addHeader("Content-Type", "application/json")

                    "/Odii/storySearchList" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(
                            gson.toJson(
                                OdiiApiTestDataHelper.createFilteredStoryResponse(
                                    titleFilter = "무각사"
                                )
                            )
                        )
                        .addHeader("Content-Type", "application/json")

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

    }

    override fun finished(description: Description?) {
        mockWebServer.shutdown()
    }

    fun setQueueDispatcher(
        code: Int = 200,
        body: String,
        header: Pair<String, String> = "Content-Type" to "application/json"
    ) {
        mockWebServer.dispatcher = QueueDispatcher()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(body)
                .addHeader(header.first, header.second)
        )
    }
}