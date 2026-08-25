package com.example.mythos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.MythologyRepository
import com.example.mythos.model.Deity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MythologyUiState(
    val loading: Boolean = true,
    val deities: List<Deity> = emptyList(),
    val favorites: Set<String> = emptySet(),
    val error: String? = null
)

class MythologyViewModel(
    private val repository: MythologyRepository = MythologyRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MythologyUiState())
    val uiState: StateFlow<MythologyUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            try {
                repository.seedIfEmpty()
                val deities = repository.getDeities()
                _uiState.update { it.copy(loading = false, deities = deities) }
            } catch (e: Exception) {
                // Se o Firestore estiver indisponível, o acervo local mantém o app utilizável.
                _uiState.update {
                    it.copy(
                        loading = false,
                        deities = MythologyRepository.LOCAL_DEITIES,
                        error = e.message
                    )
                }
            }
        }
    }

    fun deityById(id: String?): Deity? =
        _uiState.value.deities.firstOrNull { it.id == id }

    fun toggleFavorite(id: String) {
        _uiState.update { state ->
            val favorites = state.favorites.toMutableSet()
            if (!favorites.add(id)) favorites.remove(id)
            state.copy(favorites = favorites)
        }
    }

    fun isFavorite(id: String): Boolean = _uiState.value.favorites.contains(id)

    private val _curiosity = MutableStateFlow("")
    val curiosity: StateFlow<String> = _curiosity.asStateFlow()

    fun loadCuriosity(firstId: String, secondId: String) {
        viewModelScope.launch {
            _curiosity.value = try {
                repository.getCuriosity(firstId, secondId)
            } catch (e: Exception) {
                "Ambos foram representados na arte como figuras protetoras de seus povos."
            }
        }
    }
}
