package com.macaosoftware.ui.dailyagenda

import kotlinx.datetime.LocalTime

private const val HOUR_AM = "AM"
private const val HOUR_PM = "PM"
private const val MINUTES_IN_ONE_HOUR = 60

data class HourAndMinute(val hour: Int, val minute: Int)

data class HourAndMinuteEvent(
    val startSlot: Slot,
    val title: String,
    val startTime: HourAndMinute,
    val endTime: HourAndMinute
)

data class LocalTimeEvent(
    val startSlot: Slot,
    val title: String,
    val startTime: LocalTime,
    val endTime: LocalTime
)

data class HourAndMinuteSlot(
    val title: String,
    val hourAndMinute: HourAndMinute
)

data class LocalTimeSlot(
    val title: String,
    val localTime: LocalTime
)

fun Event.toHourAndMinuteEvent(): HourAndMinuteEvent {
    val startTimeHourAndMinute = fromValueToHourAndMinute(value = startValue)
    val endTimeHourAndMinute = fromValueToHourAndMinute(value = endValue)
    return HourAndMinuteEvent(
        startSlot = startSlot,
        title = title,
        startTime = startTimeHourAndMinute,
        endTime = endTimeHourAndMinute
    )
}

fun Event.toLocalTimeEvent(): LocalTimeEvent {
    val startLocalTime = fromValueToLocalTime(value = startValue)
    val endLocalTime = fromValueToLocalTime(value = endValue)
    return LocalTimeEvent(
        startSlot = startSlot,
        title = title,
        startTime = startLocalTime,
        endTime = endLocalTime
    )
}

fun HourAndMinuteEvent.toEvent(): Event {
    val startTimeValue = fromHourAndMinuteToValue(
        hour = startTime.hour,
        minute = startTime.minute
    )
    val endTimeValue = fromHourAndMinuteToValue(
        hour = endTime.hour,
        minute = endTime.minute
    )
    return Event(
        startSlot = startSlot,
        title = title,
        startValue = startTimeValue,
        endValue = endTimeValue
    )
}

fun LocalTimeEvent.toEvent(): Event {
    val startTimeValue = fromHourAndMinuteToValue(
        hour = startTime.hour,
        minute = startTime.minute
    )
    val endTimeValue = fromHourAndMinuteToValue(
        hour = endTime.hour,
        minute = endTime.minute
    )
    return Event(
        startSlot = startSlot,
        title = title,
        startValue = startTimeValue,
        endValue = endTimeValue
    )
}

fun Slot.toHourAndMinuteSlot(): HourAndMinuteSlot {
    return HourAndMinuteSlot(
        title = title,
        hourAndMinute = fromValueToHourAndMinute(value = value)
    )
}

fun HourAndMinuteSlot.toSlot(): Slot {
    return Slot(
        title = title,
        value = fromHourAndMinuteToValue(
            hour = hourAndMinute.hour,
            minute = hourAndMinute.minute
        )
    )
}

fun Slot.toLocalTimeSlot(): LocalTimeSlot {
    return LocalTimeSlot(
        title = title,
        localTime = fromValueToLocalTime(value = value)
    )
}

fun LocalTimeSlot.toSlot(): Slot {
    return Slot(
        title = title,
        value = fromHourAndMinuteToValue(
            hour = localTime.hour,
            minute = localTime.minute
        )
    )
}

fun fromHourAndMinuteToValue(hour: Int, minute: Int): Float {
    val minuteFraction = minute.toFloat() / MINUTES_IN_ONE_HOUR
    return hour.toFloat() + minuteFraction
}

fun fromValueToHourAndMinute(value: Float): HourAndMinute {
    val remaining = value % 1
    val minutes = (remaining * MINUTES_IN_ONE_HOUR).toInt()
    val hours = value.toInt()
    return HourAndMinute(hour = hours, minute = minutes)
}

fun fromValueToLocalTime(value: Float): LocalTime {
    val remaining = value % 1
    val minutes = (remaining * MINUTES_IN_ONE_HOUR).toInt()
    val hours = value.toInt()
    return LocalTime(hour = hours, minute = minutes)
}

fun fromDecimalValueToTimeText(
    slotStartValue: Float,
    useAmPm: Boolean = true
): String {
    val remaining = slotStartValue % 1
    val minutes = (remaining * MINUTES_IN_ONE_HOUR).toInt()
    val minutesTwoDigitFormat = if (minutes > 9) {
        minutes.toString()
    } else {
        "0$minutes"
    }

    if (!useAmPm) {
        val units = slotStartValue.toInt()
        return "$units:$minutesTwoDigitFormat"
    }

    val slotStartValueInt = slotStartValue.toInt()
    val hourUnits = slotStartValueInt % 12
    val hourUnitsFormatted = hourUnits.takeIf { it != 0 } ?: slotStartValueInt
    val amPmSuffix = if (slotStartValue < 12F) HOUR_AM else HOUR_PM
    return "$hourUnitsFormatted:$minutesTwoDigitFormat:$amPmSuffix"
}
