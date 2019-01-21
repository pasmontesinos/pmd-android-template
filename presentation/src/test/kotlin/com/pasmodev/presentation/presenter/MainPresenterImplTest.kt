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
import com.pasmodev.domain.model.User
import com.pasmodev.domain.usecase.Logout
import com.pasmodev.domain.usecase.SubscribeToCurrentUser
import com.pasmodev.presentation.dto.UserDto
import com.pasmodev.presentation.dto.mapper.UserDtoToUserMapper
import com.pasmodev.presentation.view.MainView
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as _when
import org.mockito.MockitoAnnotations

class MainPresenterImplTest {

    @Mock lateinit var view: MainView
    @Mock lateinit var logout: Logout
    @Mock lateinit var subscribeToCurrentUser: SubscribeToCurrentUser
    @Mock lateinit var userDtoToUserMapper: UserDtoToUserMapper
    @Mock lateinit var user: User
    @Mock lateinit var userDto: UserDto

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

        _when(subscribeToCurrentUser.invoke(any()))
                .thenAnswer { invocation ->
                    val callback: (user: User?, error: Error?) -> Unit = invocation.getArgument<Any>(0) as (User?, Error?) -> Unit
                    callback(user,null)
                }

        _when(userDtoToUserMapper.reverseMap(user)).thenReturn(userDto)

        presenter = MainPresenterImpl(view, logout, subscribeToCurrentUser, userDtoToUserMapper)
    }

    @Test
    fun `test when logout action call to logout usecase`(){
        presenter.onLogoutAction()

        verify(logout).invoke(any())
    }

    @Test
    fun `test when logout success then close`(){
        givenLogoutError(false)

        presenter.onLogoutAction()

        verify(view).close()
    }

    @Test
    fun `test when logout error then show error`(){
        givenLogoutError(true)

        presenter.onLogoutAction()

        verify(view).showError(any())
    }

    @Test
    fun `when start then subscribe to current user`() {
        presenter.onStart()

        verify(subscribeToCurrentUser).invoke(any())
    }

    @Test
    fun `when stop then unsubscribe to current user`() {
        presenter.onStop()

        verify(subscribeToCurrentUser).cancel()
    }

    @Test
    fun `given subscripted to current user when change user then show user in view`() {
        presenter.onStart()

        verify(view).showUser(any())
    }


    private fun givenLogoutError(error: Boolean) {
        _when(logout.invoke(any()))
                .thenAnswer { invocation ->
                    val callback: (error: Error?) -> Unit = invocation.getArgument<Any>(0) as (Error?) -> Unit

                    if (error)
                        callback(Error("Test error"))
                    else
                        callback( null)
                }
    }
}