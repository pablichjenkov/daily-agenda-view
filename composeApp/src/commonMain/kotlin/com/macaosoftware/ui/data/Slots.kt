package com.macaosoftware.ui.data

import com.macaosoftware.ui.dailyagenda.Config
import com.macaosoftware.ui.dailyagenda.EventWidthType

object Slots {

    val demoConfigLTR = Config.LeftToRight(
        lastEventFillRow = true,
        initialSlotValue = 0.0F,
        slotScale = 2F,
        slotHeight = 96,
        timelineLeftPadding = 72
    )

    val demoConfigRTL = Config.RightToLeft(
        lastEventFillRow = true,
        initialSlotValue = 0.0F,
        slotScale = 2F,
        slotHeight = 96,
        timelineLeftPadding = 72
    )

    val demoConfigMixedDirections = Config.MixedDirections(
        eventWidthType = EventWidthType.FixedSizeFillLastEvent,
        initialSlotValue = 0.0F,
        slotScale = 2F,
        slotHeight = 96,
        timelineLeftPadding = 72
    )
}
