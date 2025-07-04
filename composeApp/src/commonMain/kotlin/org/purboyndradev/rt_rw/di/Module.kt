package org.purboyndradev.rt_rw.di

import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.purboyndradev.rt_rw.ContextFactory
import org.purboyndradev.rt_rw.core.data.datastore.UserRepository
import org.purboyndradev.rt_rw.core.data.remote.api.AuthApi
import org.purboyndradev.rt_rw.core.data.remote.impl.KtorAuthRemoteDatasource
import org.purboyndradev.rt_rw.core.data.repository.AuthRepositoryImpl
import org.purboyndradev.rt_rw.core.network.HttpClientFactory
import org.purboyndradev.rt_rw.domain.repository.AuthRepository
import org.purboyndradev.rt_rw.domain.usecases.SignInUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyOtpUseCase
import org.purboyndradev.rt_rw.features.auth.presentation.AuthViewModel
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
    
    /// PROVIDE REPOSITORY
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }
    
    /// PROVIDE USE CASE
    single {
        SignInUseCase(get())
    }
    
    single {
        VerifyOtpUseCase(get())
    }
    
    single {
        UserRepository()
    }
    
    /// PROVIDE VIEW MODEL
    viewModel { AuthViewModel(get(), get(), get()) }
}
