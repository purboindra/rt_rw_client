package org.purboyndradev.rt_rw.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.purboyndradev.rt_rw.core.data.datastore.UserDataStore
import org.purboyndradev.rt_rw.core.data.datastore.UserRepository
import org.purboyndradev.rt_rw.core.data.remote.api.ActivityApi
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorActivityRemoteDatasource
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorAuthRemoteDatasource
import org.purboyndradev.rt_rw.core.data.repository.ActivityRepositoryImpl
import org.purboyndradev.rt_rw.core.data.repository.AuthRepositoryImpl
import org.purboyndradev.rt_rw.core.network.HttpClientFactory
import org.purboyndradev.rt_rw.database.createCurrentUserDataStore
import org.purboyndradev.rt_rw.database.createPreferencesDataStore
import org.purboyndradev.rt_rw.domain.repository.ActivityRepository
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.domain.usecases.CreateActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.DeleteActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.EditActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivitiesUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivityByIdUseCase
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase
import org.purboyndradev.rt_rw.domain.usecases.SignInUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyOtpUseCase
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
    
    single<String>(qualifier = named("baseUrl")) { BASE_URL }
    
    single<HttpClient> {
        HttpClientFactory.create(get())
    }
    
    /// PROVIDE REMOTE DATA SOURCE
    single<AuthApi> {
        KtorAuthRemoteDatasource(get())
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
    single {
        SignInUseCase(get())
    }
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
        VerifyOtpUseCase(get())
    }
    single {
        RefreshTokenUseCase(get())
    }
    
    /// PROVIDE DATA STORE
    single {
        createCurrentUserDataStore()
    }
    
    single {
        UserRepository(get())
    }

//    single<DataStore<Preferences>> { createPreferencesDataStore() }
    
    /// PROVIDE VIEW MODEL
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
}
