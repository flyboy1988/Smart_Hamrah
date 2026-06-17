package com.smartbank.smarthamrah.features.ticket.data


enum class TicketStatus {
    PENDING, CLOSED
}

data class Ticket(
    val id: Int,
    val title: String,
    val date: String,
    val time: String,
    val status: TicketStatus,
    val description: String? = null,
    val reply: TicketReply? = null,
    var isExpanded: Boolean = false,
    val hasAttachment: Boolean
)

data class TicketReply(
    val message: String,
    val date: String
)