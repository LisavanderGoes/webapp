package com.lisa.Main

import com.lisa.View.AuthenticationCredentials
import com.vaadin.flow.server.VaadinSession


class AuthenticationService {

    fun authenticate(authenticationCredentials: AuthenticationCredentials, failure: () -> Unit, success: () -> Unit) {
        if (authenticationCredentials.username == "lisa" && authenticationCredentials.password == "lisa123") {
            VaadinSession.getCurrent().setAttribute("currentUser", authenticationCredentials.username)
            success()
        } else {
            failure()
        }
    }
}