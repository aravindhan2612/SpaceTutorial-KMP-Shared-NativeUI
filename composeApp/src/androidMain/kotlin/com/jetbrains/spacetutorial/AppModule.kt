package com.jetbrains.spacetutorial

import com.jetbrains.spacetutorial.cache.AndroidDatabaseDriveFactory
import com.jetbrains.spacetutorial.network.SpaceXApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module


val appModule = module {
    single<SpaceXApi> { SpaceXApi() }
    single<SpaceXSDK> {
        SpaceXSDK(
            databaseDriverFactory = AndroidDatabaseDriveFactory(
                androidContext()
            ),
            api = get()
        )
    }
    viewModel { RocketLaunchViewModel(sdk = get()) }
}