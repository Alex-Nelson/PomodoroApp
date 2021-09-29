package com.example.pomodorotimerapplication.data

import androidx.room.*

@Entity(tableName = "task_item_table")
data class TaskItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    val itemId: Long = 0L,

    @ColumnInfo(name = "taskId")
    val taskId: Long,

    @ColumnInfo(name = "item_string")
    var itemStr: String
)

/**
 * The relationship between a task and a task item
 * */
data class TaskWithTaskItems(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val taskItem: List<TaskItem>
)

