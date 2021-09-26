package com.egorvivanov.basictodoapp


import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.text.TextUtils

import com.egorvivanov.basictodoapp.adapter.ToDoRvAdapter
import com.egorvivanov.basictodoapp.room.dao.ToDoTaskDao
import com.egorvivanov.basictodoapp.room.entity.ToDoTask
import com.egorvivanov.basictodoapp.room.database.ToDoDatabase


class ToDoActivity : AppCompatActivity() {

    // Room Database instance
    private lateinit var dbInstance: RoomDatabase

    // DAO object для выполнения операций с базой данных
    private lateinit var taskDaoTodo: ToDoTaskDao

    private lateinit var allTodos: ArrayList<ToDoTask>

    private val compositeDisposable = CompositeDisposable()

    private lateinit var editTextTodo: EditText
    private lateinit var btnAddTodo: Button

    private lateinit var toRecyclerAdapter: ToDoRvAdapter
    private lateinit var recyclerViewTodo: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        initDatabase()
        initViews()

        allTodos = ArrayList()

        recyclerViewTodo.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        recyclerViewTodo.adapter = toRecyclerAdapter

        getAllTodos()
    }


    // Метод для инициализации всех View
    private fun initViews() {
        editTextTodo = findViewById(R.id.et_todo)
        btnAddTodo = findViewById(R.id.btn_add_todo)
        recyclerViewTodo = findViewById(R.id.todo_recycler_view)

        btnAddTodo.setOnClickListener {
            if (!TextUtils.isEmpty(editTextTodo.text)) {
                addNewTask(editTextTodo.text.toString())
            }
        }
    }


    // Метод для инициализации Room сущностей
    private fun initDatabase() {
        dbInstance = databaseBuilder(
            applicationContext,
            ToDoDatabase::class.java, "todo_database"
        ).build()

        taskDaoTodo = (dbInstance as ToDoDatabase).taskToDo()
        toRecyclerAdapter = ToDoRvAdapter()
        toRecyclerAdapter.setDeleteListener(object : ToDoRvAdapter.MyAdapterListener {
            // Получаем id To do задачи для удаления
            override fun onDeleteViewClick(position: Int) {
                deleteFromDb(allTodos[position], position)
            }
        })
    }


    // Метод для удаления задачи To Do из базы данных
    private fun deleteFromDb(taskToDelete: ToDoTask, position: Int) {
        compositeDisposable.add(Observable.fromCallable {
            taskDaoTodo.deleteToDoTask(taskToDelete)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                allTodos.removeAt(position)
                toRecyclerAdapter.notifyItemRemoved(position)
            })
    }


    // Метод для добавления новой задачи To Do
    private fun addNewTask(toDoDetails: String) {
        val newTask = ToDoTask(toDoDetails)

        compositeDisposable.add(Observable.fromCallable {
            taskDaoTodo.addToDoTask(newTask)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                // Добавляем новую задачу To Do в список allTodos
                // объявляем об изменении количество задач To Do
                // и скроллим к последней в списке задаче To Do
                allTodos.add(newTask)
                toRecyclerAdapter.notifyItemChanged(allTodos.size)

                (allTodos.size - 1).takeIf { it >= 0 }
                    ?.let {
                        recyclerViewTodo.smoothScrollToPosition(it)
                    }
            })
    }


    // Метод для обновления задач To Do
    private fun updateList(todoId: Long) {

        TODO("можно добавить возможность обновлять/редактировать задачу ToDo по нажатию")

    }

    // Метод для добавления всех To Do задач из базы данных
    private fun getAllTodos() {
        compositeDisposable.add(
            (dbInstance as ToDoDatabase).taskToDo().allToDoTask()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    allTodos.clear()
                    allTodos.addAll(it)
                    toRecyclerAdapter.setData(allTodos)
                }
        )
    }


    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}