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
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.pasmodev.template.App
import com.pasmodev.template.R
import com.pasmodev.template.di.components.DaggerLoginComponent
import com.pasmodev.template.di.modules.LoginModule
import com.pasmodev.presentation.dto.CredentialDto
import com.pasmodev.presentation.presenter.LoginPresenter
import com.pasmodev.presentation.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginView {

    val component by lazy {
        DaggerLoginComponent.builder()
                .appComponent((application as App).component)
                .loginModule(LoginModule(this))
                .build()
    }

    @Inject lateinit var presenter: LoginPresenter


    override fun showIndeterminateProgress(show: Boolean) {
        runOnUiThread {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    override fun getCredentialInput(): CredentialDto {
        return CredentialDto(
                email = emailTextInputEditText.text.toString(),
                password = passwordTextInputEditText.text.toString()
        )
    }

    override fun enableInputs(enable: Boolean) {
        runOnUiThread {
            emailTextInputEditText.isEnabled = enable
            passwordTextInputEditText.isEnabled = enable
            loginButton.isEnabled = enable
            signupButton.isEnabled = enable
        }
    }

    override fun navigateToMain() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    override fun navigateToSignUp() {
        val i = Intent(this, SignupActivity::class.java)
        startActivity(i)
    }

    override fun navigateToEmailVerification() {
        val i = Intent(this, EmailVerificationActivity::class.java)
        startActivity(i)
    }

    override fun showError(error: String) {
        runOnUiThread {
            val snackbar = Snackbar.make(loginLayout, error, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(android.R.string.ok) { snackbar.dismiss() }
                    .setActionTextColor(Color.RED)
                    .show()
        }
    }

    override fun showCredentialInputErrors(errors: Map<String, String>) {
        runOnUiThread {
            emailTextInputLayout.error = errors["email"]
            passwordTextInputLayout.error = errors["password"]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        component.inject(this)

        setActions()

        presenter.onCreate()
    }

    private fun setActions() {
        loginButton.setOnClickListener { presenter.onLoginWithCredentialAction() }
        signupButton.setOnClickListener { presenter.onNavigateToSignUpAction() }
    }

    private fun showMessage(message: String){
        runOnUiThread {
            Snackbar.make(loginLayout, message, Snackbar.LENGTH_LONG).show()
        }
    }

}
