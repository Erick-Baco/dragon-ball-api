package mx.unam.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mx.unam.network.DragonBallApi

class HomeViewModel : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var _character by mutableStateOf<Character?>(null)
    val character: Character?
        get() = _character

    private var _errorMessage by mutableStateOf<String?>(null)
    val errorMessage: String?
        get() = _errorMessage

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading

    init {
        observeSearchText()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchText() {
        viewModelScope.launch {
            _searchText
                .debounce(350)
                .map { it.trim() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    searchCharacter(query, showBlankError = false)
                }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun getCharacter(name: String) {
        viewModelScope.launch {
            searchCharacter(name.trim(), showBlankError = true)
        }
    }

    private suspend fun searchCharacter(
        name: String,
        showBlankError: Boolean
    ) {
        if (name.isBlank()) {
            _character = null
            _errorMessage = if (showBlankError) "Ingresa un nombre" else null
            _isLoading = false
            return
        }

        try {
            _isLoading = true
            _errorMessage = null

            val characterResult = DragonBallApi.retrofitService.getCharacter(name)
            val found = characterResult.firstOrNull()

            if (found != null) {
                _character = found
                _errorMessage = null
            } else {
                _character = null
                _errorMessage = "No se encontró el personaje"
            }
        } catch (e: Exception) {
            _character = null
            _errorMessage = "Error al buscar"
        } finally {
            _isLoading = false
        }
    }
}