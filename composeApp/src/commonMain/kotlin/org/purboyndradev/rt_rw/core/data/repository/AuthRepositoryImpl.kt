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
    override suspend fun signIn(phoneNumber: String): ResponseDto<SignInDto> {

        val responseDto = api.signIn(phoneNumber)

        val data = responseDto.data ?: return responseDto

        val isNotVerified = responseDto.code === "USER_NOT_VERIFIED"
        if (isNotVerified) {
            return responseDto
        }

        val accessToken = data.accessToken
        val refreshToken = data.refreshToken

        val payload = JWTObject.decodeJwtPayload(accessToken)
            ?: return responseDto


        val username = payload["name"]?.jsonPrimitive?.contentOrNull ?: ""
        val userId = payload["user_id"]?.jsonPrimitive?.contentOrNull ?: ""
        appAuthRepository.saveTokens(accessToken, refreshToken)
        appAuthRepository.saveUsername(username)
        appAuthRepository.saveUserId(userId)
        tokenStore.setTokens(accessToken, refreshToken)
        return responseDto
    }

    override suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): ResponseDto<VerifyOtpDto> {

        val responseDto = api.verifyOtp(phoneNumber, otp)

        val data = responseDto.data ?: return responseDto

        val payload = JWTObject.decodeJwtPayload(data.accessToken)
            ?: return responseDto

        val username =
            payload["name"]?.jsonPrimitive?.contentOrNull ?: ""

        val userId = payload["user_id"]?.jsonPrimitive?.contentOrNull

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
        return responseDto
    }

    override suspend fun refreshToken(
        refreshToken: String,
    ): ResponseDto<RefreshTokenDto> {

        val responseDto = api.refreshToken(refreshToken)
        val data = responseDto.data ?: return responseDto
        val accessToken = data.accessToken
        val newRefreshToken = data.refreshToken

        appAuthRepository.saveTokens(
            accessToken,
            newRefreshToken
        )

        tokenStore.setTokens(accessToken, newRefreshToken)
        return responseDto
    }
}