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
import com.pasmodev.domain.exception.EmailAlreadyExistsException
import com.pasmodev.domain.model.Credential
import com.pasmodev.domain.model.User
import com.pasmodev.domain.repository.AuthRepository
import com.pasmodev.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class SignupImplTest {

    @Mock lateinit var credential: Credential
    @Mock lateinit var user: User
    @Mock lateinit var authRepository: AuthRepository
    @Mock lateinit var userRepository: UserRepository

    private lateinit var signup: Signup

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        signup = SignupImpl(authRepository, userRepository)
    }

    @Test
    fun `given email already exists when signup then failure by EmailAlreadyExists`() {
        emailAlreadyExists(true)

        var resultUser: User? = null
        var resultError: Error? = null

        runBlocking {
            signup.invoke(user, credential) { user, error ->
                resultUser = user
                resultError = error
            }
        }

        Assert.assertNull(resultUser)
        assertThat(resultError?.cause, instanceOf(EmailAlreadyExistsException::class.java))
    }

    @Test
    fun `given email not exists when signup then create user in auth repository and create user in user repository and return user`() {
        emailAlreadyExists(false)
        saveUser(true)

        var resultUser: User? = null
        var resultError: Error? = null

        runBlocking {
            signup.invoke(user, credential) { user, error ->
                resultUser = user
                resultError = error
            }
        }

        verify(authRepository).createUserWithCredential(any(), any(), any())
        verify(userRepository).save(any(), any())

        Assert.assertNotNull(resultUser)
        Assert.assertNull(resultError)
    }

    private fun emailAlreadyExists(exists: Boolean) {
        `when`(authRepository.createUserWithCredential(any(), any(), any()))
                .thenAnswer{
                    val callback = it.arguments[2] as (User?, Error?) -> Unit

                    if (exists)
                        callback(null, Error("TestError", EmailAlreadyExistsException("TestException")))
                    else
                        callback(user, null)

                }
    }

    private fun saveUser(success: Boolean){
        `when`(userRepository.save(any(), any()))
                .thenAnswer {
                    val callback = it.arguments[1] as (User?, Error?) -> Unit
                    if (success)
                        callback(user, null)
                    else
                        callback(null, Error("TestError", EmailAlreadyExistsException("TestException")))
                }
    }

}