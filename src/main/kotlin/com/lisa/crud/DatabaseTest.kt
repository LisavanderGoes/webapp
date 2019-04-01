package com.lisa.crud

import com.lisa.crud.grid.model.GridModel
import java.lang.reflect.InvocationTargetException
import java.sql.SQLException

class DatabaseTest {
    companion object {

        @Throws(InvocationTargetException::class, IllegalAccessException::class, SQLException::class, NoSuchFieldException::class, NoSuchMethodException::class, InstantiationException::class)
        @JvmStatic
        fun main(args: Array<String>) {

            System.out.println(CRUD().select(GridModel::class.java, " WHERE id= '1'")[0].name)

            val item = GridModel()
            item.id = 2
            item.name = "jack"

//            System.out.println(CRUD().update(item, " WHERE id= '2'"))
//
//            System.out.println(CRUD().delete(item))

            System.out.println(CRUD().insert(item))
        }
    }

}
