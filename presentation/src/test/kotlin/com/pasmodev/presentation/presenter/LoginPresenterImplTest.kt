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

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.pasmodev.domain.model.Credential
import com.pasmodev.domain.model.User
import com.pasmodev.domain.usecase.LoginWithCredential
import com.pasmodev.domain.usecase.RememberLogin
import com.pasmodev.presentation.dto.CredentialDto
import com.pasmodev.presentation.dto.mapper.CredentialDtoToCredentialMapper
import com.pasmodev.presentation.dto.validator.CredentialDtoValidator
import com.pasmodev.presentation.view.LoginView
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as _when

class LoginPresenterImplTest {

    @Mock lateinit var view: LoginView
    @Mock lateinit var credentialDtoValidator: CredentialDtoValidator
    @Mock lateinit var loginWithCredential: LoginWithCredential
    @Mock lateinit var rememberLogin: RememberLogin
    @Mock lateinit var credentialDtoToCredentialMapper: CredentialDtoToCredentialMapper
    @Mock lateinit var credentialDto: CredentialDto
    @Mock lateinit var credential: Credential
    @Mock lateinit var user: User

    lateinit var presenter: LoginPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        _when(view.getCredentialInput()).thenReturn(credentialDto)
        _when(credentialDtoToCredentialMapper.map(any())).thenReturn(credential)
        _when(credentialDtoValidator.getErrors(any())).thenReturn(null)

        presenter = LoginPresenterImpl(view, rememberLogin, loginWithCredential, credentialDtoValidator, credentialDtoToCredentialMapper)
    }

    @Test
    fun `test when login action then get credential from view`(){
        presenter.onLoginWithCredentialAction()

        verify(view).getCredentialInput()
    }

    @Test
    fun `test when create then call to remember login usecase`(){
        presenter.onCreate()

        verify(rememberLogin).invoke(any())
    }

    @Test
    fun `test when login action then call to LoginWithCredential usecase`(){
        presenter.onLoginWithCredentialAction()

        verify(loginWithCredential).invoke(any(), any())
    }

    @Test
    fun `test given login success and user is verified when login action then navigate to main`() {
        givenLogin(true)
        givenUser(true)

        presenter.onLoginWithCredentialAction()

        verify(view).navigateToMain()
    }

    @Test
    fun `test given login success and user is not verified when login action then navigate to email verification`(){
        givenLogin(true)
        givenUser(false)

        presenter.onLoginWithCredentialAction()

        verify(view).navigateToEmailVerification()
    }

    @Test
    fun `test given login failure when login action then show error`(){
        givenLogin(false)

        presenter.onLoginWithCredentialAction()

        verify(view).showError(any())
    }

    @Test
    fun `test given credential input invalid when login action then show credential input errors`() {
        _when(credentialDtoValidator.getErrors(any())).thenReturn(mutableMapOf("email" to "Is required"))

        presenter.onLoginWithCredentialAction()

        verify(view).showCredentialInputErrors(any())
    }

    @Test
    fun `test when navigate to sign up action then navigate to sign up`(){

        presenter.onNavigateToSignUpAction()

        verify(view).navigateToSignUp()
    }

    @Test
    fun `given remember login success when create then navigate to main`() {
        givenRememberUser(true)
        givenUser(true)

        presenter.onCreate()

        verify(view).navigateToMain()
    }

    @Test
    fun `given remember user failure when create then user stays in login`() {
        givenRememberUser(false)

        presenter.onCreate()

        verify(view, never()).navigateToMain()
        verify(view, never()).navigateToEmailVerification()
    }

    private fun givenRememberUser(remember: Boolean) {
        _when(rememberLogin(any()))
                .thenAnswer { invocation ->
                    val callback: (user: User?, error: Error?) -> Unit = invocation.getArgument<Any>(0) as (User?, Error?) -> Unit

                    if (remember)
                        callback(user, null)
                    else
                        callback(null, Error("TestError"))
                }
    }

    private fun givenLogin(success: Boolean) {
        _when(loginWithCredential(any(), any()))
                .thenAnswer { invocation ->
                    val callback: (user: User?, error: Error?) -> Unit = invocation.getArgument<Any>(1) as (User?, Error?) -> Unit

                    if (success)
                        callback(user, null)
                    else
                        callback(null, Error("TestError"))
                }
    }

    private fun givenUser(verified: Boolean) {
        _when(user.verified).thenReturn(verified)
    }
}