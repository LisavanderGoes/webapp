package com.lisa.View

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vaadin.flow.data.value.ValueChangeMode



@Route("grid")
class GridView: VerticalLayout() {

    private val list = arrayListOf(
            Data("name1", 1),
            Data("name2", 2),
            Data("name3", 3),
            Data("name4", 4),
            Data("name5", 5),
            Data("name6", 6),
            Data("name7", 7),
            Data("name8", 8),
            Data("name9", 9)
    )
    private val grid: Grid<Data> = Grid(com.lisa.View.Data::class.java)
    private val filterTextField = TextField()

    init {
        grid.setColumns("number", "name")

        filterTextField.placeholder = "Filter by name..."
        filterTextField.isClearButtonVisible = true
        filterTextField.valueChangeMode = ValueChangeMode.EAGER
        filterTextField.addValueChangeListener({ e -> updateList() })

        add(filterTextField, grid)
        setSizeFull()


        updateList()
    }

    private fun updateList() {
        grid.setItems(list.filter { s -> s.name == filterTextField.value})
    }
}

class Data(
        val name: String,
        val number: Int?
)
