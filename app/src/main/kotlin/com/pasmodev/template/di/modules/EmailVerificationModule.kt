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
import com.pasmodev.domain.usecase.SubscribeToEmailVerificationForCurrentUser
import com.pasmodev.domain.usecase.SubscribeToEmailVerificationForCurrentUserImpl
import com.pasmodev.presentation.presenter.EmailVerificationPresenter
import com.pasmodev.presentation.presenter.EmailVerificationPresenterImpl
import com.pasmodev.presentation.view.EmailVerificationView
import dagger.Module
import dagger.Provides

@Module
class EmailVerificationModule(val view: EmailVerificationView) {

    @Provides fun provideEmailVerificationView() = view

    @Provides fun provideEmailVerificationPresenter(view: EmailVerificationView, subscribeToEmailVerificationForCurrentUser: SubscribeToEmailVerificationForCurrentUser): EmailVerificationPresenter {
        return EmailVerificationPresenterImpl(view, subscribeToEmailVerificationForCurrentUser)
    }

    @Provides fun provideSubscribeToEmailVerificationForCurrentUser(authRepository: AuthRepository, userRepository: UserRepository): SubscribeToEmailVerificationForCurrentUser {
        return SubscribeToEmailVerificationForCurrentUserImpl(authRepository, userRepository)
    }

}
