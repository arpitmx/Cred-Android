package com.cred.cool.loan.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cred.cool.core.Utility.gone
import com.cred.cool.core.Utility.show
import com.cred.cool.databinding.BankAccountItemBinding
import com.cred.cool.loan.domain.models.BankAccount

class BankAccountAdapter(val bankAccounts : MutableList<BankAccount>, val selectedBankAccount : (Int)-> Unit) :
    RecyclerView.Adapter<BankAccountAdapter.BankAccountViewHolder>() {

    inner class BankAccountViewHolder(val binding: BankAccountItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data : BankAccount){
            binding.bankName.text = data.name
            binding.accountNumber.text = data.number

            binding.root.setOnClickListener{
                selectedBankAccount(adapterPosition)
            }

            if (data.isSelected){
                binding.checkIcon.show()
                binding.uncheckIcon.gone()
            }else{
                binding.checkIcon.gone()
                binding.uncheckIcon.show()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankAccountViewHolder {
        return BankAccountViewHolder(BankAccountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = bankAccounts.size

    override fun onBindViewHolder(holder: BankAccountViewHolder, position: Int) {
        holder.bind(bankAccounts[position])
    }

    fun updateSelected(pos: Int) {
        bankAccounts.forEachIndexed { index, bankAccount ->
            if (pos == index){
                bankAccount.isSelected = !bankAccount.isSelected
            }else{
                bankAccount.isSelected = false
            }
        }

        notifyDataSetChanged()
    }

}