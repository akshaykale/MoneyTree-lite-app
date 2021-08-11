package com.getmoneytree.moneytreelite.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.getmoneytree.moneytreelight.ui.BaseFragment
import com.getmoneytree.moneytreelite.R
import com.getmoneytree.moneytreelite.databinding.FragmentAccountsBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

class AccountsFragment : BaseFragment() {

    private lateinit var binding: FragmentAccountsBinding

    @ExperimentalCoroutinesApi
    private val viewModel: AccountsViewModel by activityViewModels()

    private lateinit var mAdapter: AccountsAdapter

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()

        viewModel.loadData()

        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is AccountsViewModel.AccountsEvents.AccountsLoaded -> {
                    loadView(it.data)
                }
                is AccountsViewModel.AccountsEvents.OnAddAccountClicked -> {

                }
                is AccountsViewModel.AccountsEvents.OnAccountClicked -> {
                    navigateSafe(AccountsFragmentDirections.actionAccountsFragmentToTransactionsFragment(it.account))
                }
                else -> Unit
            }
        }
    }

    private fun setUpView() {
        mAdapter = AccountsAdapter(viewModel)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
    }

    private fun loadView(data: List<InstitutionAccounts>) {
        mAdapter.submitList(data)
    }

    private fun navigateSafe(action: NavDirections) {
        val frag = findNavController()
        if (frag.currentDestination?.id == R.id.accountsFragment) {
            frag.navigate(action)
        }
    }
}
