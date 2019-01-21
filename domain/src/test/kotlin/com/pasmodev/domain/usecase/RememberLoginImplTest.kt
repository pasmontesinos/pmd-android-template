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
import com.pasmodev.domain.exception.AuthRequiredException
import com.pasmodev.domain.model.User
import com.pasmodev.domain.repository.AuthRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as _when

class RememberLoginImplTest {


    @Mock lateinit var authRepository: AuthRepository
    @Mock lateinit var user: User

    private lateinit var rememberLogin: RememberLogin


    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

        rememberLogin = RememberLoginImpl(authRepository)
    }

    @Test
    fun `given user is logged and verfied when remember login then return user`() {
        givenUserIsLogged(true)
        givenUser(true)

        rememberLogin.invoke { user, _ -> Assert.assertNotNull(user)  }
    }

    @Test
    fun `given user is logged and not verified when remember login then failure`() {
        givenUserIsLogged(false)
        givenUser(false)

        rememberLogin.invoke() { user, error ->
            MatcherAssert.assertThat(error?.cause, CoreMatchers.instanceOf(AuthRequiredException::class.java))
        }
    }

    @Test
    fun `given user is not logged when remember login then failure by AuthRequiredException`() {
        givenUserIsLogged(false)

        rememberLogin.invoke() { user, error ->
            MatcherAssert.assertThat(error?.cause, CoreMatchers.instanceOf(AuthRequiredException::class.java))
        }
    }

    private fun givenUser(verified: Boolean) {
        _when(user.verified).thenReturn(verified)
    }

    private fun givenUserIsLogged(logged: Boolean) {
        _when(authRepository.logged).thenReturn(logged)
        _when(authRepository.getCurrentUser(any()))
                .thenAnswer { invocation ->
                    val callback: (user: User?, error: Error?) -> Unit = invocation.getArgument<Any>(0) as (User?, Error?) -> Unit

                    if (logged)
                        callback(user, null)
                    else
                        callback(null, Error("TestError", AuthRequiredException("Test Error")))
                }
    }



}