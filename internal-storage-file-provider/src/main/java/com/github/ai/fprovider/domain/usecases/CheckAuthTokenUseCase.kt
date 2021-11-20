package com.github.ai.fprovider.domain.usecases

import com.github.ai.fprovider.data.dao.TokenDao

internal class CheckAuthTokenUseCase(
    private val tokenDao: TokenDao
) {

    fun isPathAccessible(authToken: String, path: String): Boolean {
        val authData = tokenDao.getAll()
        return authData
            .any { data ->
                data.authToken == authToken && isHasAccessToPath(path, data.rootPath)
            }
    }

    private fun isHasAccessToPath(path: String, root: String): Boolean {
        return path == root || path.startsWith(root)
    }
}