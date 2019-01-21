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

package com.pasmodev.presentation.dto.validator

import com.pasmodev.presentation.dto.CredentialDto
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CredentialDtoValidatorImplTest {

    private lateinit var validator: CredentialDtoValidator
    private lateinit var credentialDto: CredentialDto

    @Before
    fun setUp() {
        validator = CredentialDtoValidatorImpl()
        credentialDto = CredentialDto()
    }

    @Test
    fun `email is required`() {
        credentialDto.email = null
        assertThat(validator.getErrors(credentialDto)?.containsKey("email"), equalTo(true))

        credentialDto.email = ""
        assertThat(validator.getErrors(credentialDto)?.containsKey("email"), equalTo(true))

        credentialDto.email = " "
        assertThat(validator.getErrors(credentialDto)?.containsKey("email"), equalTo(true))

        credentialDto.email = "email@domain.com"
        assertThat(validator.getErrors(credentialDto)?.containsKey("email"), equalTo(false))
    }

    @Test
    fun `password is required`() {
        credentialDto.password = null
        assertThat(validator.getErrors(credentialDto)?.containsKey("password"), equalTo(true))

        credentialDto.password = ""
        assertThat(validator.getErrors(credentialDto)?.containsKey("password"), equalTo(true))

        credentialDto.password = " "
        assertThat(validator.getErrors(credentialDto)?.containsKey("password"), equalTo(true))

        credentialDto.password = "password"
        assertThat(validator.getErrors(credentialDto)?.containsKey("password"), equalTo(false))
    }
}