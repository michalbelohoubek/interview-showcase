package cz.michalbelohoubek.interviewshowcase.common.network.di

import cz.michalbelohoubek.interviewshowcase.common.network.Dispatcher
import cz.michalbelohoubek.interviewshowcase.common.network.STDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesScopesModule {

    @Singleton
    @ApplicationScope
    @Provides
    fun providesCoroutineScope(
        @Dispatcher(STDispatchers.IO) defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
}
