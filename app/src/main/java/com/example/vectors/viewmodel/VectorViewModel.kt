package com.example.vectors.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vectors.data.repository.VectorRepository
import com.example.vectors.domain.model.Vector2D
import com.example.vectors.domain.usecase.VectorOperations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VectorViewModel @Inject constructor(
    private val repository: VectorRepository,
    private val operations: VectorOperations
) : ViewModel() {

    val vectors = repository.getAllVectors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _resultVector = MutableStateFlow<Vector2D?>(null)
    val resultVector = _resultVector.asStateFlow()

    private val _operationResult = MutableStateFlow<Vector2D?>(null)
    val operationResult: StateFlow<Vector2D?> = _operationResult.asStateFlow()

    private val _infoMessage = MutableStateFlow("")
    val infoMessage = _infoMessage.asStateFlow()

    fun addVector(v: Vector2D) = viewModelScope.launch { repository.insertVector(v) }
    fun deleteVector(id: Long) = viewModelScope.launch { repository.deleteVector(id) }
    fun clearAll() = viewModelScope.launch { repository.clearAll() }

    fun setOperationResult(v: Vector2D?) { _resultVector.value = v }
    fun clearResult() { _resultVector.value = null; _infoMessage.value = "" }

    fun normalize(v: Vector2D) { _resultVector.value = v.normalize() }
    fun dotProduct(v1: Vector2D, v2: Vector2D) {
        val res = operations.dotProduct(v1, v2)
        _infoMessage.value = "Скалярний добуток: $res"
    }
}