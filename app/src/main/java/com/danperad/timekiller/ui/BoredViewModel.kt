package com.danperad.timekiller.ui

import androidx.lifecycle.ViewModel
import com.danperad.timekiller.models.ActivityFilter
import com.danperad.timekiller.models.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BoredViewModel : ViewModel() {

    private val repository: ActivityRepository = ActivityRepository()
    private val _uiState = MutableStateFlow(BoredUiState(ActivityFilter()))
    val uiState: StateFlow<BoredUiState> = _uiState.asStateFlow()

    suspend fun findActivity() {
        val response = repository.findActivityByFilter(_uiState.value.activityFilter)
        _uiState.update { BoredUiState(it.activityFilter, response) }
    }

    fun checkResult() = _uiState.value.activity !=null && _uiState.value.activity!!.isSuccess

    fun cleanResult(){
        _uiState.update { BoredUiState(it.activityFilter) }
    }

    fun changeAccessibility(accessibility: Double) {
        _uiState.update {
            BoredUiState(
                ActivityFilter(
                    accessibility,
                    it.activityFilter.type,
                    it.activityFilter.participants,
                    it.activityFilter.price
                ), it.activity
            )
        }
    }

    fun changeType(type: String) {
        _uiState.update {
            BoredUiState(
                ActivityFilter(
                    it.activityFilter.accessibility,
                    type,
                    it.activityFilter.participants,
                    it.activityFilter.price
                ), it.activity
            )
        }
    }

    fun changeParticipants(participants: Int) {
        _uiState.update {
            BoredUiState(
                ActivityFilter(
                    it.activityFilter.accessibility,
                    it.activityFilter.type,
                    participants,
                    it.activityFilter.price
                ), it.activity
            )
        }
    }

    fun changePrice(price: Double) {
        _uiState.update {
            BoredUiState(
                ActivityFilter(
                    it.activityFilter.accessibility,
                    it.activityFilter.type,
                    it.activityFilter.participants,
                    price
                ), it.activity
            )
        }
    }
}