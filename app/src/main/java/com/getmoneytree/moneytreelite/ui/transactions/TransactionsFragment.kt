package com.getmoneytree.moneytreelite.ui.transactions

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.getmoneytree.moneytreelight.ui.BaseFragment
import com.getmoneytree.moneytreelite.R
import com.getmoneytree.moneytreelite.data.models.MAccount
import com.getmoneytree.moneytreelite.data.models.MTransaction
import com.getmoneytree.moneytreelite.databinding.DialogDeleteBinding
import com.getmoneytree.moneytreelite.databinding.FragmentTransactionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class TransactionsFragment : BaseFragment() {

    private lateinit var binding: FragmentTransactionsBinding
    private val viewModel: TransactionsViewModel by viewModels()

    private lateinit var account: MAccount

    private lateinit var mAdapter: TransactionsAdapter

    private var deleteDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            account = TransactionsFragmentArgs.fromBundle(it).account
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.account = account
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()

        viewModel.loadData(account)

        viewModel.backPress.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is TransactionsViewModel.TransactionsEvents.TransactionsLoaded -> {
                    loadView(it.data)
                }
                is TransactionsViewModel.TransactionsEvents.DeleteTransaction -> {
                    showDeleteConfirmationDialog(it.transaction)?.show()
                }
                is TransactionsViewModel.TransactionsEvents.DeleteTransactionSuccess -> {
                    deleteDialog?.dismiss()
                }
                else -> Unit
            }
        }
    }

    private fun showDeleteConfirmationDialog(transaction: MTransaction): Dialog? {
        return context?.let {
            deleteDialog = BottomSheetDialog(it)
            val b = DialogDeleteBinding.inflate(LayoutInflater.from(it))
            b.viewModel = viewModel
            b.transaction = transaction
            deleteDialog?.setContentView(b.root)

            b.dismiss.setOnClickListener {
                deleteDialog?.dismiss()
            }
            deleteDialog
        }
    }

    private fun setUpView() {
        mAdapter = TransactionsAdapter(viewModel)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
    }

    private fun loadView(data: List<TransactionItem>) {
        mAdapter.submitList(data)
    }
}