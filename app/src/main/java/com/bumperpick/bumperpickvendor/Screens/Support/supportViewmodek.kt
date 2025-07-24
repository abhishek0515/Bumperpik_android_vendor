package com.bumperpick.bumperickUser.Screens.Support
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.bumperpick.bumperickUser.API.New_model.tickerdetails
import com.bumperpick.bumperickUser.API.New_model.ticket_add_model
import com.bumperpick.bumperickUser.API.New_model.ticketmessage
import com.bumperpick.bumperickUser.Repository.SupportRepository

import com.bumperpick.bumperpickvendor.API.Model.success_model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.bumperpick.bumperpickvendor.Repository.Result
import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState

class SupportViewModel(private val repository: SupportRepository) : ViewModel() {
    fun resetTicketAddState() {
        _ticketAddState.value = UiState.Empty
    }

    private val _ticketAddState = MutableStateFlow<UiState<ticket_add_model>>(UiState.Empty)
    val ticketAddState: StateFlow<UiState<ticket_add_model>> = _ticketAddState

    private val _ticketsState = MutableStateFlow<UiState<ticketmessage>>(UiState.Empty)
    val ticketsState: StateFlow<UiState<ticketmessage>> = _ticketsState

    private val _ticketDetailState = MutableStateFlow<UiState<tickerdetails>>(UiState.Empty)
    val ticketDetailState: StateFlow<UiState<tickerdetails>> = _ticketDetailState

    private val _ticketReplyState = MutableStateFlow<UiState<success_model>>(UiState.Empty)
    val ticketReplyState: StateFlow<UiState<success_model>> = _ticketReplyState

    fun addTicket(subject: String, message: String) {
        _ticketAddState.value = UiState.Loading
        viewModelScope.launch {
            if (message.length < 10) {
                _ticketAddState.value =
                    UiState.Error("The message field must be at least 10 characters.")
            } else {
                when (val result = repository.ticketadd(subject, message)) {
                    is Result.Success -> {
                        val resp = result.data
                        if (resp.code in 200..300) {
                            _ticketAddState.value = UiState.Success(result.data)
                        }
                        else{
                            _ticketAddState.value = UiState.Error(resp.message)
                        }
                    }
                    is Result.Error -> _ticketAddState.value = UiState.Error(result.message)
                    is Result.Loading -> _ticketAddState.value = UiState.Loading
                }
            }

        }
    }

    fun getTickets() {
        _ticketsState.value = UiState.Loading
        viewModelScope.launch {
            when (val result = repository.tickets()) {
                is Result.Success -> _ticketsState.value = UiState.Success(result.data)
                is Result.Error -> _ticketsState.value = UiState.Error(result.message)
                is Result.Loading -> _ticketsState.value = UiState.Loading
            }
        }
    }

    fun getTicketDetails(id: String) {
        _ticketDetailState.value = UiState.Loading
        viewModelScope.launch {
            when (val result = repository.tickerDetails(id)) {
                is Result.Success -> _ticketDetailState.value = UiState.Success(result.data)
                is Result.Error -> _ticketDetailState.value = UiState.Error(result.message)
                is Result.Loading -> _ticketDetailState.value = UiState.Loading
            }
        }
    }

    fun replyToTicket(id: String, message: String) {
        _ticketReplyState.value = UiState.Loading
        viewModelScope.launch {
            when (val result = repository.ticketReply(id, message)) {
                is Result.Success -> _ticketReplyState.value = UiState.Success(result.data)
                is Result.Error -> _ticketReplyState.value = UiState.Error(result.message)
                is Result.Loading -> _ticketReplyState.value = UiState.Loading
            }
        }
    }

}
