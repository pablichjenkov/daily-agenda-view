package com.macaosoftware.ui.dailyagenda.decimalslots

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class LayoutUtilTest {

    @Test
    fun `Compute the offset of an event with respect to its slot baseline - 1`() {
        val decimalEvent = DecimalEvent(
            uuid = Uuid.random(),
            title = "Event Test",
            description = "Event Description",
            startValue = 8.5F,
            endValue = 9.5F,
        )
        val eventSlot = Slot(
            title = "Slot Test",
            startValue = 8.0F,
            endValue = 9.0F,
        )
        val config = Config(
            initialSlotValue = 0.0F,
            lastSlotValue = 23.59F,
            slotScale = 1,
            slotHeight = 100,
            timelineLeftPadding = 72
        )
        val offset = getEventTranslationInSlot(decimalEvent, eventSlot, config)
        assertEquals(expected = 50.dp, actual = offset)
    }

    @Test
    fun `Compute the offset of an event with respect to its slot baseline - 2`() {
        val decimalEvent = DecimalEvent(
            uuid = Uuid.random(),
            title = "Event Test",
            description = "Event Description",
            startValue = 0.10F,
            endValue = 1.0F,
        )
        val eventSlot = Slot(
            title = "Slot Test",
            startValue = 0.0F,
            endValue = 0.25F,
        )
        val config = Config(
            initialSlotValue = 0.0F,
            lastSlotValue = 23.59F,
            slotScale = 4,
            slotHeight = 100,
            timelineLeftPadding = 72
        )
        val offset = getEventTranslationInSlot(decimalEvent, eventSlot, config)
        assertEquals(expected = 40.dp, actual = offset)
    }

    @Test
    fun `Compute the offset of an event with respect to its slot baseline - 3`() {
        val decimalEvent = DecimalEvent(
            uuid = Uuid.random(),
            title = "Event Test",
            description = "Event Description",
            startValue = 8.0F,
            endValue = 9.0F,
        )
        val eventSlot = Slot(
            title = "Slot Test",
            startValue = 7.0F,
            endValue = 8.0F,
        )
        val config = Config(
            initialSlotValue = 0.0F,
            lastSlotValue = 23.59F,
            slotScale = 1,
            slotHeight = 100,
            timelineLeftPadding = 72
        )
        val offset = getEventTranslationInSlot(decimalEvent, eventSlot, config)
        assertEquals(expected = 100.dp, actual = offset)
    }
}