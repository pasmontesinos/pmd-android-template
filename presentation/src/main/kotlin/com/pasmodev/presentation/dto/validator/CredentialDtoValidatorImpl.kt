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

class CredentialDtoValidatorImpl : CredentialDtoValidator {

    override fun getErrors(credentialDto: CredentialDto): Map<String, String>? {

        var result: MutableMap<String, String> = mutableMapOf()

        if (credentialDto.email.isNullOrEmpty() || credentialDto.email.isNullOrBlank()){
            result["email"] = "Email is required"
        }

        if (credentialDto.password.isNullOrEmpty() || credentialDto.password.isNullOrBlank()){
            result["password"] = "Password is required"
        }

        return if (result.isEmpty()) null else result
    }

}