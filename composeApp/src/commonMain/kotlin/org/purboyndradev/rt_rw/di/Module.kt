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
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.datastore.UserRepository
import org.purboyndradev.rt_rw.core.data.remote.api.ActivityApi
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorActivityRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorAuthRemoteDatasource
import org.purboyndradev.rt_rw.core.data.repository.ActivityRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.AuthRepositoryImpl
import org.purboyndradev.rt_rw.core.network.HttpClientFactory
import org.purboyndradev.rt_rw.database.createCurrentUserDataStore
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.domain.usecases.CreateActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.DeleteActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.EditActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivitiesUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivityByIdUseCase
import org.purboyndradev.rt_rw.domain.usecases.JoinActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase
import org.purboyndradev.rt_rw.domain.usecases.SignInUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyOtpUseCase
import org.purboyndradev.rt_rw.features.activity.presentation.ActivityViewModel
import org.purboyndradev.rt_rw.features.auth.presentation.AuthViewModel
import org.purboyndradev.rt_rw.features.main.presentation.MainViewModel
import org.purboyndradev.rt_rw.features.splash.SplashViewModel
import org.purboyndradev.rt_rw.helper.BASE_URL

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
                contentType(ContentType.Application.Json)
            }
        }
    }
    
    single<HttpClient> {
        HttpClientFactory.create(get(), get(), get())
    }
    
    single<String>(qualifier = named("baseUrl")) { BASE_URL }
    
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
    
    /// PROVIDE REPOSITORY
    single<AuthRepository> {
        AuthRepositoryImpl(get(), get())
    }
    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    
    /// PROVIDE USE CASE
    
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
    
    /// PROVIDE DATA STORE
    single {
        createCurrentUserDataStore()
    }
    
    /// PROVIDE APP AUTH DATA STORE
    single {
        createDataStore()
    }
    
    single {
        UserRepository(get())
    }
    
    single {
        AppAuthRepository(get())
    }
    
    /// PROVIDE VIEW MODEL
    viewModel { AuthViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { params ->
        ActivityViewModel(get(), get(), get(), get(), get(), get())
    }
}
