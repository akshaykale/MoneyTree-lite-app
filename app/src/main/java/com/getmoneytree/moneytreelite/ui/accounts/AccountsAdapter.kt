package com.getmoneytree.moneytreelite.ui.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.getmoneytree.moneytreelite.data.models.Currency
import com.getmoneytree.moneytreelite.data.models.MAccount
import com.getmoneytree.moneytreelite.databinding.CellAccountBinding
import com.getmoneytree.moneytreelite.databinding.CellInstitutionBinding

class AccountsAdapter(
    private val viewModel: AccountsViewModel
): ListAdapter<InstitutionAccounts, AccountsAdapter.AccountsViewHolder>(AccountItemCallback()) {


    class AccountsViewHolder(val binding: CellInstitutionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            institutionAccounts: InstitutionAccounts,
            position: Int,
            viewModel: AccountsViewModel,
            viewBinding: CellInstitutionBinding
        ) {
            viewBinding.viewModel = viewModel
            viewBinding.institutionName = institutionAccounts.institutionName

            if (viewBinding.accountsContainer.childCount > 0) {
                viewBinding.accountsContainer.removeAllViews()
            }

            repeat(institutionAccounts.accounts.size) {
                inflateView(viewBinding.accountsContainer, viewModel, institutionAccounts.accounts[it])
            }
        }

        private fun inflateView(
            accountsContainer: LinearLayout,
            _viewModel: AccountsViewModel,
            mAccount: MAccount
        ) {
            CellAccountBinding.inflate(LayoutInflater.from(accountsContainer.context), accountsContainer, true).apply {
                viewModel = _viewModel
                account = mAccount
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        val binding = CellInstitutionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.onBind(it, position, viewModel, holder.binding)
        }
    }

    class AccountItemCallback : DiffUtil.ItemCallback<InstitutionAccounts>() {
        override fun areItemsTheSame(oldItem: InstitutionAccounts, newItem: InstitutionAccounts): Boolean {
            return oldItem.institutionName == newItem.institutionName
        }

        override fun areContentsTheSame(oldItem: InstitutionAccounts, newItem: InstitutionAccounts): Boolean {
            return oldItem == newItem
        }
    }
}

data class InstitutionAccounts(
    val institutionName: String,
    val accounts: List<MAccount> = listOf(),
    val amount: Double = 0.0,
    val currency: String = Currency.JPY.value
)