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

package com.pasmodev.presentation.presenter

import com.pasmodev.domain.usecase.Logout
import com.pasmodev.domain.usecase.SubscribeToCurrentUser
import com.pasmodev.presentation.dto.mapper.UserDtoToUserMapper
import com.pasmodev.presentation.view.MainView

class MainPresenterImpl(var view: MainView, var logout: Logout, var subscribeToCurrentUser: SubscribeToCurrentUser, var userDtoToUserMapper: UserDtoToUserMapper) : MainPresenter {

    override fun onStart() {
        subscribeToCurrentUser.invoke { user, error ->
            user?.let { view.showUser(userDtoToUserMapper.reverseMap(user)) }
        }
    }

    override fun onStop() {
        subscribeToCurrentUser.cancel()
    }

    override fun onLogoutAction() {
        logout { error ->
            error?.let {
                view.showError(error.message ?: "Unknown")
            } ?: view.close()
        }
    }
}