package com.cred.cool.loan.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cred.cool.core.Utility
import com.cred.cool.core.Utility.gone
import com.cred.cool.core.Utility.show
import com.cred.cool.databinding.PlanItemBinding
import com.cred.cool.loan.domain.models.Plan
import com.cred.cool.loan.domain.models.RepaymentItem

class PlanAdapter(val planList:MutableList<RepaymentItem>, val onPlanClick: (Int)-> Unit) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(val binding:PlanItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data : RepaymentItem){
            binding.priceText.text = "₹${Utility.formatNumberWithCommas(data.emi!!)}"
            binding.durationText.text = "for ${data.duration} months"

            binding.root.setOnClickListener {
                onPlanClick(adapterPosition)
            }

            if (data.tag!=null){
                binding.recommendedText.show()
            }else{
                binding.recommendedText.gone()
            }

            if (data.isSelected){
                binding.checkIcon.show()
                binding.uncheckIcon.gone()
            }else{
                binding.uncheckIcon.show()
                binding.checkIcon.gone()
            }
        }
    }

    fun updateItem(pos : Int){
        planList.forEachIndexed{index, plan->
            if (index == pos){
                plan.isSelected = !plan.isSelected
            }else {
                plan.isSelected = false
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        return PlanViewHolder(PlanItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val data = planList[position]
        holder.bind(data)
    }
}