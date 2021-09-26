package com.egorvivanov.basictodoapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.egorvivanov.basictodoapp.room.entity.ToDoTask
import com.egorvivanov.basictodoapp.room.dao.ToDoTaskDao


@Database(
    entities = arrayOf(ToDoTask::class),
    version = 1,
    exportSchema = false
)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun taskToDo(): ToDoTaskDao
}