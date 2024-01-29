package com.example.allahs99names.di

import com.example.allahs99names.data.NamesListRepositoryImpl
import com.example.allahs99names.domain.repository.NamesListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NamesListModule {

    @Binds
    @Singleton
    fun bindNamesListRepository(impl: NamesListRepositoryImpl): NamesListRepository
}