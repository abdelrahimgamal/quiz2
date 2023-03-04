package myapp.aishari.aishari_oblig2.ui.home


sealed interface AlpacaUiState {
    object Idle : AlpacaUiState
    object Loading : AlpacaUiState
    data class Successful<T>(val list: List<T>) : AlpacaUiState
    data class Error(val error: String) : AlpacaUiState
}