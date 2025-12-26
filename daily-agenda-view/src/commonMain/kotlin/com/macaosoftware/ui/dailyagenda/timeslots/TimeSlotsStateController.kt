package com.macaosoftware.ui.dailyagenda.timeslots

import com.macaosoftware.ui.dailyagenda.decimalslots.DecimalSlotsBaseLayoutStateController
import com.macaosoftware.ui.dailyagenda.decimalslots.EventsArrangement
import com.macaosoftware.ui.dailyagenda.decimalslots.Slot

class TimeSlotsStateController(
    val timeSlotConfig: TimeSlotConfig = TimeSlotConfig(),
    eventsArrangement: EventsArrangement = EventsArrangement.MixedDirections()
) {

    val slotConfig = timeSlotConfig.toSlotConfig()
    val slotScale = slotConfig.slotScale
    val slotHeight = slotConfig.slotHeight
    val slotUnit = 1.0F / slotScale
    val firstSlotIndex = (slotScale * slotConfig.initialSlotValue.toInt())
    private val lastSlotIndex = (slotConfig.lastSlotValue * slotScale).toInt()

    internal val decimalSlotsBaseLayoutStateController = DecimalSlotsBaseLayoutStateController(
        decimalSlotConfig = slotConfig,
        slots = createSlots(firstSlotIndex, lastSlotIndex),
        eventsArrangement = eventsArrangement
    )

    val timeSlotsDataUpdater = TimeSlotsDataUpdater(
        decimalSlotsBaseLayoutStateController = decimalSlotsBaseLayoutStateController
    )

    fun createSlots(
        firstSlotIndex: Int,
        lastSlotIndex: Int
    ): List<Slot> {
        val slots = mutableListOf<Slot>()
        for (i in firstSlotIndex..lastSlotIndex) {
            val slotStartValue = i * slotUnit
            val title = fromDecimalValueToTimeText(slotStartValue, timeSlotConfig.useAmPm)

            slots.add(
                Slot(
                    title = title,
                    startValue = slotStartValue,
                    endValue = slotStartValue + slotUnit
                )
            )
        }
        return slots
    }

    /**
     * Returns the TimeSlotsStateController data in the current state.
     * This data can be later on serialized and be sent remote to be presented
     * in another device or another platform.
     * */
    fun getTimeSlotsData(): Map<LocalTimeSlot, List<LocalTimeEvent>> {
        val result = mutableMapOf<LocalTimeSlot, List<LocalTimeEvent>>()
        decimalSlotsBaseLayoutStateController.slotToDecimalEventMapSorted.forEach { entry ->
            result[entry.key.toLocalTimeSlot()] = entry.value.map { it.toLocalTimeEvent() }
        }
        return result
    }

}
