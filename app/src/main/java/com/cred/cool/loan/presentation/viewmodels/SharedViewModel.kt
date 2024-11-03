package com.cred.cool.loan.presentation.viewmodels

import Item
import UiResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cred.cool.core.domain.ServerResponse
import com.cred.cool.loan.domain.models.Page
import com.cred.cool.loan.domain.models.Plan
import com.cred.cool.loan.domain.models.RepaymentItem
import com.cred.cool.loan.domain.repository.MainRepository
import com.cred.cool.loan.domain.usecases.FetchLoanDataUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    val fetchLoanDataUseCase: FetchLoanDataUseCase
) : ViewModel() {

    val selectedCreditAmount : MutableLiveData<Int> = MutableLiveData(0)
    private var selectedEMIPlan: RepaymentItem? = null
    private val planList: MutableList<RepaymentItem> = mutableListOf()
    lateinit var fetchedLoanData : List<Item>


    fun getSelectedCreditAmount(): Int = selectedCreditAmount.value ?: 0
    fun setSelectedCreditAmount(amount: Int) = run { selectedCreditAmount.value = amount }


    fun getSelectedEMIPlan(): RepaymentItem? = selectedEMIPlan
    fun setSelectedEMIPlan(plan: RepaymentItem) = run { selectedEMIPlan = plan }

    fun getPlanList(): List<RepaymentItem> = planList
    fun setPlanList(list: MutableList<RepaymentItem>) = run {
        planList.clear()
        planList.addAll(list)
    }

    fun updatePlanList(pos: Int) {
        planList[pos].isSelected = !planList[pos].isSelected
    }

    val isFirstBottomSheetOpen = MutableLiveData(false)
    val isSecondBottomSheetOpen = MutableLiveData(false)

    val loanData: StateFlow<ServerResponse<UiResponse>> by lazy {
        fetchLoanData().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ServerResponse.Loading
        )
    }

    lateinit var pageList : List<Page>
    fun createPageList(items: List<Item>): List<Page> {
        return items.map { item ->
            Page(
                openStateTitle = item.open_state.body.title,
                openStateSubtitle = item.open_state.body.subtitle,
                cardHeader = item.open_state.body.card?.header,
                cardDescription = item.open_state.body.card?.description,
                cardMaxRange = item.open_state.body.card?.max_range,
                cardMinRange = item.open_state.body.card?.min_range,
                openStateFooter = item.open_state.body.footer,
                repaymentItems = item.open_state.body.items?.map { repaymentItem ->
                    RepaymentItem(
                        emi = repaymentItem.emi,
                        duration = repaymentItem.duration,
                        title = repaymentItem.title,
                        subtitle = repaymentItem.subtitle,
                        tag = repaymentItem.tag,
                        icon = repaymentItem.icon
                    )
                },
                closedStateKey1 = item.closed_state.body.key1,
                closedStateKey2 = item.closed_state.body.key2,
                ctaText = item.cta_text
            )
        }
    }


    private fun fetchLoanData(): Flow<ServerResponse<UiResponse>> = flow {
        emit(ServerResponse.Loading)
        emit(fetchLoanDataUseCase.execute())
    }.catch { e ->
        emit(ServerResponse.Failure(e.message ?: "Unknown error"))
    }


}