package com.example.vectors.di

import android.content.Context
import androidx.room.Room
import com.example.vectors.data.local.AppDatabase
import com.example.vectors.data.local.VectorDao
import com.example.vectors.data.repository.VectorRepository
import com.example.vectors.domain.usecase.VectorOperations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "vectors_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideVectorDao(database: AppDatabase): VectorDao = database.vectorDao()

    @Provides
    @Singleton
    fun provideVectorRepository(dao: VectorDao): VectorRepository =
        VectorRepository(dao)

    @Provides
    @Singleton
    fun provideVectorOperations(): VectorOperations = VectorOperations()
}