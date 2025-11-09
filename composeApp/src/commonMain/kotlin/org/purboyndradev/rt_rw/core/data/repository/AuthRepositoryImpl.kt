package org.purboyndradev.rt_rw.core.data.repository

import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.datastore.AuthTokenStore
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.domain.model.RefreshTokenModel
import org.purboyndradev.rt_rw.core.domain.model.SignInModel
import org.purboyndradev.rt_rw.core.domain.model.VerifyOtpModel
import org.purboyndradev.rt_rw.core.network.DataNotFoundException
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.helper.JWTObject

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val appAuthRepository: AppAuthRepository,
    private val tokenStore: AuthTokenStore,
) : AuthRepository {
    override suspend fun signIn(phoneNumber: String): SignInModel {

        val responseDto = api.signIn(phoneNumber)

        val data = responseDto.data

        if (data == null) {
            throw DataNotFoundException(responseDto.message)
        }

        val signInModel = SignInModel(
            accessToken = data.accessToken,
            refreshToken = data.refreshToken,
            redirectUrl = data.redirectUrl,
            code = data.code,
        )

        val isNotVerified = responseDto.code === "USER_NOT_VERIFIED"
        if (isNotVerified) {
            return signInModel
        }

        val accessToken = data.accessToken
        val refreshToken = data.refreshToken

        val payload = JWTObject.decodeJwtPayload(accessToken)
            ?: return signInModel


        val username = payload["name"]?.jsonPrimitive?.contentOrNull ?: ""
        val email = payload["email"]?.jsonPrimitive?.contentOrNull ?: ""
        val userId = payload["user_id"]?.jsonPrimitive?.contentOrNull ?: ""
        appAuthRepository.saveTokens(accessToken, refreshToken)
        appAuthRepository.saveUsername(username)
        appAuthRepository.saveEmail(email)
        appAuthRepository.saveUserId(userId)
        tokenStore.setTokens(accessToken, refreshToken)
        return signInModel
    }

    override suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): VerifyOtpModel {

        val responseDto = api.verifyOtp(phoneNumber, otp)

        val data = responseDto.data ?: throw DataNotFoundException(responseDto.message)

        val verifyOtpModel = VerifyOtpModel(
            accessToken = data.accessToken,
            refreshToken = data.refreshToken
        )

        val payload = JWTObject.decodeJwtPayload(data.accessToken)
            ?: throw DataNotFoundException("Invalid token")

        val username =
            payload["name"]?.jsonPrimitive?.contentOrNull ?: ""

        val userId = payload["user_id"]?.jsonPrimitive?.contentOrNull
        val email = payload["email"]?.jsonPrimitive?.contentOrNull ?: ""

        val accessToken = data.accessToken
        val refreshToken = data.refreshToken

        appAuthRepository.saveTokens(
            accessToken,
            refreshToken
        )

        appAuthRepository.saveUsername(
            username
        )

        appAuthRepository.saveEmail(
            email
        )

        appAuthRepository.saveUserId(
            userId ?: ""
        )
        return verifyOtpModel
    }

    override suspend fun refreshToken(
        refreshToken: String,
    ): RefreshTokenModel {

        val responseDto = api.refreshToken(refreshToken)
        val data = responseDto.data ?: throw DataNotFoundException(responseDto.message)

        val refreshTokenModel = RefreshTokenModel(
            accessToken = data.accessToken,
            refreshToken = data.refreshToken
        )

        val accessToken = data.accessToken
        val newRefreshToken = data.refreshToken

        appAuthRepository.saveTokens(
            accessToken,
            newRefreshToken
        )

        tokenStore.setTokens(accessToken, newRefreshToken)
        return refreshTokenModel
    }
}