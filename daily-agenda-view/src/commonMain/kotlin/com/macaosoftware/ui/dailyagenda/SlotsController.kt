package com.macaosoftware.ui.dailyagenda

import kotlin.math.abs

private const val HOURS_IN_ONE_DAY = 24

abstract class SlotsController(slotConfig: SlotConfig) {
    val amountOfHours = HOURS_IN_ONE_DAY
    val slotScale = slotConfig.slotScale
    val slotHeight = slotConfig.slotHeight
    val amountOfSlots = amountOfHours * slotScale
    val slotUnit = 1.0F / slotScale
    val firstSlotIndex = getSlotIndexForValue(initialSlotValue = slotConfig.initialSlotValue)
    val slots = createSlots(firstSlotIndex)
    val firstSlot = slots[0]

    abstract fun createSlots(firstSlotIndex: Int): List<Slot>

    fun getSlotForValue(startValue: Float): Slot {
        return slots.find { abs(x = startValue - it.value) < slotUnit }
            ?: error("startTime: $startValue must be between 0.0 and 24.0")
    }

    private fun getSlotIndexForValue(initialSlotValue: Float) : Int {
        return slotScale * initialSlotValue.toInt()
    }
}

class DecimalSlotsController(slotConfig: SlotConfig) : SlotsController(slotConfig) {

    override fun createSlots(firstSlotIndex: Int): List<Slot> {
        val slots = mutableListOf<Slot>()
        for (i in firstSlotIndex until amountOfSlots) {
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

    override fun createSlots(firstSlotIndex: Int): List<Slot> {
        val slots = mutableListOf<Slot>()
        for (i in firstSlotIndex until amountOfSlots) {
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
