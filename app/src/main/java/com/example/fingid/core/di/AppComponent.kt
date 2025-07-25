package com.example.fingid.core.di

import android.app.Application
import com.example.fingid.App
import com.example.fingid.data.local.di.DatabaseModule
import com.example.fingid.worker.SyncWorker
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        DataSourceModule::class,
        ViewModelModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {

    fun inject(app: App)

    fun inject(worker: SyncWorker)

    fun activityComponent(): ActivityComponent.Factory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}