package com.getmoneytree.moneytreelite.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.getmoneytree.moneytreelite.data.LocalDb
import com.getmoneytree.moneytreelite.data.models.MAccount
import com.getmoneytree.moneytreelite.data.models.MTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
@SmallTest
class TransactionsDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var database: LocalDb

    @ExperimentalCoroutinesApi
    @Before
    fun initDb() {
        Dispatchers.setMain(testDispatcher)
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            LocalDb::class.java
        ).allowMainThreadQueries().build()
    }

    @ExperimentalCoroutinesApi
    @After
    fun closeDb(){
        database.close()

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test all operations in transaction DAO`() = runBlockingTest {

        val t1 = MTransaction(id = 1, account_id = 1)
        val t2 = MTransaction(id = 2, account_id = 1)
        val t3 = MTransaction(id = 3, account_id = 2)
        val ts = mutableListOf<MTransaction>()
        ts.addAll(listOf(t1, t2, t3))

        database.getTransactionDao().addTransactions(ts)

        val loaded = database.getTransactionDao().getTransaction(1)

        assertThat<MTransaction>(loaded as MTransaction, notNullValue())
        assertThat(loaded.id, `is`(t1.id))

        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {
            database.getTransactionDao().getTransactions(accountId = 1).collect {
                assertThat(it.size, `is`(2))
                latch.countDown()
            }
        }
        latch.await()
        job.cancel()
    }
}
