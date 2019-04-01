package com.lisa.view

import com.lisa.main.AuthenticationService
import com.lisa.model.AuthenticationCredentials
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route("login")
@PageTitle("Login")
class LoginView: Div(), BeforeEnterObserver {
    override fun beforeEnter(event: BeforeEnterEvent) {
        event.navigationTarget
    }

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
        UI.getCurrent().navigate(MainView::class.java)
    }

}