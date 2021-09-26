package com.egorvivanov.basictodoapp.adapter

import androidx.recyclerview.widget.RecyclerView

import android.widget.CheckBox
import android.widget.ImageView

import com.egorvivanov.basictodoapp.R
import com.egorvivanov.basictodoapp.room.entity.ToDoTask


class ToDoRvAdapter : RecyclerView.Adapter<ToDoRvAdapter.TaskViewHolder>() {

    private var tasks: ArrayList<ToDoTask> = ArrayList()
    private lateinit var mDeleteListener: MyAdapterListener

    override fun onCreateViewHolder(parent: android.view.ViewGroup, type: Int): TaskViewHolder {
        return TaskViewHolder(parent)
    }

    // Формируем ViewHolder
    inner class TaskViewHolder(parent: android.view.ViewGroup) :
        RecyclerView.ViewHolder(
            android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todo, parent, false)
        ) {

        fun bind(task: ToDoTask, position: Int) = with(itemView) {
            val taskCheckBox = findViewById<CheckBox>(R.id.check_todo)
            val deleteTodo = findViewById<ImageView>(R.id.btn_delete)

            taskCheckBox.text = task.todoTask
            deleteTodo.setOnClickListener {
                mDeleteListener.onDeleteViewClick(position)
            }
        }
    }

    fun setData(tasks: ArrayList<ToDoTask>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun addTodo(todoTask: ToDoTask) {
        this.tasks.add(todoTask)
        notifyItemChanged(tasks.size)
    }

    override fun onBindViewHolder(viewHolder: ToDoRvAdapter.TaskViewHolder, position: Int) {
        viewHolder.bind(tasks[position], position)
    }

    override fun getItemCount(): Int = tasks.size

    fun setDeleteListener(deleteListener: MyAdapterListener) {
        this.mDeleteListener = deleteListener
    }

    interface MyAdapterListener {
        fun onDeleteViewClick(position: Int)
    }
}