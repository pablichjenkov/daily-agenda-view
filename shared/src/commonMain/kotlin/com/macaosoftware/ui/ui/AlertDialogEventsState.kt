package com.macaosoftware.ui.ui

sealed interface AlertDialogEventsState {
    object Hidden : AlertDialogEventsState
    class ShowingInfo(val text: String) : AlertDialogEventsState
}
