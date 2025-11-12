package com.macaosoftware.ui.data

import com.macaosoftware.ui.dailyagenda.Event
import com.macaosoftware.ui.dailyagenda.EventsManager
import com.macaosoftware.ui.dailyagenda.Slot

class Sample0(eventsManager: EventsManager) {

    val slotsController = eventsManager.slotsController
    val slots: List<Slot> = eventsManager.slotsController.slots
    private val slotToEventMap = eventsManager.slotToEventMap

    init {
        for (i in 0..slots.lastIndex) {
            val slot = slots[i]
            slotToEventMap[slot] = emptyList()
        }

        val slot8_00 = slotsController.getSlotForValue(8.0F)
        slotToEventMap[slot8_00] = createEventsFor800AM(startSlot = slot8_00)

        val slot8_30 = slotsController.getSlotForValue(8.5F)
        slotToEventMap[slot8_30] = createEventsFor830AM(startSlot = slot8_30)

        val slot9_00 = slotsController.getSlotForValue(9.0F)
        slotToEventMap[slot9_00] = createEventsFor900AM(startSlot = slot9_00)

        val slot9_30 = slotsController.getSlotForValue(9.5F)
        slotToEventMap[slot9_30] = createEventsFor930AM(startSlot = slot9_30)
    }

    fun createEventsFor800AM(startSlot: Slot): List<Event> {
        return slotToEventMap[startSlot]!! + listOf(
            Event(
                title = "Ev 1",
                startValue = 8.25F,
                endValue = 11.0F
            ),
            Event(
                title = "Ev 2",
                startValue = 8.0F,
                endValue = 8.75F
            ),
            Event(
                title = "Ev 3",
                startValue = 8.10F,
                endValue = 8.40F
            ),
        )
    }

    fun createEventsFor830AM(startSlot: Slot): List<Event> {
        return slotToEventMap[startSlot]!! + listOf(
            Event(
                title = "Evt 4",
                startValue = 8.65F,
                endValue = 11.0F
            ),
            Event(
                title = "Evt 5",
                startValue = 8.85F,
                endValue = 9.5F
            ),
            Event(
                title = "Evt 6",
                startValue = 8.5F,
                endValue = 9.5F
            ),
            Event(
                title = "Evt 7",
                startValue = 8.5F,
                endValue = 9.0F
            )
        )
    }

    fun createEventsFor900AM(startSlot: Slot): List<Event> {
        return slotToEventMap[startSlot]!! + listOf(
            Event(
                title = "Evt 8",
                startValue = 9.2F,
                endValue = 10.0F
            ),
            Event(
                title = "Evt 9",
                startValue = 9.25F,
                endValue = 10.0F
            )
        )
    }

    fun createEventsFor930AM(startSlot: Slot): List<Event> {
        return slotToEventMap[startSlot]!! + listOf(
            Event(
                title = "Evt 10",
                startValue = 9.5F,
                endValue = 11.0F
            ),
            Event(
                title = "Evt 11",
                startValue = 9.5F,
                endValue = 10.5F
            ),
            Event(
                title = "Evt 12",
                startValue = 9.85F,
                endValue = 10.40F
            ),
            Event(
                title = "Evt 13",
                startValue = 9.6F,
                endValue = 10.0F
            ),
            Event(
                title = "Evt 14",
                startValue = 9.8F,
                endValue = 10.20F
            )
        )
    }

}
