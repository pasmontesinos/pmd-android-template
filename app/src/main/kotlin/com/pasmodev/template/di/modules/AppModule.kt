/*
 *
 *  *
 *  *  * Copyright (c) 2019 Pascual Montesinos http://pasmodev.com
 *  *  *
 *  *  * Licensed to the Apache Software Foundation (ASF) under one
 *  *  * or more contributor license agreements.  See the NOTICE file
 *  *  * distributed with this work for additional information
 *  *  * regarding copyright ownership.  The ASF licenses this file
 *  *  * to you under the Apache License, Version 2.0 (the
 *  *  * "License"); you may not use this file except in compliance
 *  *  * with the License.  You may obtain a copy of the License at
 *  *  *
 *  *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing,
 *  *  * software distributed under the License is distributed on an
 *  *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  *  * KIND, either express or implied.  See the License for the
 *  *  * specific language governing permissions and limitations
 *  *  * under the License.
 *  *
 *
 */

package com.pasmodev.template.di.modules

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pasmodev.data.entity.mapper.CredentialEntityToCredentialMapper
import com.pasmodev.data.entity.mapper.CredentialEntityToCredentialMapperImpl
import com.pasmodev.data.entity.mapper.UserEntityToUserMapper
import com.pasmodev.data.entity.mapper.UserEntityToUserMapperImpl
import com.pasmodev.data.repository.AuthRepositoryImpl
import com.pasmodev.data.repository.UserRepositoryImpl
import com.pasmodev.data.repository.datasource.AuthDataSource
import com.pasmodev.data.repository.datasource.CacheDatasource
import com.pasmodev.data.repository.datasource.UserDataSource
import com.pasmodev.domain.repository.AuthRepository
import com.pasmodev.domain.repository.UserRepository
import com.pasmodev.template.App
import com.pasmodev.template.datasource.AuthDataSourceFirebase
import com.pasmodev.template.datasource.CacheDatasourceOnSharedPreferences
import com.pasmodev.template.datasource.UserDataSourceFirebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides @Singleton fun provideApp() = app

    @Provides @Singleton fun provideContext(): Context = app

    @Provides @Singleton fun provideResources(context: Context): Resources { return context.resources }

    @Provides @Singleton fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("preferences-00", Context.MODE_PRIVATE)
    }

    @Provides @Singleton fun provideCacheDatasource(sharedPreferences: SharedPreferences): CacheDatasource {
        return CacheDatasourceOnSharedPreferences(sharedPreferences)
    }

    @Provides @Singleton fun provideAuthRepository(authDataSource: AuthDataSource, credentialEntityToCredentialMapper: CredentialEntityToCredentialMapper, userEntityToUserMapper: UserEntityToUserMapper): AuthRepository {
        return AuthRepositoryImpl(authDataSource, credentialEntityToCredentialMapper, userEntityToUserMapper)
    }

    @Provides @Singleton fun provideAuthDataSource(firebaseAuth: FirebaseAuth): AuthDataSource {
        return AuthDataSourceFirebase(firebaseAuth)
    }

    @Provides @Singleton fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides @Singleton fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides @Singleton fun provideCredentialEntityToCredentialMapper(): CredentialEntityToCredentialMapper {
        return CredentialEntityToCredentialMapperImpl()
    }

    @Provides @Singleton fun provideUserEntityToUserMapper(): UserEntityToUserMapper {
        return UserEntityToUserMapperImpl()
    }

    @Provides @Singleton fun provideUserRepository(userDataSource: UserDataSource, userEntityToUserMapper: UserEntityToUserMapper): UserRepository {
        return UserRepositoryImpl(userDataSource, userEntityToUserMapper)
    }

    @Provides @Singleton fun provideUserDataSource(firebaseFirestore: FirebaseFirestore): UserDataSource {
        return UserDataSourceFirebase(firebaseFirestore)
    }
}
