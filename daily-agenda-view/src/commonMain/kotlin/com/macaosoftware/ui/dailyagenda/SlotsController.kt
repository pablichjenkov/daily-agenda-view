package com.macaosoftware.ui.dailyagenda

import kotlin.math.abs

private const val HOURS_IN_ONE_DAY = 24

abstract class SlotsController(slotConfig: SlotConfig) {
    val slotScale = slotConfig.slotScale
    val slotHeight = slotConfig.slotHeight
    val slotUnit = 1.0F / slotScale
    val firstSlotIndex = (slotScale * slotConfig.initialSlotValue.toInt())


    private val amountOfSlotsInOneDay = (HOURS_IN_ONE_DAY) * slotScale
    val slots = createSlots(firstSlotIndex, amountOfSlotsInOneDay)

    val firstSlot = slots[0]

    abstract fun createSlots(firstSlotIndex: Int, amountOfSlotsInOneDay: Int): List<Slot>

    fun getSlotForValue(startValue: Float): Slot {
        return slots.find { abs(x = startValue - it.value) < slotUnit }
            ?: error("startTime: $startValue must be between 0.0 and 24.0")
    }
}

class DecimalSlotsController(slotConfig: SlotConfig) : SlotsController(slotConfig) {

    override fun createSlots(firstSlotIndex: Int, amountOfSlotsInOneDay: Int): List<Slot> {
        val slots = mutableListOf<Slot>()
        for (i in firstSlotIndex until amountOfSlotsInOneDay) {
            val slotStartValue = i * slotUnit
            slots.add(
                Slot(
                    title = "$slotStartValue",
                    value = slotStartValue
                )
            )
        }
        return slots
    }
}

class TimeLineSlotsController(slotConfig: SlotConfig) : SlotsController(slotConfig) {

    override fun createSlots(firstSlotIndex: Int, amountOfSlotsInOneDay: Int): List<Slot> {
        val slots = mutableListOf<Slot>()
        for (i in firstSlotIndex until amountOfSlotsInOneDay) {
            val slotStartValue = i * slotUnit
            val title = fromDecimalValueToTimeText(slotStartValue)

            slots.add(
                Slot(
                    title = title,
                    value = slotStartValue
                )
            )
        }
        return slots
    }
}
