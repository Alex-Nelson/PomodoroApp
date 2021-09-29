package com.example.pomodorotimerapplication

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pomodorotimerapplication.data.*
import com.example.pomodorotimerapplication.utilities.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception
import kotlin.jvm.Throws

/**
 * Unit tests for the Room Database used in the app
 * */
@RunWith(AndroidJUnit4::class)
class RoomUnitTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: AppRepository

    private val task1 = Task(1, "Study for Physics III test", 25,
        5, 15, 4)
    private val task2 = Task(2, "Work on Algorithms Homework", 25,
    5, 15, 3)
    private val task3 = Task(3, "Review Compose library", 30,
    10, 20, 4)

    private val timer1 = RecentTimer(1, 1, "Session",
        960000, 1500000)
    private val timer2 = RecentTimer(2, 2, "Short Break",
        180000, 300000)

    private val item1 = TaskItem(1, 2, "Work on Dynamic Programming problems")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Create an in-memory version of the database
    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        repository = AppRepository(db.appDao)

        repository.addTask(task1)
        repository.addTask(task2)
        repository.addTask(task3)
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    /** Test inserting pomodoro tasks */
    @Test
    @Throws(Exception::class)
    fun testInsertAndReadTasks(){
        runBlocking {

            val result = getValue(repository.getAllTasks())

            assertThat(result.size, equalTo(3))

            assertThat(result[0], equalTo(task1))
            assertThat(result[1], equalTo(task2))
            assertThat(result[2], equalTo(task3))
        }
    }

    @Test
    fun testUpdateTask(){
        runBlocking {
            val newTask1 = repository.retrieveTask(1)
            newTask1.sessionLen = 30

            repository.updateTask(newTask1)

            val result = repository.retrieveTask(1)
            assertThat(newTask1, equalTo(result))
        }
    }

    @Test
    fun testSaveTimer(){
        runBlocking {
            repository.addTimer(timer1)
            repository.addTimer(timer2)

            val result1 = repository.getTaskTimer(1).recentTimer
            val result2 = repository.getTaskTimer(2)

            assertThat(result1, equalTo(timer1))
            assertThat(result2.recentTimer, equalTo(timer2))
        }
    }

    @Test
    fun testSaveTaskItem(){
        runBlocking {
            repository.addItem(item1)

            val result = getValue(repository.getAllTaskItems(2))

            assertThat(result[0].taskItem.size, equalTo(1))

            assertThat(result[0].taskItem[0], equalTo(item1))
        }
    }
}