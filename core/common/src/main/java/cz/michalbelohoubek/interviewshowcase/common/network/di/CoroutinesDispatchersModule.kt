package cz.michalbelohoubek.interviewshowcase.common.network.di

import cz.michalbelohoubek.interviewshowcase.common.network.Dispatcher
import cz.michalbelohoubek.interviewshowcase.common.network.STDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesDispatchersModule {
    @Provides
    @Dispatcher(STDispatchers.IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(STDispatchers.DEFAULT)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(STDispatchers.MAIN)
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Dispatcher(STDispatchers.MAIN_IMMEDIATE)
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate


    @Provides
    fun providesIOCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}
