package com.lisa.View

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route("")
class MainView: VerticalLayout() {

    private val grid: Grid<Data>

    init {
        grid = Grid(Data::class.java)
        grid.setColumns("name", "number")
        add(grid)
        setSizeFull()

        grid.setItems(Data("name", "3"))
    }
}
