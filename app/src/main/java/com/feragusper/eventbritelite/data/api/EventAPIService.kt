package com.feragusper.eventbritelite.data.api

import com.feragusper.eventbritelite.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Used to connect to the TMDB API to query catalog
 */
interface EventAPIService {

    @GET("organizations/{organizationId}/events")
    suspend fun getEventsByOrganization(
        @Path("organizationId") organizationId: String = BuildConfig.EVENTBRITE_ORGANIZATION_ID,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("token") apiToken: String = BuildConfig.EVENTBRITE_API_TOKEN,
    ): EventListResponse

    @GET("events/{eventId}")
    suspend fun fetchEvent(
        @Path("eventId") eventId: String,
        @Query("token") apiToken: String = BuildConfig.EVENTBRITE_API_TOKEN,
        @Query("expand") expand: String = "venue",
    ): EventEntity

    companion object {
        fun create(): EventAPIService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.EVENTBRITE_API_HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EventAPIService::class.java)
        }
    }
}
