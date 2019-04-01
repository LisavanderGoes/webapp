package com.lisa.view

import com.lisa.crud.CRUD
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route("grid")
class GridView: Div() { //TODO: ControllerDiv()

    private var list: ArrayList<GridView> = CRUD().select(GridView::class.java, "")

    private val layout = VerticalLayout()
    private val grid: Grid<GridView> = Grid(GridView::class.java)

    init {
        grid.setColumns("id", "name")

        layout.add(grid)
        add(layout)
        setSizeFull()


        updateList()
    }

    private fun updateList() {
        grid.setItems(list)
    }
}
