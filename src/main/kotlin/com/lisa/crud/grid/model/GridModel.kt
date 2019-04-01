package com.lisa.crud.grid.model

import com.lisa.crud.Column
import com.lisa.crud.ID
import com.lisa.crud.Table

@Table("grid")
class GridModel {
    @ID()
    @Column("id")
    var id: Long = 0
    @Column("name")
    lateinit var name: String
}
