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

package com.pasmodev.template.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.pasmodev.data.entity.CredentialEntity
import com.pasmodev.data.entity.UserEntity
import com.pasmodev.data.repository.datasource.AuthDataSource
import com.pasmodev.domain.exception.AuthRequiredException
import java.util.*
import kotlin.concurrent.timerTask


class AuthDataSourceFirebase(private var firebaseAuth: FirebaseAuth) : AuthDataSource {

    override val logged: Boolean
        get() = firebaseAuth.currentUser != null

    override val currentUser: UserEntity?
        get() = firebaseAuth.currentUser?.let { mapUserFirebaseToUserEntityMapper(it) }


    private var cachedUser: UserEntity? = null
    private var checkCurrentUserUpdates: Timer? = null

    override fun createUserWithCredential(user: UserEntity, credential: CredentialEntity, onComplete: (user: UserEntity?, error: Error?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(credential.email, credential.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(user.name)
                                .build()

                        firebaseAuth.currentUser!!.updateProfile(profileUpdates).addOnSuccessListener {
                            firebaseAuth.currentUser?.let { firebaseUser ->
                                firebaseUser.sendEmailVerification()
                                onComplete(mapUserFirebaseToUserEntityMapper(firebaseUser), null)
                            }
                        }
                    } else {
                        onComplete(null, Error(task.exception!!.localizedMessage, task.exception))
                    }
                }
    }

    override fun loginWithCredential(credential: CredentialEntity, onComplete: (user: UserEntity?, error: Error?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(credential.email, credential.password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(mapUserFirebaseToUserEntityMapper(firebaseAuth.currentUser!!), null)
            } else {
                onComplete(null, Error(task.exception!!.localizedMessage, task.exception))
            }
        }
    }

    override fun getCurrentUser(onComplete: (user: UserEntity?, error: Error?) -> Unit) {
        firebaseAuth.currentUser?.let {
            onComplete(mapUserFirebaseToUserEntityMapper(it), null)
        } ?: onComplete(null, Error("Not logged", AuthRequiredException("Not logged")))
    }

    private fun mapUserFirebaseToUserEntityMapper(it: FirebaseUser): UserEntity {
        val userEntity = UserEntity(
                id = it.uid,
                email = it.email!!,
                name = it.displayName?: "--",
                verified = it.isEmailVerified
        )
        return userEntity
    }

    override fun logout(onResult: (error: Error?) -> Unit) {
        firebaseAuth.signOut()
        onResult(null)
    }

    override fun subscribeToCurrentUser(onResult: (user: UserEntity?, error: Error?) -> Unit) {

        checkCurrentUserUpdates?.cancel()

        checkCurrentUserUpdates = Timer("checkCurrentUser")

        checkCurrentUserUpdates?.schedule(
                timerTask {
                    Log.v("AuthDataSourceFirebase", "Check current user updates")

                    firebaseAuth.currentUser?.reload().let {
                        val currentUser = mapUserFirebaseToUserEntityMapper(firebaseAuth.currentUser!!)

                        Log.v("AuthDataSourceFirebase", "Old user: $cachedUser - New user: $currentUser")

                        if (userIsChanged(cachedUser, currentUser)) {
                            Log.v("AuthDataSourceFirebase", "User changed")
                            onResult(currentUser, null)
                        } else {
                            Log.v("AuthDataSourceFirebase", "User not changed")
                        }

                        cachedUser = currentUser
                    }
                },
                0L,
                5000L
        )
    }

    override fun unsubscribeToCurrentUser(onResult: (user: UserEntity?, error: Error?) -> Unit) {
        checkCurrentUserUpdates?.cancel()
    }

    private fun userIsChanged(old: UserEntity?, new: UserEntity): Boolean {

        return old?.let {
            it.name != new.name || it.email != new.email || it.verified != new.verified
        } ?: false
    }
}