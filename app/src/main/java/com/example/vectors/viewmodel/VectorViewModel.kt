package com.example.vectors.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vectors.data.repository.VectorRepository
import com.example.vectors.domain.model.Vector2D
import com.example.vectors.domain.usecase.VectorOperations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow // Додано
import kotlinx.coroutines.flow.asStateFlow    // Додано
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VectorViewModel @Inject constructor(
    private val repository: VectorRepository,
    private val operations: VectorOperations
) : ViewModel() {

    val vectors = repository.getAllVectors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 🔹 Використовуємо StateFlow замість звичайної змінної
    private val _resultVector = MutableStateFlow<Vector2D?>(null)
    val resultVector = _resultVector.asStateFlow()

    fun addVector(vector: Vector2D) {
        viewModelScope.launch {
            repository.insertVector(vector)
        }
    }

    fun deleteVector(id: Long) {
        viewModelScope.launch {
            repository.deleteVector(id)
        }
    }

    // 🔹 Оновлюємо значення через .value
    fun add(v1: Vector2D, v2: Vector2D) {
        _resultVector.value = operations.add(v1, v2)
    }

    // Додай цей метод, щоб очищати результат, якщо потрібно
    fun clearResult() {
        _resultVector.value = null
    }
}