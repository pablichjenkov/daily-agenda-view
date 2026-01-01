package com.macaosoftware.ui.dailyagenda.marker

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.macaosoftware.ui.dailyagenda.decimalslots.DecimalSlotConfig
import com.macaosoftware.ui.dailyagenda.timeslots.fromLocalTimeToValue
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class CurrentTimeMarkerState(val offsetY: Dp)

internal class CurrentTimeMarkerStateController(
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    decimalSlotConfig: DecimalSlotConfig
) {

    val state = mutableStateOf(value = CurrentTimeMarkerState(offsetY = 0.dp))

    init {
        val localTime = getCurrentLocalTime()
        val currentTimeAsDecimal = fromLocalTimeToValue(localTime)
        val offsetY =
            (currentTimeAsDecimal - decimalSlotConfig.initialSlotValue) * (decimalSlotConfig.slotScale * decimalSlotConfig.slotHeight)


        state.value = CurrentTimeMarkerState(offsetY = offsetY.dp)
    }

    @OptIn(ExperimentalTime::class)
    fun getCurrentLocalTime(): LocalTime {
        return clock.now().toLocalDateTime(timeZone).time
    }

}
