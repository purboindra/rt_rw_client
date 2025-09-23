package org.purboyndradev.rt_rw.core.data.repository

import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.datastore.AuthTokenStore
import org.purboyndradev.rt_rw.core.data.dto.RefreshTokenDto
import org.purboyndradev.rt_rw.core.data.dto.ResponseDto
import org.purboyndradev.rt_rw.core.data.dto.SignInDto
import org.purboyndradev.rt_rw.core.data.dto.VerifyOtpDto
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.mapBoth
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.helper.JWTObject

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val appAuthRepository: AppAuthRepository,
    private val tokenStore: AuthTokenStore,
) : AuthRepository {
    override suspend fun signIn(phoneNumber: String): Result<ResponseDto<SignInDto>, AppError> {
        return api.signIn(phoneNumber).mapBoth(
            onSuccess = { responseDto ->
                val data = responseDto.data ?: return@mapBoth Result.Error(
                    AppError.Remote.InvalidResponse
                )
                
                val code = responseDto.code
                val isNotVerified = code == "USER_NOT_VERIFIED"
                
                if (isNotVerified) return Result.Success(responseDto)
                
                val payload = JWTObject.decodeJwtPayload(data.accessToken)
                    ?: return@mapBoth Result.Error(AppError.Remote.InvalidResponse)
                
                println("Payload sign in: $payload")
                
                val username =
                    payload["name"]?.jsonPrimitive?.contentOrNull ?: ""
                
                val email = payload["email"]?.jsonPrimitive?.contentOrNull
                
                val userId = payload["userId"]?.jsonPrimitive?.contentOrNull
                
                val accessToken = data.accessToken
                val refreshToken = data.refreshToken
                
                appAuthRepository.saveTokens(
                    accessToken,
                    refreshToken
                )
                
                appAuthRepository.saveUsername(
                    username
                )
                
                appAuthRepository.saveUserId(
                    userId ?: ""
                )
                
                Result.Success(responseDto)
            },
            onFailure = { Result.Error(it) }
        )
    }
    
    override suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): Result<ResponseDto<VerifyOtpDto>, AppError> {
        return api.verifyOtp(phoneNumber, otp).mapBoth(
            onSuccess = {
                
                val data = it.data
                    ?: return@mapBoth Result.Error(AppError.Remote.InvalidResponse)
                
                val payload = JWTObject.decodeJwtPayload(data.accessToken)
                    ?: return@mapBoth Result.Error(AppError.Remote.InvalidResponse)
                
                val username =
                    payload["name"]?.jsonPrimitive?.contentOrNull ?: ""
                
                val email = payload["email"]?.jsonPrimitive?.contentOrNull
                
                val userId = payload["userId"]?.jsonPrimitive?.contentOrNull
                
                val accessToken = data.accessToken
                val refreshToken = data.refreshToken
                
                appAuthRepository.saveTokens(
                    accessToken,
                    refreshToken
                )
                
                appAuthRepository.saveUsername(
                    username
                )
                
                appAuthRepository.saveUserId(
                    userId ?: ""
                )
                
                Result.Success(it)
            },
            onFailure = {
                Result.Error(it)
            }
        )
        
    }
    
    override suspend fun refreshToken(
        refreshToken: String,
        
        ): Result<ResponseDto<RefreshTokenDto>, AppError> {
        return api.refreshToken(refreshToken).mapBoth(
            onSuccess = {
                
                val data = it.data
                
                data?.let { token ->
                    tokenStore.setTokens(token.accessToken, token.refreshToken)
                }
                
                Result.Success(it)
            },
            onFailure = {
                Result.Error(it)
            }
        )
    }
}