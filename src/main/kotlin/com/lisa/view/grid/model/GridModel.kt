package com.lisa.view.grid.model

import com.lisa.crud.annotation.Column
import com.lisa.crud.annotation.ID
import com.lisa.crud.annotation.Table

@Table("grid")
class GridModel {
    @ID()
    @Column("id")
    var id: Long = 0
    @Column("name")
    lateinit var name: String
    @Column("description")
    lateinit var description: String
}
