package com.android.shopmaker.data.di

import com.android.shopmaker.data.repository.FileDownloadRepository
import com.android.shopmaker.data.repository.RemoteRepository
import com.android.shopmaker.data.service.network.DownloadDataSourceImpl
import com.android.shopmaker.data.service.network.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideFileDownloadRepository(
        downloadDataSource: DownloadDataSourceImpl
    ): FileDownloadRepository {
        return FileDownloadRepository(
            downloadDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(
        remoteDataSource: RemoteDataSourceImpl
    ): RemoteRepository {
        return RemoteRepository(
            remoteDataSource
        )
    }
}