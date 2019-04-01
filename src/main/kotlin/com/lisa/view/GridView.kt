package com.lisa.view

import com.lisa.main.extend.ControllerDiv
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.Route

@Route("grid")
class GridView: ControllerDiv() {

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

    private val layout = VerticalLayout()
    private val grid: Grid<Data> = Grid(com.lisa.view.Data::class.java)
    private val filterTextField = TextField()

    init {
        grid.setColumns("number", "name")

        filterTextField.placeholder = "Filter by name..."
        filterTextField.valueChangeMode = ValueChangeMode.EAGER
        filterTextField.addValueChangeListener({ e -> updateList() })

        layout.add(filterTextField, grid)
        add(layout)
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
