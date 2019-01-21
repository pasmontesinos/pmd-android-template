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
import com.pasmodev.domain.usecase.SubscribeToEmailVerificationForCurrentUser
import com.pasmodev.presentation.view.EmailVerificationView
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as _when

class EmailVerificationPresenterImplTest {

    @Mock lateinit var view: EmailVerificationView
    @Mock lateinit var subscribeToEmailVerificationForCurrentUser: SubscribeToEmailVerificationForCurrentUser

    lateinit var presenter: EmailVerificationPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = EmailVerificationPresenterImpl(view, subscribeToEmailVerificationForCurrentUser)
    }

    @Test
    fun `test when start show progress and subscribe to email verification`(){
        presenter.onStart()

        verify(view).showIndeterminateProgress(true)
        verify(subscribeToEmailVerificationForCurrentUser).invoke(any())
    }
    @Test
    fun `test when stop hide progress and cancel subscription to email verification`(){
        presenter.onStart()

        presenter.onStop()

        verify(view).showIndeterminateProgress(false)
        verify(subscribeToEmailVerificationForCurrentUser).cancel()
    }


    @Test
    fun `test when email verified then navigate to main`(){
        givenEmailVerified(true)

        presenter.onStart()

        verify(view).navigateToMain()
    }

    @Test
    fun `test when subscription error then show error and hide progress`(){
        givenSubscriptionError(true)

        presenter.onStart()

        verify(view).showIndeterminateProgress(false)
        verify(view).showError(any())
    }



    private fun givenEmailVerified(verified: Boolean) {
        givenSubscription(verified = verified)
    }

    private fun givenSubscriptionError(error: Boolean) {
        givenSubscription(error = error)
    }

    private fun givenSubscription(verified: Boolean = false, error: Boolean = false){
        _when(subscribeToEmailVerificationForCurrentUser.invoke(any()))
                .thenAnswer { invocation ->
                    val callback: (error: Error?) -> Unit = invocation.getArgument<Any>(0) as (Error?) -> Unit

                    if (error)
                        callback(Error("Test error"))
                    else if (verified)
                        callback( null)
                }
    }
}