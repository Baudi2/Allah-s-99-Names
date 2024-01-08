package com.example.allahs99names.di

import com.example.allahs99names.core.ResourceManager
import com.example.allahs99names.core.ResourceManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {


    @Binds
    @Singleton
    abstract fun bindResourceManager(impl: ResourceManagerImpl): ResourceManager

    companion object {

    }
}