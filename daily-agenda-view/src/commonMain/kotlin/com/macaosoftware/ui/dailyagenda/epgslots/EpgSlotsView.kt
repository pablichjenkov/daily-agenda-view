package com.macaosoftware.ui.dailyagenda.epgslots

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.macaosoftware.ui.dailyagenda.decimalslots.toDp
import com.macaosoftware.ui.dailyagenda.marker.CurrentTimeMarkerStateController
import com.macaosoftware.ui.dailyagenda.marker.CurrentTimeMarkerView
import com.macaosoftware.ui.dailyagenda.slotslayer.SlotsLayer
import com.macaosoftware.ui.dailyagenda.slotslayer.getSlotsLayerState
import com.macaosoftware.ui.dailyagenda.timeslots.LocalTimeEvent

@Composable
fun EpgSlotsView(
    epgSlotsStateController: EpgSlotsStateController,
    eventContentProvider: @Composable (localTimeEvent: LocalTimeEvent) -> Unit
) {
    val epgSlotsState = epgSlotsStateController.state.value ?: return
    val scrollState = rememberScrollState()
    val currentTimeMarkerStateController = remember {
        CurrentTimeMarkerStateController(decimalSlotConfig = epgSlotsState.epgChannelSlotConfig.toSlotConfig())
    }
    val scrollOffset by remember { derivedStateOf { scrollState.value } }
    Box(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
    ) {
        SlotsLayer(
            modifier = Modifier.padding(top = epgSlotsState.epgChannelSlotConfig.topHeaderHeight.dp),
            slotsLayerState = epgSlotsState.getSlotsLayerState()
        )
        EpgSlotsLayout(
            epgSlotsState = epgSlotsState,
            eventContentProvider = eventContentProvider,
            scrollOffset = scrollOffset.toDp()
        )
        CurrentTimeMarkerView(
            modifier = Modifier.padding(top = epgSlotsState.epgChannelSlotConfig.topHeaderHeight.dp),
            currentTimeMarkerStateController = currentTimeMarkerStateController
        )
    }
}
