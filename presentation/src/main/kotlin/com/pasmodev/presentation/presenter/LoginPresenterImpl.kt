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

import com.pasmodev.domain.usecase.LoginWithCredential
import com.pasmodev.domain.usecase.RememberLogin
import com.pasmodev.presentation.dto.mapper.CredentialDtoToCredentialMapper
import com.pasmodev.presentation.dto.validator.CredentialDtoValidator
import com.pasmodev.presentation.view.LoginView

class LoginPresenterImpl(private var view: LoginView, private var rememberLogin: RememberLogin, private var loginWithCredential: LoginWithCredential, private var credentialDtoValidator: CredentialDtoValidator, private var credentialDtoToCredentialMapper: CredentialDtoToCredentialMapper) : LoginPresenter {

    override fun onCreate() {
        view.enableInputs(false)
        view.showIndeterminateProgress(true)

        rememberLogin() { user, error ->
            view.showIndeterminateProgress(false)
            view.enableInputs(true)

            user?.let { loginSuccess(it.verified) }
        }
    }

    override fun onLoginWithCredentialAction() {
        view.enableInputs(false)
        view.showIndeterminateProgress(true)

        val credentialDto = view.getCredentialInput()

        credentialDtoValidator.getErrors(credentialDto)?.let {
            view.showCredentialInputErrors(it)
            view.showIndeterminateProgress(false)
            view.enableInputs(true)
            return
        }

        loginWithCredential(credentialDtoToCredentialMapper.map(credentialDto)) { user, error ->
            view.showIndeterminateProgress(false)
            view.enableInputs(true)

            error?.let {
                view.showError(error.message ?: "Unknown")
            } ?: user?.let{
                loginSuccess(it.verified)
            }
        }
    }

    private fun loginSuccess(verified: Boolean) {
        if (verified)
            view.navigateToMain()
        else
            view.navigateToEmailVerification()
    }

    override fun onNavigateToSignUpAction() {
        view.navigateToSignUp()
    }
}