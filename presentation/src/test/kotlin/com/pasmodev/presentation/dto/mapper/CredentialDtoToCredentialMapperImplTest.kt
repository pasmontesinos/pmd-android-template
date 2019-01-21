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

import com.pasmodev.domain.model.Credential
import com.pasmodev.presentation.dto.CredentialDto
import com.pasmodev.presentation.exception.NullPropertyException
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as _when

class CredentialDtoToCredentialMapperImplTest {

    @Mock lateinit var credentialDto: CredentialDto
    @Mock lateinit var credential: Credential

    lateinit var mapper: CredentialDtoToCredentialMapper

    private val email: String = "email"
    private val password: String = "password"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        _when(credentialDto.email).thenReturn(email)
        _when(credentialDto.password).thenReturn(password)

        _when(credential.email).thenReturn(email)
        _when(credential.password).thenReturn(password)

        mapper = CredentialDtoToCredentialMapperImpl()
    }


    @Test(expected = NullPropertyException::class)
    fun `when map with email null then trows NullPropertyException`(){

        _when(credentialDto.email).thenReturn(null)

        mapper.map(credentialDto)
    }

    @Test(expected = NullPropertyException::class)
    fun `when map with password null then trows NullPropertyException`(){

        _when(credentialDto.password).thenReturn(null)

        mapper.map(credentialDto)
    }

    @Test
    fun `test reverse map`(){
        val mapped = mapper.reverseMap(credential)

        assertThat(mapped.email, equalTo(credential.email))
        assertThat(mapped.password, equalTo(credential.password))
    }

}