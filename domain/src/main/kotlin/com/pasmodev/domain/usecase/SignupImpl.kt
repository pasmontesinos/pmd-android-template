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

package com.pasmodev.domain.usecase

import com.pasmodev.domain.model.Credential
import com.pasmodev.domain.model.User
import com.pasmodev.domain.repository.AuthRepository
import com.pasmodev.domain.repository.UserRepository

class SignupImpl(var authRepository: AuthRepository, var userRepository: UserRepository) : Signup {

    override fun invoke(user: User, credential: Credential, onComplete: (user: User?, error: Error?) -> Unit) {
        authRepository.createUserWithCredential(user, credential) { _user, _error ->
            _error?.let {
                onComplete(null, it)
            } ?: _user?.let {
                userRepository.save(it) { user, error ->  onComplete(user, error)}
            }
        }
    }

}