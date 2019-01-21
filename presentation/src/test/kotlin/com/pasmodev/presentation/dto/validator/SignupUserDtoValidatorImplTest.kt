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
import com.pasmodev.presentation.dto.SignupUserDto
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignupUserDtoValidatorImplTest {
    
    private lateinit var validator: SignupUserDtoValidator
    private lateinit var signupUserDto: SignupUserDto

    @Before
    fun setUp() {
        validator = SignupUserDtoValidatorImpl()
        signupUserDto = SignupUserDto()
    }

    @Test
    fun `name is required`() {
        signupUserDto.name = null
        assertThat(validator.getErrors(signupUserDto)?.containsKey("name"), CoreMatchers.equalTo(true))

        signupUserDto.name = ""
        assertThat(validator.getErrors(signupUserDto)?.containsKey("name"), CoreMatchers.equalTo(true))

        signupUserDto.name = " "
        assertThat(validator.getErrors(signupUserDto)?.containsKey("name"), CoreMatchers.equalTo(true))

        signupUserDto.name = "email@domain.com"
        assertThat(validator.getErrors(signupUserDto)?.containsKey("name"), CoreMatchers.equalTo(false))
    }

    @Test
    fun `email is required`() {
        signupUserDto.email = null
        assertThat(validator.getErrors(signupUserDto)?.containsKey("email"), CoreMatchers.equalTo(true))

        signupUserDto.email = ""
        assertThat(validator.getErrors(signupUserDto)?.containsKey("email"), CoreMatchers.equalTo(true))

        signupUserDto.email = " "
        assertThat(validator.getErrors(signupUserDto)?.containsKey("email"), CoreMatchers.equalTo(true))

        signupUserDto.email = "email@domain.com"
        assertThat(validator.getErrors(signupUserDto)?.containsKey("email"), CoreMatchers.equalTo(false))
    }

    @Test
    fun `password is required`() {
        signupUserDto.password = null
        assertThat(validator.getErrors(signupUserDto)?.containsKey("password"), CoreMatchers.equalTo(true))

        signupUserDto.password = ""
        assertThat(validator.getErrors(signupUserDto)?.containsKey("password"), CoreMatchers.equalTo(true))

        signupUserDto.password = " "
        assertThat(validator.getErrors(signupUserDto)?.containsKey("password"), CoreMatchers.equalTo(true))

        signupUserDto.password = "password@domain.com"
        assertThat(validator.getErrors(signupUserDto)?.containsKey("password"), CoreMatchers.equalTo(false))
    }

    @Test
    fun `password confirmation is equal to password`() {
        signupUserDto.password = "a"
        signupUserDto.passwordConfirmation = "b"
        assertThat(validator.getErrors(signupUserDto)?.containsKey("password_confirmation"), CoreMatchers.equalTo(true))
    }
}