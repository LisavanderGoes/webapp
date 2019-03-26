package com.lisa.View

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import java.io.Serializable

@Route("grid")
class GridView: VerticalLayout() {

    private val list = arrayListOf<Data>(
//            Data("name1", 1),
//            Data("name2", 2),
//            Data("name3", 3),
//            Data("name4", 4),
//            Data("name5", 5),
//            Data("name6", 6),
//            Data("name7", 7),
//            Data("name8", 8),
//            Data("name9", 9)
    )
    private val grid: Grid<Data>

    init {
        grid = Grid(com.lisa.View.Data::class.java)
        //grid.setColumns("Name", "Number")
        updateList()
        add(grid)
        setSizeFull()
    }

    private fun updateList() {
        grid.setItems(list)
    }
}

class Data(
        val name: String,
        val number: String
) : Serializable, Cloneable {
    init {

    }
}
