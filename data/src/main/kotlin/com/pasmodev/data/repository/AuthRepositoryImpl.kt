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

import com.pasmodev.data.entity.UserEntity
import com.pasmodev.data.entity.mapper.CredentialEntityToCredentialMapper
import com.pasmodev.data.entity.mapper.UserEntityToUserMapper
import com.pasmodev.data.repository.datasource.AuthDataSource
import com.pasmodev.domain.model.Credential
import com.pasmodev.domain.model.User
import com.pasmodev.domain.repository.AuthRepository
import java.util.*

class AuthRepositoryImpl(private var authDataSource: AuthDataSource, private var credentialEntityToCredentialMapper: CredentialEntityToCredentialMapper, private var userEntityToUserMapper: UserEntityToUserMapper) : AuthRepository {

    override val logged: Boolean
        get() = authDataSource.logged

    override val currentUser: User?
        get() = userEntityToUserMapper.map(authDataSource.currentUser)

    var subscriptions = mutableMapOf<String, (user: User?, error: Error?) -> Unit>()

    var masterSubscription: ((user: UserEntity?, error: Error?) -> Unit)? = null

    private var cachedUser: UserEntity? = null

    override fun loginWithCredential(credential: Credential, onComplete: (user: User?, error: Error?) -> Unit) {
        authDataSource.loginWithCredential(credentialEntityToCredentialMapper.reverseMap(credential)) { user, error ->
            onComplete(user?.let { userEntityToUserMapper.map(it) }, error)
        }
    }

    override fun createUserWithCredential(user: User, credential: Credential, onComplete: (user: User?, error: Error?) -> Unit) {
        authDataSource.createUserWithCredential(userEntityToUserMapper.reverseMap(user)!!, credentialEntityToCredentialMapper.reverseMap(credential)!!) { user, error ->
            onComplete(user?.let { userEntityToUserMapper.map(it) }, error)
        }
    }

    override fun getCurrentUser(onComplete: (user: User?, error: Error?) -> Unit) {
        authDataSource.getCurrentUser { user, error ->
            onComplete(user?.let { userEntityToUserMapper.map(it) }, error)
        }
    }

    override fun logout(onComplete: (error: Error?) -> Unit) {
        authDataSource.logout { error -> onComplete(error) }
    }

    override fun subscribeToCurrentUser(onComplete: (user: User?, error: Error?) -> Unit): String {

        val subscriptionUid = UUID.randomUUID().toString()

        subscriptions.put(subscriptionUid, onComplete)

        if (subscriptions.size == 1)
            initMasterSubscription()
        else
            onComplete(userEntityToUserMapper.map(cachedUser), null)

        return subscriptionUid
    }

    override fun unsubscribeToCurrentUser(subscriptionUid: String) {
        if (subscriptions.containsKey(subscriptionUid))
            subscriptions.remove(subscriptionUid)

        if (subscriptions.isEmpty())
            cancelMasterSubscription()
    }

    private fun initMasterSubscription() {
        masterSubscription = { userEntity, error ->
            cachedUser = userEntity
            var user = userEntityToUserMapper.map(userEntity)
            for ((_, subscription) in subscriptions) {
                subscription(user, error)
            }
        }
        authDataSource.subscribeToCurrentUser(masterSubscription!!)
    }

    private fun cancelMasterSubscription() {
        masterSubscription?.let {
            authDataSource.unsubscribeToCurrentUser(it)
        }
    }

}