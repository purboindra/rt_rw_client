package org.purboyndradev.rt_rw.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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
import org.purboyndradev.rt_rw.core.data.remote.api.DuesInvoiceApi
import org.purboyndradev.rt_rw.core.data.remote.api.NewsApi
import org.purboyndradev.rt_rw.core.data.remote.api.ReportApi
import org.purboyndradev.rt_rw.core.data.remote.api.UserApi
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorActivityRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorAuthRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorBannerRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorDuesInvoiceRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorNewsRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorReportRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorUserRemoteDatasource
import org.purboyndradev.rt_rw.core.data.repository.ActivityRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.AuthRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.BannerRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.DuesInvoiceRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.NewsRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.ReportRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.UserRepositoryImpl
import org.purboyndradev.rt_rw.core.network.HttpClientFactory
import org.purboyndradev.rt_rw.core.network.TokenRefresher
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.domain.repository.BannerRepository
import org.purboyndradev.rt_rw.domain.repository.DuesInvoiceRepository
import org.purboyndradev.rt_rw.domain.repository.NewsRepository
import org.purboyndradev.rt_rw.domain.repository.ReportRepository
import org.purboyndradev.rt_rw.domain.repository.UserRepository
import org.purboyndradev.rt_rw.domain.usecases.CreateActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.CreateReportUseCase
import org.purboyndradev.rt_rw.domain.usecases.DeleteActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.EditActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivitiesUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivityByIdUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchAllBannersUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchAllNewsUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchAllReportsUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchCurrentUserUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchDuesInvoicesUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchNewsByIdUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchReportByIdUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchUsersActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.JoinActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase
import org.purboyndradev.rt_rw.domain.usecases.RequestEmailVerificationUseCase
import org.purboyndradev.rt_rw.domain.usecases.SignInUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyEmailUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyOtpUseCase
import org.purboyndradev.rt_rw.features.activity.presentation.ActivityViewModel
import org.purboyndradev.rt_rw.features.auth.presentation.AuthViewModel
import org.purboyndradev.rt_rw.features.main.presentation.MainViewModel
import org.purboyndradev.rt_rw.features.news.presentation.NewsViewModel
import org.purboyndradev.rt_rw.features.notification.NotificationViewModel
import org.purboyndradev.rt_rw.features.profile.presentation.ProfileViewModel
import org.purboyndradev.rt_rw.features.report.presentation.ReportViewModel
import org.purboyndradev.rt_rw.features.splash.SplashViewModel
import co.touchlab.kermit.Logger as KermitLogger

fun initKoin(config: KoinAppDeclaration? = null) {
    val app = startKoin {
        config?.invoke(this)
        modules(
            platformModule,
            sharedModule
        )
    }

    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    appScope.launch {
        val appAuthRepository: AppAuthRepository = app.koin.get()
        val tokenStore: AuthTokenStore = app.koin.get()
        val tokenRefresher: TokenRefresher = app.koin.get()

        val httpClient = HttpClientFactory.create(
            engine = CIO.create(),
            appAuthRepository = appAuthRepository,
            tokenRefresher = tokenRefresher,
            tokenStore = tokenStore
        )

        InitializedHttpClient.client = httpClient
        KermitLogger.i("initKoin") { "Authenticated HttpClient has been asynchronously initialized." }
    }
}

expect val platformModule: Module

object InitializedHttpClient {
    var client: HttpClient? = null
}

val sharedModule: Module = module {


    /// PROVIDE HTTP CLIENT FACTORY
    val defaultHttpClient = named("DefaultHttpClient")
    val authHttpClient = named("AuthHttpClient")

    single<HttpClient>(qualifier = defaultHttpClient) {
        InitializedHttpClient.client ?: error("Authenticated HttpClient is not yet initialized.")
    }

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
        TokenRefresher(get(qualifier = authHttpClient), get())
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
        KtorActivityRemoteDatasource(get(qualifier = defaultHttpClient))
    }
    single<BannerApi> {
        KtorBannerRemoteDatasource(get(qualifier = defaultHttpClient))
    }
    single<NewsApi> {
        KtorNewsRemoteDatasource(get(qualifier = defaultHttpClient))
    }
    single<ReportApi> {
        KtorReportRemoteDatasource(get(qualifier = defaultHttpClient))
    }
    single<UserApi> {
        KtorUserRemoteDatasource(get(qualifier = defaultHttpClient))
    }
    single<DuesInvoiceApi> {
        KtorDuesInvoiceRemoteDatasource(get(qualifier = defaultHttpClient))
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
    single<ReportRepository> {
        ReportRepositoryImpl(get())
    }
    single<UserRepository> {
        UserRepositoryImpl(get(), get())
    }
    single<DuesInvoiceRepository> {
        DuesInvoiceRepositoryImpl(get())
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
    single {
        FetchUsersActivityUseCase(get())
    }

    /// BANNER USE CASE
    single {
        FetchAllBannersUseCase(get())
    }

    /// NEWS USE CASE
    single {
        FetchAllNewsUseCase(get())
    }
    single {
        FetchNewsByIdUseCase(get())
    }

    /// REPORT USE CASE
    single {
        CreateReportUseCase(get())
    }
    single {
        FetchAllReportsUseCase(get())
    }
    single {
        FetchReportByIdUseCase(get())
    }

    /// USER USE CASE
    single {
        RequestEmailVerificationUseCase(get())
    }
    single {
        VerifyEmailUseCase(get())
    }
    single {
        FetchCurrentUserUseCase(get())
    }

    /// DUES INVOICE USE CASE
    single {
        FetchDuesInvoicesUseCase(get())
    }


    /// PROVIDE VIEW MODEL
    viewModel { AuthViewModel(get(), get(), get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { params ->
        ActivityViewModel(get(), get(), get(), get(), get(), get(), get(), get())
    }
    viewModel {
        NewsViewModel(
            get(), get()
        )
    }
    viewModel {
        ReportViewModel(
            get(),
            get(), get()
        )
    }
    viewModel {
        ProfileViewModel(
            get(),
            get()
        )
    }
}
