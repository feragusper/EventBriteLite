package com.feragusper.eventbritelite.di

import com.feragusper.eventbritelite.data.api.EventAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideEventAPIService(): EventAPIService {
        return EventAPIService.create()
    }
}
