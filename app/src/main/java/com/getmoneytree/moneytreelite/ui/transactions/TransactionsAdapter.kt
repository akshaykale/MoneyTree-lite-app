package com.getmoneytree.moneytreelite.ui.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.getmoneytree.moneytreelite.data.models.Currency
import com.getmoneytree.moneytreelite.data.models.MTransaction
import com.getmoneytree.moneytreelite.databinding.CellTransactionBinding
import com.getmoneytree.moneytreelite.databinding.CellTransactionHeaderBinding

class TransactionsAdapter(
    private val viewModel: TransactionsViewModel
): ListAdapter<TransactionItem, RecyclerView.ViewHolder>(AccountItemCallback()) {

    class TransactionHeaderViewHolder(val binding: CellTransactionHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            transactionItem: TransactionItem,
            viewModel: TransactionsViewModel,
            viewBinding: CellTransactionHeaderBinding
        ) {
            viewBinding.viewModel = viewModel
            viewBinding.transaction = transactionItem
        }
    }

    class TransactionsViewHolder(val binding: CellTransactionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            transactionItem: TransactionItem,
            viewModel: TransactionsViewModel,
            viewBinding: CellTransactionBinding
        ) {
            viewBinding.viewModel = viewModel
            viewBinding.transaction = transactionItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) { //header
            val binding = CellTransactionHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TransactionHeaderViewHolder(binding)
        } else { //transaction
            val binding = CellTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TransactionsViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isHeader) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when(holder) {
                is TransactionHeaderViewHolder -> {
                    holder.onBind(it, viewModel, holder.binding)
                }
                is TransactionsViewHolder -> {
                    holder.onBind(it, viewModel, holder.binding)
                }
            }
        }
    }

    class AccountItemCallback : DiffUtil.ItemCallback<TransactionItem>() {
        override fun areItemsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
            return oldItem.diff == newItem.diff
        }

        override fun areContentsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
            return oldItem == newItem
        }
    }
}

data class TransactionItem(
    val diff: Int,
    val isHeader: Boolean = false,

    val accountId: Int,
    val transaction: MTransaction? = null,

    //used for header
    val titleText: String = "",
    val amountIn: Double = 0.0,
    val amountOut: Double = 0.0,
    val currency: String = Currency.JPY.value
)