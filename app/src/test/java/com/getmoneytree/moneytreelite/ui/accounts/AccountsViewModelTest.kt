package com.getmoneytree.moneytreelite.ui.accounts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.getmoneytree.moneytreelite.data.dao.AccountDao
import com.getmoneytree.moneytreelite.data.dao.AccountsDaoTest
import com.getmoneytree.moneytreelite.data.models.Currency
import com.getmoneytree.moneytreelite.data.models.MAccount
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class AccountsViewModelTest : TestCase() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    //@get:Rule
    //val coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var mockObserver: Observer<AccountsViewModel.AccountsEvents>

    private lateinit var viewModel: AccountsViewModel

    @Mock
    private lateinit var handle: SavedStateHandle

    @Mock
    private lateinit var accountsDao: AccountDao

    @Captor
    private lateinit var captor: ArgumentCaptor<AccountsViewModel.AccountsEvents>

    private var viewState: AccountsViewModel.AccountsEvents? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = AccountsViewModel()
    }

    @Test
    fun loadData() = runBlockingTest {

        val account1 = MAccount(id = 1, institution = "A")
        val account2 = MAccount(id = 2, institution = "A")
        val account3 = MAccount(id = 3, institution = "B")
        val accounts = mutableListOf<MAccount>()
        accounts.addAll(listOf(account1, account2, account3))

        val flow = flow {
            delay(10)
            emit(accounts)
        }

        `when`(accountsDao.getAccounts()).thenReturn(flow)

        val liveData = viewModel.events
        liveData.observeForever(mockObserver)

        verify(mockObserver).onChanged(captor.capture())

        val res = mutableListOf<InstitutionAccounts>()
        res.add(InstitutionAccounts(
            institutionName = "A",
            accounts = listOf(account1, account2),
            amount = 0.0
        ))
        res.add(InstitutionAccounts(
            institutionName = "B",
            accounts = listOf(account3),
            amount = 0.0
        ))

        assertEquals(AccountsViewModel.AccountsEvents.AccountsLoaded(totalAmount = 0.0, currency = Currency.JPY.value, data = res), captor.value)

        verify(mockObserver, times(1)).onChanged(captor.capture())

    }
}