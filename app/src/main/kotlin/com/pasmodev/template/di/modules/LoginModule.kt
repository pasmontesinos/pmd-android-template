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
import com.pasmodev.domain.usecase.LoginWithCredential
import com.pasmodev.domain.usecase.LoginWithCredentialImpl
import com.pasmodev.domain.usecase.RememberLogin
import com.pasmodev.domain.usecase.RememberLoginImpl
import com.pasmodev.presentation.dto.mapper.CredentialDtoToCredentialMapper
import com.pasmodev.presentation.dto.mapper.CredentialDtoToCredentialMapperImpl
import com.pasmodev.presentation.dto.validator.CredentialDtoValidator
import com.pasmodev.presentation.dto.validator.CredentialDtoValidatorImpl
import com.pasmodev.presentation.presenter.LoginPresenter
import com.pasmodev.presentation.presenter.LoginPresenterImpl
import com.pasmodev.presentation.view.LoginView
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginModule(val view: LoginView) {

    @Provides fun provideLoginView() = view

    @Provides fun provideLoginPresenter(view: LoginView, rememberLogin: RememberLogin, loginWithCredential: LoginWithCredential, credentialDtoValidator: CredentialDtoValidator, credentialDtoToCredentialMapper: CredentialDtoToCredentialMapper): LoginPresenter {
        return LoginPresenterImpl(view, rememberLogin, loginWithCredential, credentialDtoValidator, credentialDtoToCredentialMapper)
    }

    @Provides fun provideRememberLogin(authRepository: AuthRepository): RememberLogin {
        return RememberLoginImpl(authRepository)
    }

    @Provides fun provideLoginWithCredential(authRepository: AuthRepository, userRepository: UserRepository): LoginWithCredential {
        return LoginWithCredentialImpl(authRepository, userRepository)
    }

    @Provides fun provideCredentialDtoToCredentialMapper(): CredentialDtoToCredentialMapper {
        return CredentialDtoToCredentialMapperImpl()
    }

    @Provides fun provideCredentialDtoValidator(): CredentialDtoValidator {
        return CredentialDtoValidatorImpl()
    }
}
