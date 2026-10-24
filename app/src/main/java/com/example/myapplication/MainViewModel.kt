import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// 1. Модель состояния экрана (список приложений)
data class MainUiState(
    val items: List<String> = emptyList()
)

// 2. Разовое событие для Snackbar
sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}

class MainViewModel : ViewModel() {

    // Сохранение состояния списка при повороте экрана
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // Канал для одноразового показа Snackbar
    private val _eventChannel = Channel<UiEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _uiState.value = MainUiState(
            items = listOf(
                "Приложение 1: Telegram",
                "Приложение 2: WhatsApp",
                "Приложение 3: YouTube",
                "Приложение 4: VK",
                "Приложение 5: Yandex"
            )
        )
    }

    // Вызывается при клике на Toolbar
    fun onToolbarClick() {
        viewModelScope.launch {
            _eventChannel.send(UiEvent.ShowSnackbar("Клик по Toolbar! Задание 5 выполнено."))
        }
    }
}