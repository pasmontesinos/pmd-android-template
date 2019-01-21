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

import com.pasmodev.domain.repository.AuthRepository
import com.pasmodev.domain.repository.UserRepository
import com.pasmodev.domain.usecase.Logout
import com.pasmodev.domain.usecase.LogoutImpl
import com.pasmodev.domain.usecase.SubscribeToCurrentUser
import com.pasmodev.domain.usecase.SubscribeToCurrentUserImpl
import com.pasmodev.presentation.dto.mapper.UserDtoToUserMapper
import com.pasmodev.presentation.dto.mapper.UserDtoToUserMapperImpl
import com.pasmodev.presentation.presenter.MainPresenter
import com.pasmodev.presentation.presenter.MainPresenterImpl
import com.pasmodev.presentation.view.MainView
import dagger.Module
import dagger.Provides

@Module
class MainModule(val view: MainView) {

    @Provides fun provideSignupView() = view

    @Provides fun provideMainPresenter(view: MainView, logout: Logout, subscribeToCurrentUser: SubscribeToCurrentUser, userDtoToUserMapper: UserDtoToUserMapper) : MainPresenter {
        return MainPresenterImpl(view, logout, subscribeToCurrentUser, userDtoToUserMapper)
    }

    @Provides fun provideLogout(authRepository: AuthRepository): Logout {
        return LogoutImpl(authRepository)
    }

    @Provides fun provideSubscribeToCurrentUser(authRepository: AuthRepository, userRepository: UserRepository): SubscribeToCurrentUser {
        return SubscribeToCurrentUserImpl(authRepository, userRepository)
    }

    @Provides fun provideUserDtoToUserMapper(): UserDtoToUserMapper {
        return UserDtoToUserMapperImpl()
    }

}
