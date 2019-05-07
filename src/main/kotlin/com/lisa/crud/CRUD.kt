package com.lisa.crud

import com.lisa.crud.annotation.Column
import com.lisa.crud.annotation.ID
import com.lisa.crud.annotation.Table

class CRUD<T: Any> {

    fun select(classType: Class<T>, conditions: String) : ArrayList<T> {
        val connection = DatabaseConnection().databaseConnection

        val objectList = ArrayList<T>()

        try {
            val tableName = classType.getAnnotation(Table::class.java).value
            val query = "SELECT * FROM $tableName$conditions"

            val statement = connection.prepareStatement(query)
            val resultSet = statement.executeQuery()


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
        } catch (e: Exception) {
            Notification().open()
        }
        return objectList
    }

    fun update(foundClass: T, conditions: String) : Boolean {
        val connection = DatabaseConnection().databaseConnection
        val classType = foundClass::class.java

        val tableName = classType.getAnnotation(Table::class.java).value

        val marks = StringBuilder()
        for (field in classType.declaredFields) {
            field.isAccessible = true
            val column = field.getAnnotation(Column::class.java)
            if (column != null && field.getAnnotation(ID::class.java) == null) {
                marks.append(column.value).append(" = '"+field.get(foundClass)+"'").append(",")
            }
        }
        marks.deleteCharAt(marks.length - 1)
        val formattedMarks = marks.toString()

        try {

            val query = "UPDATE $tableName SET $formattedMarks$conditions"

            val statement = connection.prepareStatement(query)

            val rowUpdated = statement.executeUpdate() > 0
            statement.close()
            connection.close()
            return rowUpdated
        } catch (e: Exception) {
            Notification().open()
        }
        return false
    }

    fun delete(foundClass: T) : Boolean {
        val connection = DatabaseConnection().databaseConnection
        val classType = foundClass::class.java

        val tableName = classType.getAnnotation(Table::class.java).value
        val field = classType.declaredFields.filter { s -> s.getAnnotation(ID::class.java)!= null }[0]
        field.isAccessible = true
        val id = field.get(foundClass) as Long
        val query = "DELETE FROM $tableName WHERE id = ?"

        try {

            val statement = connection.prepareStatement(query)
            statement.setInt(1, id.toInt())

            val rowDeleted = statement.executeUpdate() > 0

            statement.close()
            connection.close()
            return rowDeleted
        } catch (e: Exception) {
            Notification().open()
        }
        return false
    }

    fun insert(foundClass: T) : Boolean {
        val connection = DatabaseConnection().databaseConnection
        val classType = foundClass::class.java

        val tableName = classType.getAnnotation(Table::class.java).value

        val values = StringBuilder()
        val columns = StringBuilder()
        for (field in classType.declaredFields) {
            val column = field.getAnnotation(Column::class.java)
            if (column != null && field.getAnnotation(ID::class.java) == null) {
                field.isAccessible = true
                values.append("'"+field.get(foundClass)+"'").append(",")
                columns.append(column.value).append(",")
            }
        }
        columns.deleteCharAt(columns.length - 1)
        values.deleteCharAt(values.length - 1)
        val formattedColumns = columns.toString()
        val formattedValues = values.toString()

        val query = "INSERT INTO $tableName ($formattedColumns) VALUES ($formattedValues)"

        try {

            val statement = connection.prepareStatement(query)

            val rowInserted = statement.executeUpdate() > 0

            statement.close()
            connection.close()
            return rowInserted
        } catch (e: Exception) {

            Notification().open()
        }
        return false
    }
}