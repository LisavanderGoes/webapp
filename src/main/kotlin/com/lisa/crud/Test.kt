package com.lisa.crud

import com.lisa.view.grid.model.TestGridModel
import java.lang.reflect.InvocationTargetException
import java.sql.SQLException
import java.util.*

class Test {
    companion object {

        @Throws(InvocationTargetException::class, IllegalAccessException::class, SQLException::class, NoSuchFieldException::class, NoSuchMethodException::class, InstantiationException::class)
        @JvmStatic
        fun main(args: Array<String>) {

            val item = CRUD<TestGridModel>().select(TestGridModel::class.java, " WHERE id= '1'")[0]

            item.number = 9.toDouble()
            item.name = "doet het niet"

//            System.out.println(CRUD().update(item, " WHERE id= '2'"))
//
//            System.out.println(CRUD().delete(item))

            System.out.println(CRUD<TestGridModel>().update(item, "WHERE id = '${item.id}'"))
        }
    }

}
