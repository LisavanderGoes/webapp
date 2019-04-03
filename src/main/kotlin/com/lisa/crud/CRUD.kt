package com.lisa.crud

import com.lisa.crud.annotation.Column
import com.lisa.crud.annotation.ID
import com.lisa.crud.annotation.Table

class CRUD {

    fun <T> select(classType: Class<T>, conditions: String) : ArrayList<T> {
        val connection = DatabaseConnection().databaseConnection

        val tableName = classType.getAnnotation(Table::class.java).value
        val query = "SELECT * FROM $tableName$conditions"

        val statement = connection.prepareStatement(query)
        val resultSet = statement.executeQuery()

        val objectList = ArrayList<T>()

        while (resultSet.next()) {
            val newItem = classType.getConstructor().newInstance()

            for (field in classType.declaredFields) {
                val column = field.getAnnotation(Column::class.java)
                if (column != null) {
                    val item = resultSet.getObject(column.value)
                    field.isAccessible = true
                    field.set(newItem, item)
                }
            }
            objectList.add(newItem)
        }

        resultSet.close()
        statement.close()
        connection.close()
        return objectList
    }

    fun <T: Any> update(foundClass: T, conditions: String) : Boolean {
        val connection = DatabaseConnection().databaseConnection
        val classType = foundClass::class.java

        val tableName = classType.getAnnotation(Table::class.java).value

        val marks = StringBuilder()
        for (field in classType.fields) {
            val column = field.getAnnotation(Column::class.java)
            if (column != null && field.getAnnotation(ID::class.java) == null) {
                marks.append(column.value).append(" = '"+field.get(foundClass)+"'").append(",")
            }
        }
        marks.deleteCharAt(marks.length - 1)
        val formattedMarks = marks.toString()

        val query = "UPDATE $tableName SET $formattedMarks$conditions"

        val statement = connection.prepareStatement(query)

        val rowUpdated = statement.executeUpdate() > 0
        statement.close()
        connection.close()
        return rowUpdated
    }

    fun <T: Any> delete(foundClass: T) : Boolean {
        val connection = DatabaseConnection().databaseConnection
        val classType = foundClass::class.java

        val tableName = classType.getAnnotation(Table::class.java).value
        val field = classType.declaredFields.filter { s -> s.getAnnotation(ID::class.java)!= null }[0]
        field.isAccessible = true
        val id = field.get(foundClass) as Long
        val query = "DELETE FROM $tableName WHERE id = ?"

        val statement = connection.prepareStatement(query)
        statement.setInt(1, id.toInt())

        val rowDeleted = statement.executeUpdate() > 0

        statement.close()
        connection.close()
        return rowDeleted
    }

    fun <T: Any> insert(foundClass: T) : Boolean {
        val connection = DatabaseConnection().databaseConnection
        val classType = foundClass::class.java

        val tableName = classType.getAnnotation(Table::class.java).value

        val values = StringBuilder()
        val columns = StringBuilder()
        for (field in classType.fields) {
            val column = field.getAnnotation(Column::class.java)
            if (column != null && field.getAnnotation(ID::class.java) == null) {
                values.append("'"+field.get(foundClass)+"'").append(",")
                columns.append(column.value).append(",")
            }
        }
        columns.deleteCharAt(columns.length - 1)
        values.deleteCharAt(values.length - 1)
        val formattedColumns = columns.toString()
        val formattedValues = values.toString()

        val query = "INSERT INTO $tableName ($formattedColumns) VALUES ($formattedValues)"

        val statement = connection.prepareStatement(query)

        val rowInserted = statement.executeUpdate() > 0

        statement.close()
        connection.close()
        return rowInserted

    }
}