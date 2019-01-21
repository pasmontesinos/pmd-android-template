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

package com.pasmodev.template.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.View
import com.pasmodev.template.App
import com.pasmodev.template.R
import com.pasmodev.template.di.components.DaggerSignupComponent
import com.pasmodev.template.di.modules.SignupModule
import com.pasmodev.presentation.dto.SignupUserDto
import com.pasmodev.presentation.presenter.SignupPresenter
import com.pasmodev.presentation.view.SignupView
import kotlinx.android.synthetic.main.activity_signup.*
import javax.inject.Inject

class SignupActivity : SignupView, AppCompatActivity() {

    val component by lazy {
        DaggerSignupComponent.builder()
                .appComponent((application as App).component)
                .signupModule(SignupModule(this))
                .build()
    }

    @Inject lateinit var presenter: SignupPresenter

    override fun enableInputs(enable: Boolean) {
        runOnUiThread {
            nameTextInputEditText.isEnabled = enable
            emailTextInputEditText.isEnabled = enable
            passwordTextInputEditText.isEnabled = enable
            passwordConfirmationTextInputEditText.isEnabled = enable
            signupButton.isEnabled = enable
        }
    }

    override fun showIndeterminateProgress(show: Boolean) {
        runOnUiThread {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    override fun navigateToMain() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    override fun navigateToEmailVerification() {
        val i = Intent(this, EmailVerificationActivity::class.java)
        startActivity(i)
    }

    override fun getSignupUserDtoInput(): SignupUserDto {
        return SignupUserDto(
                name = nameTextInputEditText.text.toString(),
                email = emailTextInputEditText.text.toString(),
                password = passwordTextInputEditText.text.toString(),
                passwordConfirmation = passwordConfirmationTextInputEditText.text.toString()
        )
    }

    override fun showSignupUserDtoInputErrors(errors: Map<String, String>) {
        runOnUiThread {
            nameTextInputEditText.error = errors["name"]
            emailTextInputLayout.error = errors["email"]
            passwordTextInputLayout.error = errors["password"]
            passwordConfirmationTextInputLayout.error = errors["password_confirmation"]
        }
    }

    override fun showError(error: String) {
        runOnUiThread {
            val snackbar = Snackbar.make(signupLayout, error, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(android.R.string.ok) { snackbar.dismiss() }
                    .setActionTextColor(Color.RED)
                    .show()
        }    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        component.inject(this)

        setActions()

        presenter.onCreate()
    }

    private fun setActions() {
        signupButton.setOnClickListener { presenter.onSignupAction() }
    }

    private fun showMessage(message: String){
        runOnUiThread {
            Snackbar.make(signupLayout, message, Snackbar.LENGTH_LONG).show()
        }
    }
}
