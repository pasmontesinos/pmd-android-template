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

package com.pasmodev.data.entity.mapper

import com.pasmodev.data.entity.CredentialEntity
import com.pasmodev.domain.model.Credential
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class CredentialEntityToCredentialMapperImplTest {

    private lateinit var mapper: CredentialEntityToCredentialMapper

    @Before
    fun setUp() {
        mapper = CredentialEntityToCredentialMapperImpl()
    }

    @Test
    fun map() {
        var credentialEntity = CredentialEntity(
                email = "email@domain.com",
                password = "password"
        )

        var credential = mapper.map(credentialEntity)

        assertThat(credential.email, equalTo(credentialEntity.email))
        assertThat(credential.password, equalTo(credentialEntity.password))
    }

    @Test
    fun reverseMap() {
        var credential = Credential(
                email = "email@domain.com",
                password = "password"
        )

        var credentialEntity = mapper.reverseMap(credential)

        assertThat(credentialEntity.email, equalTo(credential.email))
        assertThat(credentialEntity.password, equalTo(credential.password))
    }
}