package com.cred.cool.loan.presentation.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cred.cool.R
import com.cred.cool.core.Utility.fadeInAndSlideUp
import com.cred.cool.core.Utility.getCalculatedHeight
import com.cred.cool.core.Utility.gone
import com.cred.cool.core.Utility.show
import com.cred.cool.loan.presentation.adapters.PlanAdapter
import com.cred.cool.core.Utility
import com.cred.cool.databinding.FirstbottomsheetlayoutBinding
import com.cred.cool.loan.domain.models.Page
import com.cred.cool.loan.presentation.viewmodels.SharedViewModel
import com.cred.cool.loan.domain.models.Plan
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmiPlanSelectionBottomSheet : BottomSheetDialogFragment() {

    val sharedViewModel: SharedViewModel by activityViewModels()


    val binding: FirstbottomsheetlayoutBinding by lazy {
        FirstbottomsheetlayoutBinding.inflate(layoutInflater)
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onStart() {
        super.onStart()


        dialog?.let { dialog ->

            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                dialog.window?.setDimAmount(0.3f)

                sharedViewModel.isFirstBottomSheetOpen.value = true
                bottomSheet.layoutParams?.height = getCalculatedHeight(requireContext(), 190)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            BankAccountSelectionBottomSheet().show(requireActivity().supportFragmentManager, "ThirdSheet")
        }

        val emiPage : Page? = sharedViewModel.pageList[1]?: null
        if (emiPage!=null){
            setupViews(emiPage)
            setupObservers()
        }else{
            Snackbar.make(binding.root,"Page not found", Snackbar.LENGTH_LONG).show()
            dismiss()
        }

    }

    lateinit var adapter: PlanAdapter
    private fun setupViews(emiPage : Page) {

        val emiPlanList = emiPage.repaymentItems

        if (emiPlanList== null){
            Snackbar.make(binding.root,"Page not found", Snackbar.LENGTH_LONG).show()
            dismiss()
            return
        }

        sharedViewModel.setPlanList(emiPlanList.toMutableList())
        adapter = PlanAdapter(sharedViewModel.getPlanList().toMutableList()){pos ->
            sharedViewModel.setSelectedEMIPlan(emiPlanList[pos])
            adapter.updateItem(pos)
        }

        binding.plansRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.plansRV.adapter = adapter
        binding.plansRV.fadeInAndSlideUp()

    }



    private fun setupObservers() {

        sharedViewModel.isSecondBottomSheetOpen.observe(viewLifecycleOwner) { isOpen ->
            when (isOpen) {
                true -> {
                    val selectedPlan = sharedViewModel.getSelectedEMIPlan()
                    selectedPlan?.let {plan->
                        binding.emiAmount.text = "₹${Utility.formatNumberWithCommas(plan.emi!!)} /mo"
                        binding.creditDuration.text = "${plan.duration} months"
                        binding.title.gone()
                        binding.description.gone()
                        binding.selectionParent.show()
                    }
                }

                false -> {
                    binding.title.show()
                    binding.description.show()
                    binding.selectionParent.gone()

                }
            }
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        sharedViewModel.isFirstBottomSheetOpen.value = false
    }
}