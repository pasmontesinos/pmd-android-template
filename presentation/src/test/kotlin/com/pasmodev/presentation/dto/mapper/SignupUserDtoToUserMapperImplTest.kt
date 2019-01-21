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

package com.pasmodev.presentation.dto.mapper

import com.pasmodev.presentation.dto.SignupUserDto
import com.pasmodev.presentation.exception.NullPropertyException
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as _when

class SignupUserDtoToUserMapperImplTest {

    @Mock lateinit var signupUserDto: SignupUserDto

    lateinit var mapper: SignupUserDtoToUserMapper

    private val name: String = "name"
    private val email: String = "email"
    private val password: String = "password"


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        _when(signupUserDto.name).thenReturn(name)
        _when(signupUserDto.email).thenReturn(email)

        mapper = SignupUserDtoToUserMapperImpl()
    }


    @Test(expected = NullPropertyException::class)
    fun `when map with email null then trows NullPropertyException`(){

        _when(signupUserDto.email).thenReturn(null)

        mapper.map(signupUserDto)
    }

    @Test(expected = NullPropertyException::class)
    fun `when map with name null then trows NullPropertyException`(){

        _when(signupUserDto.name).thenReturn(null)

        mapper.map(signupUserDto)
    }

    @Test
    fun `test map`(){
        val mapped = mapper.map(signupUserDto)

        assertThat(mapped.name, equalTo(signupUserDto.name))
        assertThat(mapped.email, equalTo(signupUserDto.email))
    }

}