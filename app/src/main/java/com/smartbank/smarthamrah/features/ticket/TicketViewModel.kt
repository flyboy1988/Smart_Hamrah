package com.smartbank.smarthamrah.features.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.features.ticket.data.Ticket
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.smartbank.smarthamrah.features.ticket.data.TicketReply
import com.smartbank.smarthamrah.features.ticket.data.TicketStatus

class TicketsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<TicketsUiState>(TicketsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadMockTickets()
    }

    private fun loadMockTickets() {
        viewModelScope.launch {
            delay(1000) // Simulate network delay
            val mockTickets = listOf(
                Ticket(
                    id = 1,
                    title = "بررسی تراکنش",
                    date = "۱۴۰۴/۱۱/۱۱",
                    time = "۲۲:۱۸",
                    status = TicketStatus.PENDING,
                    description = "زمان زیادی از ثبت درخواستم میگذره، لطفا بررسی کنید.",
                    reply = TicketReply(
                        message = "کاربر گرامی، سلام.\n" +
                                "\n" +
                                "بابت طولانی شدن روند بررسی تراکنش شما معذرت می\u200Cخواهیم. درخواست شما به بخش فنی و مالی ارجاع داده شد تا به صورت فوری اولویت\u200Cدهی شود. به محض دریافت تاییدیه، جزئیات و وضعیت واریز را در همینجا با شما به اشتراک خواهیم گذاشت.",
                        date = "۱۴۰۴/۱۱/۱۳"
                    ),
                    hasAttachment = false,
                    isExpanded = false
                ),
                Ticket(
                    id = 2,
                    title = "درخواست وام",
                    date = "۱۴۰۴/۱۱/۱۱",
                    time = "22:18",
                    description = "برای دریافت وام، چه مدارکی لازم است و چند روز کاری طول میکشد تا نتیجه درخواست من اعلام شود؟",
                    status = TicketStatus.CLOSED,
                    hasAttachment = true,
                    isExpanded = false
                )
            )
            _uiState.value = TicketsUiState.Success(mockTickets)
        }
    }

    fun toggleExpand(ticketId: Int) {
        val currentState = _uiState.value
        if (currentState is TicketsUiState.Success) {
            val updatedList = currentState.tickets.map {
                if (it.id == ticketId) it.copy(isExpanded = !it.isExpanded) else it
            }
            _uiState.value = TicketsUiState.Success(updatedList)
        }
    }

    fun submitReply(ticketId: Int, replyText: String) {
        viewModelScope.launch {
            // Simulate API call
            delay(1000)

            val currentState = _uiState.value
            if (currentState is TicketsUiState.Success) {
                val updatedList = currentState.tickets.map { ticket ->
                    if (ticket.id == ticketId) {
                        // Create a new reply or update existing one
                        val newReply = TicketReply(
                            message = replyText,
                            date = getCurrentDate()
                        )
                        ticket.copy(reply = newReply)
                    } else {
                        ticket
                    }
                }
                _uiState.value = TicketsUiState.Success(updatedList)
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault())
        return dateFormat.format(java.util.Date())
    }
}

sealed class TicketsUiState {
    object Loading : TicketsUiState()
    data class Success(val tickets: List<Ticket>) : TicketsUiState()
}