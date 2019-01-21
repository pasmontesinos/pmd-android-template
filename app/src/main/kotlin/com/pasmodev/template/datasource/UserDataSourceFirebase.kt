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

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.pasmodev.data.entity.UserEntity
import com.pasmodev.data.repository.datasource.UserDataSource
import java.util.*

class UserDataSourceFirebase(val db: FirebaseFirestore) : UserDataSource {

    private var subscriptions = mutableMapOf<String, ListenerRegistration>()

    override fun create(user: UserEntity, onComplete: (user: UserEntity?, error: Error?) -> Unit) {
        userDocumentReference(user.id)
                .set(user)
                .addOnSuccessListener { onComplete(user, null) }
                .addOnFailureListener { e -> onComplete(null, Error("Create user failure", e)) }
    }

    override fun get(id: String, onComplete: (user: UserEntity?, error: Error?) -> Unit) {
        userDocumentReference(id)
                .get()
                .addOnSuccessListener { documentSnapshot -> onComplete(documentSnapshot.toObject(UserEntity::class.java), null) }
                .addOnFailureListener { e -> onComplete(null, Error("Get user failure", e)) }
    }

    override fun subscribeToUser(userId: String, onEvent: (user: UserEntity?, error: Error?) -> Unit) : String {
        val subscriptionId = UUID.randomUUID().toString()
        val listenerRegistration = userDocumentReference(userId).addSnapshotListener { snapshot, exception ->
            snapshot?.let {
                onEvent(it.toObject(UserEntity::class.java), null)
            } ?: exception?.let {
                onEvent(null, Error(it.localizedMessage, it.cause))
            }
        }

        subscriptions.put(subscriptionId, listenerRegistration)
        return subscriptionId
    }

    override fun unsubscribeFromUser(subscriptionId: String) {

        if (subscriptions.containsKey(subscriptionId)){
            subscriptions[subscriptionId]?.remove()
            subscriptions.remove(subscriptionId)
        }
    }

    private fun userDocumentReference(userId: String) : DocumentReference {
        return db.collection("users").document(userId)
    }
}