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

class CredentialDtoToCredentialMapperImpl : CredentialDtoToCredentialMapper {

    override fun map(dto: CredentialDto): Credential {
        return Credential(
                email = dto.email?.let { it } ?: throw NullPropertyException("Email must no be null"),
                password = dto.password?.let { it } ?: throw NullPropertyException("Password must no be null")
        )
    }

    override fun reverseMap(model: Credential): CredentialDto {
        return CredentialDto(
                email = model.email,
                password = model.password
        )
    }
}