package com.android.shopmaker.domain.usecase

import com.android.shopmaker.data.repository.RemoteRepository
import javax.inject.Inject

class BannerUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {
    suspend fun excute(code: String) = remoteRepository.banner(code)
}