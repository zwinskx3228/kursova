package com.example.vectors.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vectors.data.repository.VectorRepository
import com.example.vectors.domain.model.Vector2D
import com.example.vectors.domain.usecase.VectorOperations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VectorViewModel @Inject constructor(
    private val repository: VectorRepository,
    private val operations: VectorOperations
) : ViewModel() {

    // Стрім усіх векторів з БД
    val vectors = repository.getAllVectors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Стрім історії операцій
    val history = repository.getHistory()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _resultVector = MutableStateFlow<Vector2D?>(null)
    val resultVector = _resultVector.asStateFlow()

    private val _operationResult = MutableStateFlow<Vector2D?>(null)
    val operationResult: StateFlow<Vector2D?> = _operationResult.asStateFlow()

    private val _infoMessage = MutableStateFlow("")
    val infoMessage = _infoMessage.asStateFlow()

    // --- Робота з векторами ---
    fun addVector(v: Vector2D) = viewModelScope.launch {
        repository.insertVector(v)
    }

    fun deleteVector(id: Long) = viewModelScope.launch {
        repository.deleteVector(id)
    }

    fun clearAll() = viewModelScope.launch {
        repository.clearAll()
    }

    fun setOperationResult(v: Vector2D?) {
        _resultVector.value = v
    }

    fun clearResult() {
        _resultVector.value = null
        _infoMessage.value = ""
    }

    fun normalize(v: Vector2D) {
        _resultVector.value = v.normalize()
    }

    fun dotProduct(v1: Vector2D, v2: Vector2D) {
        val res = operations.dotProduct(v1, v2)
        _infoMessage.value = "Скалярний добуток: $res"
    }

    // --- Робота з історією ---
    fun saveOperationToHistory(opName: String, input: String, result: String) {
        viewModelScope.launch {
            repository.saveOperation(opName, input, result)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // --- JSON Логіка ---

    // Експорт: перетворює поточний список векторів у JSON-рядок
    fun exportData(): String {
        return repository.exportVectorsToJson(vectors.value)
    }

    // Імпорт: приймає JSON-рядок і додає вектори в базу
    fun importData(json: String) {
        viewModelScope.launch {
            try {
                repository.importVectorsFromJson(json)
                _infoMessage.value = "Дані успішно імпортовано!"
            } catch (e: Exception) {
                _infoMessage.value = "Помилка імпорту: невірний формат"
            }
        }
    }

    // --- Робота з файлами (Експорт/Імпорт через URI) ---

    // Експорт: запис даних у вибраний файл .txt
    fun exportToFile(uri: android.net.Uri, contentResolver: android.content.ContentResolver) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Отримуємо актуальні дані зі StateFlow vectors
                val json = exportData()

                // Відкриваємо потік і записуємо дані
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(json.toByteArray())
                    outputStream.flush() // Важливо: виштовхуємо дані з буфера у файл
                }
                _infoMessage.value = "Файл успішно збережено!"
            } catch (e: Exception) {
                e.printStackTrace()
                _infoMessage.value = "Помилка при збереженні файлу"
            }
        }
    }

    // Імпорт: читання даних із вибраного файлу .txt
    fun importFromFile(uri: android.net.Uri, contentResolver: android.content.ContentResolver) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val json = inputStream.bufferedReader().use { it.readText() }
                    if (json.isNotBlank()) {
                        // Повертаємося в Main потік через importData для оновлення БД
                        launch(Dispatchers.Main) {
                            importData(json)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _infoMessage.value = "Не вдалося прочитати файл"
            }
        }
    }
}