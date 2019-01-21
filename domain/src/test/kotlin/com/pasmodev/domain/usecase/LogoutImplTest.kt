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
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as _when

class LogoutImplTest {

    @Mock lateinit var authRepository: AuthRepository

    private lateinit var logout: Logout


    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

        logout = LogoutImpl(authRepository)
    }

    @Test
    fun `given user is not logged when logout then failure by AuthRequiredException`() {
        _when(authRepository.logged).thenReturn(false)

        logout.invoke(){ error ->
            MatcherAssert.assertThat(error?.cause, CoreMatchers.instanceOf(AuthRequiredException::class.java))
        }
    }

    @Test
    fun `given user is logged when logout then success`() {
        _when(authRepository.logged).thenReturn(true)
        _when(authRepository.logout(any()))
                .thenAnswer { invocation ->
                    val callback: (error: Error?) -> Unit = invocation.getArgument<Any>(0) as (Error?) -> Unit
                    callback(null)
                }

        var completedWithoutError: Boolean = false

        logout.invoke(){ error -> completedWithoutError = error == null }

        assertThat(completedWithoutError, equalTo(true))
    }

    @Test
    fun `when logout error then error`() {
        _when(authRepository.logged).thenReturn(true)
        _when(authRepository.logout(any()))
                .thenAnswer { invocation ->
                    val callback: (error: Error?) -> Unit = invocation.getArgument<Any>(0) as (Error?) -> Unit
                    callback(Error("Test error"))
                }

        logout.invoke(){ error -> Assert.assertNotNull(error)}
    }
}