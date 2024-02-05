package com.example.allahs99names.di

import android.content.Context
import androidx.room.Room
import com.example.allahs99names.core.ResourceManager
import com.example.allahs99names.core.ResourceManagerImpl
import com.example.allahs99names.local.BlessedNameDao
import com.example.allahs99names.local.BlessedNameDatabase
import com.example.allahs99names.local.BlessedNameDatabase.Companion.BLESSED_NAME_DATABASE
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    @Singleton
    abstract fun bindResourceManager(impl: ResourceManagerImpl): ResourceManager

    companion object {

        @Provides
        @Singleton
        fun provideBlessedNameDatabase(@ApplicationContext context: Context): BlessedNameDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = BlessedNameDatabase::class.java,
                name = BLESSED_NAME_DATABASE
            ).build()
        }

        @Provides
        @Singleton
        fun provideBlessedNameDao(database: BlessedNameDatabase): BlessedNameDao {
            return database.blessedNameDao()
        }
    }
}
