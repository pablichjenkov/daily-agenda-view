package com.macaosoftware.ui.dailyagenda

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class CurrentTimeMarkerState(val offsetY: Dp)

internal class CurrentTimeMarkerStateController(
    val dailyAgendaState: DailyAgendaState
) {

    val config = dailyAgendaState.config
    val state = mutableStateOf(value = CurrentTimeMarkerState(offsetY = 0.dp))

    init {
        val localTime = getCurrentLocalTime()
        val currentTimeAsDecimal = fromLocalTimeToValue(localTime)
        val offsetY =
            (currentTimeAsDecimal - config.initialSlotValue) * (config.slotScale * config.slotHeight)


        state.value = CurrentTimeMarkerState(offsetY = offsetY.dp)
    }

    @OptIn(ExperimentalTime::class)
    fun getCurrentLocalTime(): LocalTime {
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = now.toLocalDateTime(timeZone)
        return localDateTime.time
    }

}
