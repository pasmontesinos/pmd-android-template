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

package com.pasmodev.presentation.presenter

import com.pasmodev.domain.usecase.Signup
import com.pasmodev.presentation.dto.mapper.SignupUserDtoToCredentialMapper
import com.pasmodev.presentation.dto.mapper.SignupUserDtoToUserMapper
import com.pasmodev.presentation.dto.validator.SignupUserDtoValidator
import com.pasmodev.presentation.view.SignupView

class SignupPresenterImpl(var view: SignupView, var signup: Signup, var signupUserDtoValidator: SignupUserDtoValidator, var signupUserDtoToUserMapper: SignupUserDtoToUserMapper, var signupUserDtoToCredentialMapper: SignupUserDtoToCredentialMapper) : SignupPresenter {

    override fun onCreate() {
        view.showIndeterminateProgress(false)
        view.enableInputs(true)
    }

    override fun onSignupAction() {
        view.enableInputs(false)
        view.showIndeterminateProgress(true)

        val signupUserDto = view.getSignupUserDtoInput()

        signupUserDtoValidator.getErrors(signupUserDto)?.let {
            view.showSignupUserDtoInputErrors(it)
            view.showIndeterminateProgress(false)
            view.enableInputs(true)
            return
        }

        signup(signupUserDtoToUserMapper.map(signupUserDto), signupUserDtoToCredentialMapper.map(signupUserDto)) { user, error ->
            view.showIndeterminateProgress(false)
            view.enableInputs(true)

            error?.let {
                view.showError(error.message ?: "Unknown")
            } ?: user?.let{
                if (it.verified)
                    view.navigateToMain()
                else
                    view.navigateToEmailVerification()
            }
        }
    }
}