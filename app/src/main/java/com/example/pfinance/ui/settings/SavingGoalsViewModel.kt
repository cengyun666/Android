package com.example.pfinance.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.data.local.dao.SavingGoalDao
import com.example.pfinance.domain.model.SavingGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavingGoalsState(val goals: List<SavingGoal> = emptyList())

@HiltViewModel
class SavingGoalsViewModel @Inject constructor(
    private val savingGoalDao: SavingGoalDao
) : ViewModel() {

    private val _state = MutableStateFlow(SavingGoalsState())
    val state: StateFlow<SavingGoalsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            savingGoalDao.getAllGoals().collect { list ->
                _state.value = SavingGoalsState(
                    goals = list.map {
                        SavingGoal(
                            id = it.id, name = it.name,
                            targetAmount = it.targetAmount,
                            currentAmount = it.currentAmount,
                            icon = it.icon, color = it.color,
                            deadline = it.deadline, note = it.note,
                            isCompleted = it.isCompleted
                        )
                    }
                )
            }
        }
    }
}
