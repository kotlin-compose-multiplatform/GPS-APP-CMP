import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TestViewModel(private val httpClient: HttpClient): ScreenModel {
    private val _state = MutableStateFlow("Loading...")
    val state = _state.asStateFlow()
    fun getData() {
        screenModelScope.launch {
            try {
                val result: List<Post> = httpClient.get("https://jsonplaceholder.typicode.com/posts").body()
                _state.value = result.size.toString()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun setData(value: String) {
        _state.value = value
    }
}