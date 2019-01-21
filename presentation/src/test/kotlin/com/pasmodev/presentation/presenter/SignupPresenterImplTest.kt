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
import com.nhaarman.mockitokotlin2.verify
import com.pasmodev.domain.model.Credential
import com.pasmodev.domain.model.User
import com.pasmodev.domain.usecase.Signup
import com.pasmodev.presentation.dto.SignupUserDto
import com.pasmodev.presentation.dto.mapper.CredentialDtoToCredentialMapper
import com.pasmodev.presentation.dto.mapper.SignupUserDtoToCredentialMapper
import com.pasmodev.presentation.dto.mapper.SignupUserDtoToUserMapper
import com.pasmodev.presentation.dto.validator.SignupUserDtoValidator
import com.pasmodev.presentation.view.SignupView
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as _when

class SignupPresenterImplTest {

    @Mock lateinit var view: SignupView
    @Mock lateinit var signupUserDtoValidator: SignupUserDtoValidator
    @Mock lateinit var signup: Signup
    @Mock lateinit var signupUserDtoToUserMapper: SignupUserDtoToUserMapper
    @Mock lateinit var signupUserDtoToCredentialMapper: SignupUserDtoToCredentialMapper
    @Mock lateinit var signupUserDto: SignupUserDto
    @Mock lateinit var user: User
    @Mock lateinit var credential: Credential

    lateinit var presenter: SignupPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        _when(view.getSignupUserDtoInput()).thenReturn(signupUserDto)
        _when(signupUserDtoToUserMapper.map(any())).thenReturn(user)
        _when(signupUserDtoToCredentialMapper.map(any())).thenReturn(credential)
        _when(signupUserDtoValidator.getErrors(any())).thenReturn(null)

        presenter = SignupPresenterImpl(view, signup, signupUserDtoValidator, signupUserDtoToUserMapper, signupUserDtoToCredentialMapper)
    }

    @Test
    fun `test when signup action then get signup user from view`(){
        presenter.onSignupAction()

        verify(view).getSignupUserDtoInput()
    }

    @Test
    fun `test when signup action then call to signup usecase`(){
        presenter.onSignupAction()

        verify(signup).invoke(any(), any(), any())
    }

    @Test
    fun `test when signup success and user is verified then navigate to main`() {
        givenSignup(true)
        givenUser(true)

        presenter.onSignupAction()

        verify(view).navigateToMain()
    }

    @Test
    fun `test when signup success and user is not verified then navigate to email verification`() {
        givenSignup(true)
        givenUser(false)

        presenter.onSignupAction()

        verify(view).navigateToEmailVerification()
    }

    @Test
    fun `test when signup failure then show error`(){
        givenSignup(false)

        presenter.onSignupAction()

        verify(view).showError(any())
    }

    @Test
    fun `test given signup user input invalid when signup action then show signup user input errors`() {
        _when(signupUserDtoValidator.getErrors(any())).thenReturn(mutableMapOf("email" to "Is required"))

        presenter.onSignupAction()

        verify(view).showSignupUserDtoInputErrors(any())
    }

    private fun givenSignup(success: Boolean) {
        _when(signup(any(), any(), any()))
                .thenAnswer { invocation ->
                    val callback: (user: User?, error: Error?) -> Unit = invocation.getArgument<Any>(2) as (User?, Error?) -> Unit

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