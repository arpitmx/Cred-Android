package com.cred.cool.loan.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cred.cool.R
import com.cred.cool.core.Utility
import com.cred.cool.core.Utility.fadeIn
import com.cred.cool.core.Utility.fadeOut
import com.cred.cool.core.domain.ServerResponse
import com.cred.cool.databinding.ActivityMainBinding
import com.cred.cool.loan.domain.models.Page
import com.cred.cool.loan.presentation.fragments.EmiPlanSelectionBottomSheet
import com.cred.cool.loan.presentation.viewmodels.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.tankery.lib.circularseekbar.CircularSeekBar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            EmiPlanSelectionBottomSheet().show(supportFragmentManager, "SecondSheet")
        }

        setupObservers()

    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(amountSelectionPage : Page) {

        if (amountSelectionPage==null){
            Snackbar.make(binding.root,"Error",Snackbar.LENGTH_LONG).show()
            return
        }

        binding.selectedCreditAmonut.text = "₹${
            Utility.formatNumberWithCommas(
                sharedViewModel.getSelectedCreditAmount().toString()
            )
        }"


        with(binding){

            title.text = amountSelectionPage.openStateTitle
            description.text = amountSelectionPage.openStateSubtitle
            interestRate.text = amountSelectionPage.cardDescription
            seekbarDesc.text = amountSelectionPage.openStateFooter
            titleCreditAmount.text = amountSelectionPage.cardHeader

            with(circularSeekbar) {
                circleStrokeWidth = 30F
                circleProgressColor = resources.getColor(R.color.orange_200)
                pointerColor = resources.getColor(R.color.orange_100)
                pointerStrokeWidth = 100F
                circularSeekbar.max = amountSelectionPage.cardMaxRange!!.toFloat()
            }
        }
    }

    private fun setupObservers() {

        lifecycleScope.launch {
            sharedViewModel.loanData.collect{ serverResponse->

                when(serverResponse){
                    is ServerResponse.Failure -> {
                        Snackbar.make(binding.root, serverResponse.reason, Snackbar.LENGTH_LONG).show()
                    }
                    ServerResponse.Loading -> {
                        binding.progress.fadeIn()
                    }
                    is ServerResponse.Success -> {
                        sharedViewModel.pageList = sharedViewModel.createPageList(serverResponse.data.items)
                        val page = sharedViewModel.pageList[0]
                        setupViews(page)
                        setValidationObserver()
                        binding.progress.fadeOut()

                    }
                }
                }
            }



        binding.circularSeekbar.setOnSeekBarChangeListener(object :
            CircularSeekBar.OnCircularSeekBarChangeListener {
            override fun onProgressChanged(
                circularSeekBar: CircularSeekBar?,
                progress: Float,
                fromUser: Boolean
            ) {
                sharedViewModel.setSelectedCreditAmount(progress.toInt())
                binding.selectedCreditAmonut.text = "₹ ${
                    Utility.formatNumberWithCommas(
                        sharedViewModel.getSelectedCreditAmount().toString()
                    )
                }"

            }

            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
            }

        })


        sharedViewModel.isFirstBottomSheetOpen.observe(this) { isOpen ->
            when (isOpen) {
                true -> {
                    binding.titleParent.fadeOut()
                    binding.selectionParent.fadeIn()
                    binding.creditAmount.text = "₹ ${
                        Utility.formatNumberWithCommas(
                            sharedViewModel.getSelectedCreditAmount().toString()
                        )
                    }"
                }

                false -> {
                    binding.titleParent.fadeIn()
                    binding.selectionParent.fadeOut()
                }
            }
        }
    }

    fun setValidationObserver(){
        sharedViewModel.selectedCreditAmount.observe(this){selectedAmount->
            if (selectedAmount< sharedViewModel.pageList[0].cardMinRange!!){
                setButtonState(binding.button,false)
            }else{
                setButtonState(binding.button,true)
            }
        }
    }

    fun setButtonState(button: Button, isEnabled: Boolean) {
        if (isEnabled) {
            button.isEnabled = true
            button.setBackground(resources.getDrawable(R.drawable.button_bg))
            button.setTextColor(Color.WHITE)
        } else {
            button.isEnabled = false
            button.setBackground(resources.getDrawable(R.drawable.button_bg_disabled))
            button.setTextColor(Color.DKGRAY)
        }


    }
}

