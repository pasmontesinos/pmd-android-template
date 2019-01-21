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
import com.pasmodev.domain.usecase.Signup
import com.pasmodev.domain.usecase.SignupImpl
import com.pasmodev.presentation.dto.mapper.SignupUserDtoToCredentialMapper
import com.pasmodev.presentation.dto.mapper.SignupUserDtoToCredentialMapperImpl
import com.pasmodev.presentation.dto.mapper.SignupUserDtoToUserMapper
import com.pasmodev.presentation.dto.mapper.SignupUserDtoToUserMapperImpl
import com.pasmodev.presentation.dto.validator.SignupUserDtoValidator
import com.pasmodev.presentation.dto.validator.SignupUserDtoValidatorImpl
import com.pasmodev.presentation.presenter.SignupPresenter
import com.pasmodev.presentation.presenter.SignupPresenterImpl
import com.pasmodev.presentation.view.SignupView
import dagger.Module
import dagger.Provides

@Module
class SignupModule(val view: SignupView) {

    @Provides fun provideSignupView() = view

    @Provides fun provideSignupPresenter(signup: Signup, signupUserDtoValidator: SignupUserDtoValidator, signupUserDtoToUserMapper: SignupUserDtoToUserMapper, signupUserDtoToCredentialMapper: SignupUserDtoToCredentialMapper): SignupPresenter {
        return SignupPresenterImpl(view, signup, signupUserDtoValidator, signupUserDtoToUserMapper, signupUserDtoToCredentialMapper)
    }

    @Provides fun provideSignup(authRepository: AuthRepository, userRepository: UserRepository): Signup {
        return SignupImpl(authRepository, userRepository)
    }

    @Provides fun provideSignupUserDtoToUserMapper(): SignupUserDtoToUserMapper {
        return SignupUserDtoToUserMapperImpl()
    }

    @Provides fun provideSignupUserDtoToCredentialMapper(): SignupUserDtoToCredentialMapper {
        return SignupUserDtoToCredentialMapperImpl()
    }

    @Provides fun provideSignupUserDtoValidator(): SignupUserDtoValidator {
        return SignupUserDtoValidatorImpl()
    }
}
