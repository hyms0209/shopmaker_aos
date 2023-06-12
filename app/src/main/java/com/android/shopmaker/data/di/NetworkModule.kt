package com.android.shopmaker.data.di

import com.android.shopmaker.data.service.network.FileDownloadAPI
import com.android.shopmaker.data.service.network.RemoteServiceAPI
import com.android.shopmaker.data.service.network.RemoteServiceIntercepter
import com.android.shopmaker.domain.constant.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    /***
     * Retrofit2에 주입을 하기위한 OKHttpClient 싱글톤 생성
     */
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        //OKHttpClient 생성
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor(RemoteServiceIntercepter())
            .build()
    }

    /***
     * 공통으로 사용하는 Retrofit2의 주입을 위한 싱글톤 생성
     */
    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BaseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    /***
     * HttpLogingInterceptro을 주입을 위한 생성
     */
    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    /***
     * GSonConverterFactory의 주입을 위한 싱글톤 생성
     */
    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    /***
     * 파일다운로드를 위한 API 인터페이스 생성
     */
    @Provides
    @Singleton
    fun provideFileDownloadAPI(retrofit: Retrofit): FileDownloadAPI
            = retrofit.create(FileDownloadAPI::class.java)

    /***
     * 서버와의 API통신을 위한 인터페이스 생성
     */
    @Provides
    @Singleton
    fun provideRemoteServiceAPI(retrofit: Retrofit): RemoteServiceAPI
            = retrofit.create(RemoteServiceAPI::class.java)
}