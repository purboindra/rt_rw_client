package org.purboyndradev.rt_rw.core.data.datastore

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull


class AuthTokenStore(
    private val repo: AppAuthRepository
) {
    val memory = MutableStateFlow<BearerTokens?>(null)
    
    suspend fun initFromDisk() {
        val access = repo.accessTokenFlow.firstOrNull()
        val refresh = repo.refreshTokenFlow.firstOrNull()
        if (!access.isNullOrBlank() && !refresh.isNullOrBlank()) {
            memory.value = BearerTokens(access, refresh)
        }
    }
    
    suspend fun setTokens(access: String, refresh: String) {
        memory.value = BearerTokens(access, refresh)
        repo.saveTokens(access, refresh)
    }
    
    suspend fun clear() {
        memory.value = null
        repo.clearTokens()
    }
}
