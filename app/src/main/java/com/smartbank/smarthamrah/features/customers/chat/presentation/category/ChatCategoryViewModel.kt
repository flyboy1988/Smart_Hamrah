package com.smartbank.smarthamrah.features.customers.chat.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatCategory
import com.smartbank.smarthamrah.features.customers.chat.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatCategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatCategoryUiState())
    val uiState: StateFlow<ChatCategoryUiState> = _uiState.asStateFlow()

    fun loadCategories() {
        loadCategoriesInternal(forceRefresh = false)
    }

    fun refreshCategories() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(isRefreshing = true)
            }

            launch {
                runCatching {
                    getCategoriesUseCase()
                }.onSuccess { categories ->

                    _uiState.update {
                        it.copy(
                            categories = categories
                        )
                    }

                }.onFailure { throwable ->

                    _uiState.update {
                        it.copy(
                            errorMessage = throwable.message
                        )
                    }
                }
            }

            delay(1000)

            _uiState.update {
                it.copy(isRefreshing = false)
            }
        }
    }
    fun selectCategory(category: ChatCategory) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    private fun loadCategoriesInternal(forceRefresh: Boolean) {
        if (!forceRefresh && _uiState.value.categories.isNotEmpty()) return
        if (_uiState.value.isLoading || _uiState.value.isRefreshing) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = !forceRefresh,
                    isRefreshing = forceRefresh,
                    errorMessage = null
                )
            }

            runCatching {
                getCategoriesUseCase()
            }.onSuccess { categories ->
                _uiState.update { currentState ->
                    val selectedStillExists =
                        currentState.selectedCategory?.takeIf { selected ->
                            categories.any { category -> category.id == selected.id }
                        }

                    currentState.copy(
                        categories = categories,
                        selectedCategory = selectedStillExists,
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = throwable.message ?: "خطا در دریافت دسته‌بندی‌ها"
                    )
                }
            }
        }
    }
}