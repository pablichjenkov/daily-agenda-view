package com.macaosoftware.ui.dailyagenda

import kotlin.math.abs

class SlotsGenerator {
    val slots = mutableListOf<Slot>()
    val amountOfHours = 24
    val slotScale = 2
    val amountOfSlots = amountOfHours * slotScale
    val slotUnit = 1.0F / slotScale

    init {
        for (i in 0 until amountOfSlots) {

            val slotStartValue = i * slotUnit

            val titleSuffix = if (slotStartValue < 12F) "AM" else "PM"

            slots.add(
                Slot(
                    title = "$slotStartValue $titleSuffix",
                    time = slotStartValue
                )
            )
        }
    }

    fun getSlotForTime(startTime: Float): Slot {
        return slots.find { abs(x = startTime - it.time) < slotUnit }
            ?: error("startTime must be between 0.0 and 24.0")
    }

}