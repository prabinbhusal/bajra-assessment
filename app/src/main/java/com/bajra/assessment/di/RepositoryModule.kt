package com.bajra.assessment.di

import com.google.firebase.auth.FirebaseAuth
import com.bajra.assessment.data.repository.AuthRepository
import com.bajra.assessment.data.repository.AuthRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
    ): AuthRepository {
        return AuthRepositoryImp(auth)
    }
}