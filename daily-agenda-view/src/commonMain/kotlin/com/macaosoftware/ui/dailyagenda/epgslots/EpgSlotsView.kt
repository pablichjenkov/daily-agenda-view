package com.macaosoftware.ui.dailyagenda.epgslots

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    Box(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
    ) {
        SlotsLayer(
            modifier = Modifier.padding(top = epgSlotsState.epgChannelSlotConfig.topHeaderHeight.dp),
            slotsLayerState = epgSlotsState.getSlotsLayerState()
        )
        EpgSlotsLayout(
            epgSlotsState = epgSlotsState,
            eventContentProvider = eventContentProvider
        )
        CurrentTimeMarkerView(
            modifier = Modifier.padding(top = epgSlotsState.epgChannelSlotConfig.topHeaderHeight.dp),
            currentTimeMarkerStateController = currentTimeMarkerStateController
        )
    }
    Row(
        modifier = Modifier
            .padding(start = epgSlotsState.epgChannelSlotConfig.timeSlotConfig.timelineLeftPadding.dp)
            .wrapContentSize()
    ) {
        epgSlotsState.epgChannels.forEach { channel ->
            Box(
                Modifier.height(height = epgSlotsState.epgChannelSlotConfig.topHeaderHeight.dp)
                    .width(width = epgSlotsState.epgChannelSlotConfig.channelWidth.dp)
                    .padding(all = 1.dp)
                    .background(color = Color.Gray)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = channel.name
                )
            }
        }
    }
}
