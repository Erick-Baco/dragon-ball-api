package mx.unam.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.unam.network.DragonBallApi

class HomeViewModel: ViewModel() {

    private var _character by mutableStateOf<Character?>(null)
    val character: Character?
        get () = _character
    private var _errorMessage by mutableStateOf<String?>(null)
    val errorMessage: String?
        get() = _errorMessage

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get () = _isLoading

    fun getCharacter(name: String) {
        if (name.isBlank()) {
            _errorMessage = "Ingresa un nombre"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading = true

                val characterResult = DragonBallApi.retrofitService.getCharacter(name)
                val found = characterResult.firstOrNull()

                if (found !== null) {
                    _character = found
                    _errorMessage = null
                } else {
                    _errorMessage = "No se encontró el personaje"
                }
            } catch (e: Exception) {
                _errorMessage = "Error al buscar"
            } finally {
                _isLoading = false
            }
        }
    }
}