package com.macaosoftware.ui.ui

import com.macaosoftware.ui.dailyagenda.decimalslots.DecimalEvent
import com.macaosoftware.ui.dailyagenda.timeslots.LocalTimeEvent

sealed interface BottomSheetEventsState {
    object Hidden : BottomSheetEventsState

    class ShowTimedEventRequested(val localTimeEvent: LocalTimeEvent) :
        BottomSheetEventsState

    object AddTimedEventRequested : BottomSheetEventsState

    class RemoveTimedEventRequested(val localTimeEvent: LocalTimeEvent) :
        BottomSheetEventsState

    class ShowDecimalEventRequested(val decimalEvent: DecimalEvent) : BottomSheetEventsState

    object AddDecimalEventRequested : BottomSheetEventsState

    class RemoveDecimalEventRequested(val decimalEvent: DecimalEvent) : BottomSheetEventsState

    class ShowEpgEventRequested(val epgEvent: LocalTimeEvent) : BottomSheetEventsState

    object AddEpgEventRequested : BottomSheetEventsState

    class RemoveEpgEventRequested(val epgEvent: LocalTimeEvent) : BottomSheetEventsState
}