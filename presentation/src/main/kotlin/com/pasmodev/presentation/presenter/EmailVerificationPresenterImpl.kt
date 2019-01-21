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

import com.pasmodev.domain.usecase.SubscribeToEmailVerificationForCurrentUser
import com.pasmodev.presentation.view.EmailVerificationView

class EmailVerificationPresenterImpl(private var view: EmailVerificationView, private var subscribeToEmailVerificationForCurrentUser: SubscribeToEmailVerificationForCurrentUser) : EmailVerificationPresenter {

    override fun onStart() {
        view.showIndeterminateProgress(true)
        subscribeToEmailVerificationForCurrentUser.invoke { error -> handleOnResult(error) }
    }

    override fun onStop() {
        subscribeToEmailVerificationForCurrentUser.cancel()
        view.showIndeterminateProgress(false)
    }

    private fun handleOnResult(error: Error?) {
        if (error != null) {
            subscribeToEmailVerificationForCurrentUser.cancel()
            view.showIndeterminateProgress(false)
            view.showError(error.localizedMessage)
        } else {
            subscribeToEmailVerificationForCurrentUser.cancel()
            view.showIndeterminateProgress(false)
            view.navigateToMain()
        }
    }
}