package com.smartbank.smarthamrah.features.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.features.reports.data.repository.ReportsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class ReportsViewModel : ViewModel() {

    // 🌟 مقداردهی سخت‌کد شده دقیقاً مطابق با انواع داده (Types) در مدل و Screenshot 2026-05-31 223514.png
    private val _uiState = MutableStateFlow(
        ReportsUiState(
            isLoading = false, // غیرفعال کردن لودینگ برای نمایش مستقیم لیست
            isRefreshing = false,
            error = null,

            // رضایت مشتری
            satisfactionRate = 4.3f,
            satisfactionCount = 896,

            // مشتریان
            assignedCustomers = 60,
            activeCustomers = 56,

            // تعاملات
            totalMessages = 1023,
            answeredRequests = 324,

            // سرعت پاسخگویی و حل مشکل
            averageResponseTime = "۳ دقیقه و ۲۰ ثانیه",
            resolutionTime = "۳ دقیقه و ۲۰ ثانیه",
            slaStatus = 93, // مقدار عددی درصد شاخص وضعیت SLA (بر اساس نشان ۹۳٪ در تصویر)
            proactiveInteractions = 5
        )
    )
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        // متد لود کردن داده‌ها از ریپازیتوری را خالی می‌گذاریم تا مقادیر سخت‌کد شده بازنویسی نشوند.
    }

    fun refresh() {
        // شبیه‌سازی انیمیشن Pull-to-Refresh برای حفظ پویایی لایه کاربری
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            kotlinx.coroutines.delay(500)
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }
}