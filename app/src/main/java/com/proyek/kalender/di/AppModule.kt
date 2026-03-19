package com.proyek.kalender.di

import android.app.Application
import androidx.room.Room
import com.proyek.kalender.data.local.KalenderDatabase
import com.proyek.kalender.data.repository.EventRepositoryImpl
import com.proyek.kalender.domain.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideKalenderDatabase(app: Application): KalenderDatabase {
        return Room.databaseBuilder(
            app,
            KalenderDatabase::class.java,
            "kalender_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEventRepository(db: KalenderDatabase): EventRepository {
        return EventRepositoryImpl(db.eventDao)
    }
}