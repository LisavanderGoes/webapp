package com.lisa.main

import com.lisa.model.AuthenticationCredentials
import com.vaadin.flow.server.VaadinSession


class AuthenticationService {

    fun authenticate(authenticationCredentials: AuthenticationCredentials, failure: () -> Unit, success: () -> Unit) {
        if (authenticationCredentials.username == "lisa" && authenticationCredentials.password == "lisa123") {
            VaadinSession.getCurrent().setAttribute("currentUser", authenticationCredentials.username)
            VaadinSession.getCurrent().setAttribute("currentUserRole", Roles.USER) //TODO: Not always user
            success()
        } else {
            failure()
        }
    }
}

enum class Roles {
    ADMIN, USER
}

