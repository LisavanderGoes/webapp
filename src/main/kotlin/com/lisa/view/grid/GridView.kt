package com.lisa.view.grid

import com.lisa.crud.CRUD
import com.lisa.crud.annotation.Column
import com.lisa.crud.annotation.ID
import com.lisa.main.grid.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import java.lang.reflect.Field
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class GridView<T: Any>(private val classType: Class<T>): VerticalLayout() {

    private val crud = CRUD<T>()
    private var list: ArrayList<T> = crud.select(classType, "")

    private val grid = Grid(classType)
    private val searchField = TextField()
    private val formLayout = FormLayout()
    private val save = Button("Save")
    private val delete = Button("Delete")


    private var newModel = classType.getConstructor().newInstance()
    private lateinit var selectedModel: T

    init {
        grid.setColumns()
        classType.declaredFields.forEach { field ->
            val column = field.getAnnotation(Column::class.java)
            if (column != null) {
                field.isAccessible = true
                addColumn(field)
            }
            try {
                formLayout.add(createFormField(field, null))
            } catch (e: Exception) {}
        }

        searchField.placeholder = "Filter by..."
        searchField.isClearButtonVisible = true

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR)
        val mainContent = VerticalLayout(grid, delete, formLayout, save)

        grid.addItemClickListener { e ->
            selectedModel = e.item
        }

        mainContent.setSizeFull()
        grid.setWidthFull()
        grid.height = "900px"

        add(searchField, mainContent)


        save.addClickListener { save() }
        delete.addClickListener { delete() }


        grid.setSelectionMode(Grid.SelectionMode.SINGLE)

        searchField.valueChangeMode = ValueChangeMode.EAGER
        searchField.addValueChangeListener({ updateList() })

        updateList()
    }

    private fun addColumn(field: Field) {
        when {
            field.getAnnotation(StepperFieldGetter::class.java) != null -> {
                grid.addComponentColumn<NumberField>({ item ->
                    val stepperField = NumberField()
                    stepperField.value = field.get(item) as Double
                    stepperField.setHasControls(true)
                    stepperField.addValueChangeListener { e ->
                        if (item != null) {
                            if (!stepperField.isEmpty) {
                                field.set(item, stepperField.value)
                                edit(item)
                            }
                        }
                    }
                    stepperField
                })
                        .setHeader(field.name.capitalize())
                        .isResizable = true
            }
            field.getAnnotation(ID::class.java) != null -> {
                grid.addColumn(field.name)
                        .isResizable = true
            }
            else -> {
                grid.addColumn(ComponentRenderer<VerticalLayout, T> { item ->
                    val component = createFormField(field, item)
                    VerticalLayout(component)
                })
                        .setHeader(field.name.capitalize())
                        .isResizable = true
            }
        }
    }

    private fun createFormField(field: Field, item: T?) : Component? {
        when {
            field.getAnnotation(ComboBoxGetter::class.java) != null -> {
                val annotation = field.getAnnotation(ComboBoxGetter::class.java)
                val comboBox = ComboBox<String>()
                val collection = HashSet<String>()
                annotation.value.forEach { e -> collection.add(e) }
                comboBox.setItems(collection)
                if (item != null) {
                    comboBox.value = field.get(item).toString()
                    comboBox.addValueChangeListener({
                        if (!comboBox.isEmpty) {
                            field.set(item, comboBox.value)
                            edit(item)
                        }
                    })
                } else {
                    comboBox.label = field.name.capitalize()
                    comboBox.addValueChangeListener({
                        if (!comboBox.isEmpty) {
                            updateModel(field, comboBox.value)
                        }
                    })
                }
                return comboBox
            }
            field.getAnnotation(TextFieldGetter::class.java) != null -> {
                val textField = TextField()
                textField.isClearButtonVisible = true
                textField.valueChangeMode = ValueChangeMode.EAGER
                if (item != null) {
                    textField.value = field.get(item).toString()
                    textField.addValueChangeListener({
                        if (!textField.isEmpty) {
                            field.set(item, textField.value)
                            edit(item)
                        }
                    })
                } else {
                    textField.label = field.name.capitalize()
                    textField.addValueChangeListener({
                        if (!textField.isEmpty) {
                            updateModel(field, textField.value)
                        }
                    })
                }
                return textField
            }
            field.getAnnotation(TextAreaGetter::class.java) != null -> {
                val textArea = TextArea()
                textArea.isClearButtonVisible = true
                textArea.valueChangeMode = ValueChangeMode.EAGER
                if (item != null) {
                    textArea.value = field.get(item).toString()
                    textArea.addValueChangeListener({
                        if (!textArea.isEmpty) {
                            field.set(item, textArea.value)
                            edit(item)
                        }
                    })
                } else {
                    textArea.label = field.name.capitalize()
                    textArea.addValueChangeListener({
                        if (!textArea.isEmpty) {
                            updateModel(field, textArea.value)
                        }
                    })
                }
                return textArea
            }
            field.getAnnotation(NumberFieldGetter::class.java) != null -> {
                val numberField = NumberField()
                numberField.isClearButtonVisible = true
                numberField.valueChangeMode = ValueChangeMode.EAGER
                if (item != null) {
                    numberField.value = field.get(item) as Double?
                    numberField.addValueChangeListener({
                        if (!numberField.isEmpty) {
                            try {
                                field.set(item, numberField.value)
                                edit(item)
                            } catch (e : Exception) {}
                        }
                    })
                } else {
                    numberField.label = field.name.capitalize()
                    numberField.addValueChangeListener({
                        if (!numberField.isEmpty) {
                            try {
                                updateModel(field, numberField.value)
                            } catch (e : Exception) {}
                        }
                    })
                }
                return numberField
            }
            field.getAnnotation(DatePickerGetter::class.java) != null -> {
                val datePicker = DatePicker()
                if (item != null) {
                    datePicker.value = LocalDate.parse( SimpleDateFormat("yyyy-MM-dd").format(field.get(item)))
                    datePicker.addValueChangeListener({
                        if (!datePicker.isEmpty) {
                            field.set(item, Date.valueOf(datePicker.value))
                            edit(item)
                        }
                    })
                } else {
                    datePicker.label = field.name.capitalize()
                    datePicker.addValueChangeListener({
                        if (!datePicker.isEmpty) {
                            updateModel(field, Date.valueOf(datePicker.value))
                        }
                    })
                }
                return datePicker
            }
            else -> {
                return null
            }
        }
    }

    private fun edit(item: T) {
        classType.declaredFields.forEach { field ->
            val column = field.getAnnotation(ID::class.java)
            if (column != null) {
                field.isAccessible = true
                try {
                    crud.update(item, "WHERE id = '${field.get(item)}'")
                } catch (e: Exception) {
                   //TODO: error message
                }
            }
        }
        reloadList()
    }

    private fun save() {
        try {
            crud.insert(newModel)
        } catch (e: Exception) {
            //TODO: error message
        }
        newModel = classType.getConstructor().newInstance()
        reloadList()
        UI.getCurrent().page.reload()
    }

    private fun delete() {
        try {
            crud.delete(grid.selectedItems.first())
        } catch (e: Exception) {

        }
        grid.select(null)
        reloadList()
    }

    private fun reloadList() {
        list = crud.select(classType, "")
        grid.setItems(list)
    }

    private fun updateModel(field: Field, value: Any) {
        field.set(newModel, value)
    }

    private fun updateList() {
        grid.setItems(filter(searchField.value))
    }

    private fun filter(value: String) : ArrayList<T> {
        if (value != "") {
            return list.filter { e ->
                val stringBuilder = StringBuilder()
                classType.declaredFields.forEach { field ->
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
