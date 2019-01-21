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

package com.pasmodev.domain.usecase

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.pasmodev.domain.exception.UserNotFoundException
import com.pasmodev.domain.model.Credential
import com.pasmodev.domain.model.User
import com.pasmodev.domain.repository.AuthRepository
import com.pasmodev.domain.repository.UserRepository
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as _when

class LoginWithCredentialImplTest {

    @Mock lateinit var userFromAuth: User
    @Mock lateinit var userFromRepository: User
    @Mock lateinit var authRepository: AuthRepository
    @Mock lateinit var userRepository: UserRepository
    @Mock lateinit var credential: Credential

    private lateinit var loginWithCredential: LoginWithCredential

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        loginWithCredential = LoginWithCredentialImpl(authRepository, userRepository)
        _when(userFromRepository.id).thenReturn("xxx")
        _when(userFromAuth.id).thenReturn("xxx")
    }

    @Test
    fun `when exists user with credential return user from user repository`() {
        existsUser(true)

        loginWithCredential.invoke(credential) { user, error ->
            Assert.assertNotNull(user)
            Assert.assertNull(error)
        }

        verify(userRepository).getOne(any(), any())
    }

    @Test
    fun `given exists user with credential and user is not verified in user repository but yes in auth repository when login with credential then update user repository`() {
        existsUser(true)
        _when(userFromAuth.verified).thenReturn(true)
        _when(userFromRepository.verified).thenReturn(false)

        loginWithCredential.invoke(credential) {_, _ -> Unit }

        verify(userRepository).save(any(), any())
    }


    private fun existsUser(exists: Boolean) {
        _when(authRepository.loginWithCredential(any(), any()))
                .thenAnswer { invocation ->
                    val callback: (user: User?, error: Error?) -> Unit = invocation.getArgument<Any>(1) as (User?, Error?) -> Unit

                    if (exists)
                        callback(userFromAuth, null)
                    else
                        callback(null, Error("TestError", UserNotFoundException("TestException")))
                }

        _when(userRepository.getOne(any(), any()))
                .thenAnswer { invocation ->
                    val callback: (user: User?, error: Error?) -> Unit = invocation.getArgument<Any>(1) as (User?, Error?) -> Unit

                    if (exists)
                        callback(userFromRepository, null)
                    else
                        callback(null, Error("TestError", UserNotFoundException("TestException")))
                }
    }

    @Test
    fun `when not exists user with credential then return UserNotFoundException`() {
        existsUser(false)

        loginWithCredential.invoke(credential) { user, error ->
            Assert.assertNull(user)
            assertThat(error?.cause, instanceOf(UserNotFoundException::class.java))
        }
    }

}