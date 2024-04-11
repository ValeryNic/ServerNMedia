package ru.netology.nmedia.activity

import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ActivityModule {
    @Singleton
    @Provides
    fun provideFirebaseInstallations() = FirebaseInstallations.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseMessaging() = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun googleApiAvailability() = GoogleApiAvailability.getInstance()
}