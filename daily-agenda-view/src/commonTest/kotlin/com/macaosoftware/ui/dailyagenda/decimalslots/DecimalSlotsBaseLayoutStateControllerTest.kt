package com.macaosoftware.ui.dailyagenda.decimalslots

import com.macaosoftware.ui.dailyagenda.decimalslots.data.DecimalSlotsDataSample
import kotlin.test.Test
import kotlin.test.assertEquals

class DecimalSlotsBaseLayoutStateControllerTest {

    @Test
    fun `maxColumn computation - 1`() {

        val decimalSlotsStateController = DecimalSlotsStateController(
                decimalSlotConfig = DecimalSlotConfig(
                    initialSlotValue = 7.0F,
                    lastSlotValue = 19.0F,
                    slotScale = 2,
                    slotHeight = 48
                ),
                eventsArrangement = EventsArrangement.MixedDirections(EventWidthType.FixedSizeFillLastEvent)
            ).apply {
                DecimalSlotsDataSample(decimalSlotsStateController = this)
            }

        decimalSlotsStateController.decimalSlotsBaseLayoutStateController.updateState()
        val state = decimalSlotsStateController.decimalSlotsBaseLayoutStateController.state
        println(state.value?.maxColumns)
        assertEquals(expected = 11, actual = state.value?.maxColumns)
    }
}