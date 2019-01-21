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
import com.pasmodev.domain.exception.AuthRequiredException
import com.pasmodev.domain.model.User
import com.pasmodev.domain.repository.AuthRepository
import com.pasmodev.domain.repository.UserRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*
import org.mockito.Mockito.`when` as _when

class SubscribeToEmailVerificationForCurrentUserImplTest {


    //TODO: there is not posible mock lambda and return in method authRepository.subscribeToCurrentUser  REVIEW
    @Mock lateinit var authRepository : AuthRepository
    @Mock lateinit var userRepository : UserRepository
    @Mock lateinit var user: User

    private lateinit var subscribeToEmailVerificationForCurrentUser : SubscribeToEmailVerificationForCurrentUser

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        _when(authRepository.subscribeToCurrentUser(any()))
                .thenAnswer { invocation ->
                    val callback: (user: User?, error: Error?) -> Unit = invocation.getArgument<Any>(0) as (User?, Error?) -> Unit
                    callback(user, null)
                }

        subscribeToEmailVerificationForCurrentUser = SubscribeToEmailVerificationForCurrentUserImpl(authRepository, userRepository)
    }

    @Test
    fun `given user is not logged when subscribe then failure by AuthRequiredException`() {
        _when(authRepository.logged).thenReturn(false)

        subscribeToEmailVerificationForCurrentUser.invoke() { error ->
            assertThat(error?.cause, instanceOf(AuthRequiredException::class.java))
        }
    }

    /*
    @Test
    fun `given user is logged and verified when subscribe then receive verification event`(){
        _when(authRepository.logged).thenReturn(true)
        _when(user.verified).thenReturn(true)

        var received: Boolean = false

        subscribeToEmailVerificationForCurrentUser.invoke() { _ -> received = true }

        assertThat(received, equalTo(true))
    }
    */

    /*
    @Test
    fun `given user is logged and not verified when subscribe then not receive verification event`(){
        _when(authRepository.logged).thenReturn(true)
        _when(user.verified).thenReturn(false)

        var received: Boolean = false

        subscribeToEmailVerificationForCurrentUser.invoke() { _ -> received = true }

        assertThat(received, equalTo(false))
    }
    */

    @Test
    fun `given there is a subscription when cancel then cancel subscription`(){
        _when(authRepository.logged).thenReturn(true)
        _when(authRepository.subscribeToCurrentUser(any())).thenReturn("subscription-uid")

        subscribeToEmailVerificationForCurrentUser.invoke { }


        subscribeToEmailVerificationForCurrentUser.cancel()

        verify(authRepository).unsubscribeToCurrentUser("subscription-uid")
    }

}