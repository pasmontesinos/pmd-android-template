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

package com.pasmodev.data.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isNull
import com.nhaarman.mockitokotlin2.verify
import com.pasmodev.data.entity.UserEntity
import com.pasmodev.data.entity.mapper.UserEntityToUserMapper
import com.pasmodev.data.repository.datasource.UserDataSource
import com.pasmodev.domain.exception.UserNotFoundException
import com.pasmodev.domain.model.User
import com.pasmodev.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*
import org.mockito.Mockito.`when` as _when

class UserRepositoryImplTest {

    @Mock lateinit var mockUserDataSource: UserDataSource
    @Mock lateinit var mockUserEntityToUserMapper: UserEntityToUserMapper
    @Mock lateinit var mockUserEntity: UserEntity
    @Mock lateinit var mockUser: User

    lateinit var userRepository : UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        userRepository = UserRepositoryImpl(mockUserDataSource, mockUserEntityToUserMapper)

        _when(mockUserEntityToUserMapper.map(mockUserEntity)).thenReturn(mockUser)
        _when(mockUserEntityToUserMapper.reverseMap(mockUser)).thenReturn(mockUserEntity)
    }

    @Test
    fun `given exists user when get user then return user`(){
        existsUser(true)

        var resultUser: User? = null
        var resultError: Error? = null

        runBlocking {
            userRepository.getOne(UUID.randomUUID().toString()) { user, error ->
                resultUser = user
                resultError = error
            }
        }

        verify(mockUserDataSource).get(any(), any())
        verify(mockUserEntityToUserMapper).map(mockUserEntity)

        assertThat(resultUser, equalTo(mockUser))
        Assert.assertNull(resultError)
    }

    @Test
    fun `given not exists user when get user then failure by UserNotFoundException`() {
        existsUser(false)

        var resultUser: User? = null
        var resultError: Error? = null

        runBlocking {
            userRepository.getOne(UUID.randomUUID().toString()) { user, error ->
                resultUser = user
                resultError = error
            }
        }

        Assert.assertNull(resultUser)
        assertThat(resultError?.cause, instanceOf(UserNotFoundException::class.java))
    }

    @Test
    fun `given not exists user when create user then success`() {
        existsUser(false)

        var resultUser: User? = null
        var resultError: Error? = null

        runBlocking {
            userRepository.save(mockUser) { user, error ->
                resultUser = user
                resultError = error
            }
        }

        verify(mockUserEntityToUserMapper).reverseMap(mockUser)
        verify(mockUserDataSource).create(any(), any())
        verify(mockUserEntityToUserMapper).map(mockUserEntity)

        assertThat(resultUser, equalTo(mockUser))
        assertThat(resultError, isNull())
    }

    private fun existsUser(exists: Boolean) {
        _when(mockUserDataSource.get(any(), any()))
                .thenAnswer { invocation ->
                    val callback: (userEntity: UserEntity?, error: Error?) -> Unit = invocation.getArgument<Any>(1) as (UserEntity?, Error?) -> Unit

                    if (exists)
                        callback(mockUserEntity, null)
                    else
                        callback(null, null)
                }
    }




}