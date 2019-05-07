package com.lisa.view.grid.model

import com.lisa.crud.annotation.Column
import com.lisa.crud.annotation.ID
import com.lisa.crud.annotation.Table
import com.lisa.main.grid.*
import java.util.*

@Table("grid")
class TestGridModel {
    @ID
    @Column("id")
    var id: Long = 0

    @Column("name")
    @TextFieldGetter
    lateinit var name: String

    @Column("description")
    @TextAreaGetter
    lateinit var description: String

    @Column("status")
    @ComboBoxGetter("ADMIN", "USER")
    lateinit var status: String

    @Column("birthday")
    @DatePickerGetter
    lateinit var birthday: Date

    @Column("number")
    @NumberFieldGetter
    @StepperFieldGetter
    var number: Double = 0.0
}

enum class Roles {
    ADMIN, USER
}
