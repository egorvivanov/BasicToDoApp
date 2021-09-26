package com.egorvivanov.basictodoapp.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_table")
data class ToDoTask(
    @ColumnInfo(name = "todo_task")
    var todoTask: String
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}