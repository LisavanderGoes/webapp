package com.lisa.view

import com.lisa.view.grid.GridView
import com.lisa.view.grid.model.TestGridModel
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.Route

@Route("")
class MainView: Div() { //TODO: ControllerDiv()

    private val grid = GridView(TestGridModel::class.java)

    init {
        add(grid)
    }
}