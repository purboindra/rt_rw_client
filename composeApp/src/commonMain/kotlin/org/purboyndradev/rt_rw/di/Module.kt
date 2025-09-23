package org.purboyndradev.rt_rw.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.purboyndradev.rt_rw.PlatformConfig
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.datastore.AuthTokenStore
import org.purboyndradev.rt_rw.core.data.datastore.NotificationRepository
import org.purboyndradev.rt_rw.core.data.remote.api.ActivityApi
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.data.remote.api.BannerApi
import org.purboyndradev.rt_rw.core.data.remote.api.NewsApi
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorActivityRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorAuthRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorBannerRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorNewsRemoteDatasource
import org.purboyndradev.rt_rw.core.data.repository.ActivityRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.AuthRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.BannerRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.NewsRepositoryImpl
import org.purboyndradev.rt_rw.core.network.HttpClientFactory
import org.purboyndradev.rt_rw.core.network.TokenRefresher
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.domain.repository.BannerRepository
import org.purboyndradev.rt_rw.domain.repository.NewsRepository
import org.purboyndradev.rt_rw.domain.usecases.CreateActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.DeleteActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.EditActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivitiesUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivityByIdUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchAllBannersUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchAllNewsUseCase
import org.purboyndradev.rt_rw.domain.usecases.JoinActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase
import org.purboyndradev.rt_rw.domain.usecases.SignInUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyOtpUseCase
import org.purboyndradev.rt_rw.features.activity.presentation.ActivityViewModel
import org.purboyndradev.rt_rw.features.auth.presentation.AuthViewModel
import org.purboyndradev.rt_rw.features.main.presentation.MainViewModel
import org.purboyndradev.rt_rw.features.notification.NotificationViewModel
import org.purboyndradev.rt_rw.features.splash.SplashViewModel

fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)
    modules(
        platformModule,
        sharedModule
    )
}

expect val platformModule: Module

val sharedModule: Module = module {

    /// PROVIDE HTTP CLIENT FACTORY
    val authHttpClient = named("AuthHttpClient")
    single<HttpClient>(qualifier = authHttpClient) {
        HttpClient(get()) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            defaultRequest {
                url(PlatformConfig.baseUrl)
                contentType(ContentType.Application.Json)
            }
        }
    }

    single<HttpClient> {
        HttpClientFactory.create(get(), get(), get(), get())
    }

    /// PROVIDE OBJECT KEYS DATASTORE
    single<AuthKeys> {
        AuthKeys
    }

    /// PROVIDE REMOTE DATA SOURCE
    single<AuthApi> {
        KtorAuthRemoteDatasource(get(qualifier = authHttpClient))
    }
    single<ActivityApi> {
        KtorActivityRemoteDatasource(get())
    }
    single<BannerApi> {
        KtorBannerRemoteDatasource(get())
    }
    single<NewsApi> {
        KtorNewsRemoteDatasource(get())
    }

    /// PROVIDE REPOSITORY
    single<AuthRepository> {
        AuthRepositoryImpl(get(), get(), get())
    }
    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    single<BannerRepository> {
        BannerRepositoryImpl(get())
    }
    single<NewsRepository> {
        NewsRepositoryImpl(get())
    }

    /// PROVIDE USE CASE
    single {
        NotificationRepository(get())
    }

    /// AUTH USE CASE
    single {
        VerifyOtpUseCase(get())
    }
    single {
        RefreshTokenUseCase(get())
    }
    single {
        SignInUseCase(get())
    }

    /// ACTIVITY USE CASE
    single {
        CreateActivityUseCase(get())
    }
    single {
        DeleteActivityUseCase(get())
    }
    single {
        EditActivityUseCase(get())
    }
    single {
        FetchActivitiesUseCase(get())
    }
    single {
        FetchActivityByIdUseCase(get())
    }
    single {
        JoinActivityUseCase(get())
    }

    /// BANNER USE CASE
    single {
        FetchAllBannersUseCase(get())
    }

    /// NEWS USE CASE
    single {
        FetchAllNewsUseCase(get())
    }

    /// PROVIDE APP AUTH DATA STORE
    single {
        createDataStore()
    }

    single {
        AppAuthRepository(get())
    }

    single {
        AuthTokenStore(get())
    }

    single {
        TokenRefresher(get(), get())
    }

    /// PROVIDE VIEW MODEL
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { params ->
        ActivityViewModel(get(), get(), get(), get(), get(), get(), get())
    }
}
