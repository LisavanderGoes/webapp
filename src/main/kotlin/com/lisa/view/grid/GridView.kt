package com.lisa.view.grid

import com.lisa.crud.CRUD
import com.lisa.crud.annotation.Column
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class GridView<T: Any>(private val classType: Class<T>): Div() {

    private var list: ArrayList<T> = CRUD().select(classType, "")

    private val layout = VerticalLayout()
    private val grid = Grid(classType)
    private val searchField = TextField()

    init {
        searchField.placeholder = "Filter by name..."
        searchField.isClearButtonVisible = true

        layout.add(searchField, grid)
        add(layout)
        setSizeFull()

        searchField.valueChangeMode = ValueChangeMode.EAGER
        searchField.addValueChangeListener({ updateList() })

        updateList()
    }

    private fun updateList() {
        grid.setItems(filter(searchField.value))
    }

    private fun filter(value: String) : ArrayList<T> {
        if (value != "") {
            return list.filter { e ->
                val stringBuilder = StringBuilder()
                for (field in classType.declaredFields) {
                        val column = field.getAnnotation(Column::class.java)
                        if (column != null) {
                            field.isAccessible = true
                            stringBuilder.append(" " + field.get(e).toString())
                        }
                }
                stringBuilder.contains(searchField.value)
            } as ArrayList<T>
        }
        return list
    }
}
