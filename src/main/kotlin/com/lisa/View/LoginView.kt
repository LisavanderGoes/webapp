package com.lisa.View

import com.lisa.Main.AuthenticationService
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinSession

@Route("login")
class LoginView: FormLayout() {

    private val authentication = AuthenticationService()

    private val usernameTextField = TextField("Username")
    private val passwordTextField = TextField("Password")
    private val label = Label()
    private val loginButton = Button("Login")

    init {
        val mainContent = VerticalLayout(usernameTextField, passwordTextField, label, loginButton)
        add(mainContent)
        setSizeFull()
        loginButton.addClickListener({ login() })
    }

    private fun login() {
        authentication.authenticate(
                AuthenticationCredentials(
                        usernameTextField.value,
                        passwordTextField.value
                ),
                {loginFailure()},
                {loginSuccess()}
        )
    }

    private fun loginFailure() {
        label.text = "Incorrect"
    }

    private fun loginSuccess() {
        label.text = VaadinSession.getCurrent().getAttribute("currentUser") as String
    }

}

class AuthenticationCredentials(
        val username: String,
        val password: String
)