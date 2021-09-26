package com.egorvivanov.basictodoapp.room.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

import com.egorvivanov.basictodoapp.room.entity.ToDoTask

import io.reactivex.rxjava3.core.Observable

@Dao
interface ToDoTaskDao {

    // Получаем все задачи To Do
    @Query("select * from todo_table")
    fun allToDoTask(): Observable<List<ToDoTask>>

    // Получаем To Do задачу по определенному id
    @Query("select * from todo_table where id = :todo_id")
    fun getTaskUsingId(todo_id: Long): Observable<ToDoTask>

    // Добавление задачи
    @Insert(onConflict = REPLACE)
    fun addToDoTask(todoTask: ToDoTask): Long

    // Обновление задачи
    @Update(onConflict = REPLACE)
    fun updateToDoTask(todoTask: ToDoTask)

    // Удаление задачи
    @Delete()
    fun deleteToDoTask(todoTask: ToDoTask)
}