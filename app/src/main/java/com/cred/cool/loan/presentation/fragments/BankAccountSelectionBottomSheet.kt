package com.cred.cool.loan.presentation.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cred.cool.R
import com.cred.cool.core.Utility.getCalculatedHeight
import com.cred.cool.loan.presentation.adapters.BankAccountAdapter
import com.cred.cool.databinding.SecondbottomsheetlayoutBinding
import com.cred.cool.loan.presentation.viewmodels.SharedViewModel
import com.cred.cool.loan.domain.models.BankAccount
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankAccountSelectionBottomSheet : BottomSheetDialogFragment() {

    val sharedViewModel: SharedViewModel by activityViewModels()

    private val binding: SecondbottomsheetlayoutBinding by lazy {
        SecondbottomsheetlayoutBinding.inflate(layoutInflater)
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
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.isDraggable = false
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                dialog.window?.setDimAmount(0.15f)
                sharedViewModel.isSecondBottomSheetOpen.value = true
                bottomSheet.layoutParams?.height = getCalculatedHeight(requireContext(), 290)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    lateinit var adapter: BankAccountAdapter
    private fun setupViews() {
        val bankAccounts = mutableListOf(
            BankAccount("Paytm Bank","","9451243978"),
            BankAccount( "HDFC Bank","","8887358051"),
            BankAccount( "State Bank of India","","8887358051"), BankAccount( "IDBI Bank","","8887358051"),
            BankAccount("Alok Bank","","9451243978"),
            BankAccount("Siddhartha Bank","","9451243978")
        )
        adapter = BankAccountAdapter(bankAccounts){ pos ->
            adapter.updateSelected(pos)
        }

        binding.plansRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.plansRV.adapter = adapter
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        sharedViewModel.isSecondBottomSheetOpen.value = false
    }
}