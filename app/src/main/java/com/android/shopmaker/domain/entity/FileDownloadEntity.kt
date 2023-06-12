package com.android.shopmaker.domain.entity

data class FileDownloadEntity(
    var filePath: String?,
    var fileName: String?
) {
    open fun isEmpty(): Boolean {
        return filePath.isNullOrEmpty()
    }

    open fun getFileFullPath() : String {
        return filePath ?: ""
    }
}
