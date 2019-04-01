package com.lisa.main.extend

import com.lisa.view.LoginView
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.server.VaadinSession

open class ControllerDiv: Div(), BeforeEnterObserver {
    override fun beforeEnter(event: BeforeEnterEvent) {
        if (VaadinSession.getCurrent().getAttribute("currentUser") == null) {
            event.forwardTo(LoginView::class.java)
        }
    }
}