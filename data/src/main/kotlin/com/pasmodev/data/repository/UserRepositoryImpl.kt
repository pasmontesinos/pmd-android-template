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

import com.pasmodev.data.entity.mapper.UserEntityToUserMapper
import com.pasmodev.data.repository.datasource.UserDataSource
import com.pasmodev.domain.exception.UserNotFoundException
import com.pasmodev.domain.model.User
import com.pasmodev.domain.repository.UserRepository

class UserRepositoryImpl(private var userDataSource: UserDataSource, private var userEntityToUserlMapper: UserEntityToUserMapper) : UserRepository {

    override fun getOne(id: String, onComplete: (user: User?, error: Error?) -> Unit) {
        userDataSource.get(id) { user, error ->
            error?.let {
                onComplete(null, it)
            } ?: user?.let {
                onComplete(userEntityToUserlMapper.map(it), null)
            } ?: onComplete(null, Error("User $id not exists in datasource.", UserNotFoundException("User $id not exists in datasource.")))
        }
    }

    override fun save(user: User, onComplete: (user: User?, error: Error?) -> Unit) {
        userDataSource.create(userEntityToUserlMapper.reverseMap(user)!!) {user, error ->
            error?.let {
                onComplete(null, it)
            } ?: onComplete( userEntityToUserlMapper.map(user), null)
        }
    }

    override fun subscribeToUser(id: String, onEvent: (user: User?, error: Error?) -> Unit): String {
        return userDataSource.subscribeToUser(id) { user, error ->  onEvent(userEntityToUserlMapper.map(user), error) }
    }

    override fun unsubscribeFromUser(subscriptionId: String) {
        userDataSource.unsubscribeFromUser(subscriptionId)
    }
}
