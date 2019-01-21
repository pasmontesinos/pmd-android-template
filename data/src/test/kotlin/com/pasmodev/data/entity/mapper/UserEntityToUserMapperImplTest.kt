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

import com.pasmodev.data.entity.UserEntity
import com.pasmodev.domain.model.User
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class UserEntityToUserMapperImplTest {

    lateinit var mapper: UserEntityToUserMapper

    @Before
    fun setUp() {
        mapper = UserEntityToUserMapperImpl()
    }

    @Test
    fun map() {
        var userEntity = UserEntity(
                id = UUID.randomUUID().toString(),
                name = "name",
                email = "email@domain.com",
                verified = true
        )

        var user = mapper.map(userEntity)

        assertThat(user?.id, equalTo(userEntity.id))
        assertThat(user?.name, equalTo(userEntity.name))
        assertThat(user?.email, equalTo(userEntity.email))
        assertThat(user?.verified, equalTo(userEntity.verified))
    }

    @Test
    fun reverseMap() {
        var user = User(
                id = UUID.randomUUID().toString(),
                name = "name",
                email = "email@domain.com",
                verified = true
        )

        var userEntity = mapper.reverseMap(user)

        assertThat(userEntity?.id, equalTo(user.id))
        assertThat(userEntity?.name, equalTo(user.name))
        assertThat(userEntity?.email, equalTo(user.email))
        assertThat(userEntity?.verified, equalTo(user.verified))
    }
}