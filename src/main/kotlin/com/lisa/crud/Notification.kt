package com.lisa.crud

import com.vaadin.flow.component.notification.Notification

class Notification: Notification() {

    init {
        val text = "Er is iets mis gegaan. Informeer de systeembeheerder."

        setText(text)
        position = Notification.Position.TOP_START

        duration = 30000
    }
}