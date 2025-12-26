package com.macaosoftware.ui.dailyagenda.decimalslots

open class DecimalSlotsStateController(
    val decimalSlotConfig: DecimalSlotConfig,
    eventsArrangement: EventsArrangement = EventsArrangement.MixedDirections()
) {

    val slotScale = decimalSlotConfig.slotScale
    val slotHeight = decimalSlotConfig.slotHeight
    val slotUnit = 1.0F / slotScale
    val firstSlotIndex = (slotScale * decimalSlotConfig.initialSlotValue.toInt())

    private val lastSlotIndex = decimalSlotConfig.lastSlotValue.toInt() * slotScale


    internal val decimalSlotsBaseLayoutStateController = DecimalSlotsBaseLayoutStateController(
        decimalSlotConfig = decimalSlotConfig,
        slots = createSlots(firstSlotIndex, lastSlotIndex),
        eventsArrangement = eventsArrangement
    )

    val decimalSlotsDataUpdater = DecimalSlotsDataUpdater(decimalSlotsBaseLayoutStateController)

    fun createSlots(
        firstSlotIndex: Int,
        lastSlotIndex: Int
    ): List<Slot> {
        val slots = mutableListOf<Slot>()
        for (i in firstSlotIndex..lastSlotIndex) {
            val slotStartValue = i * slotUnit
            slots.add(
                Slot(
                    title = "$slotStartValue",
                    startValue = slotStartValue,
                    endValue = slotStartValue + slotUnit
                )
            )
        }
        return slots
    }

    /**
     * Returns the DecimalSlotsStateController data in the current state.
     * This data can be later on serialized and be sent remote to be presented
     * in another device or another platform.
     * */
    fun getTimeSlotsData(): Map<Slot, List<DecimalEvent>> {
        return decimalSlotsBaseLayoutStateController.slotToDecimalEventMapSorted
    }

}
