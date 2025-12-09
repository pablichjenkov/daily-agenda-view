package com.macaosoftware.ui.dailyagenda.epgslots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.macaosoftware.ui.dailyagenda.timeslots.LocalTimeEvent
import com.macaosoftware.ui.dailyagenda.timeslots.fromLocalTimeToValue
import com.macaosoftware.ui.dailyagenda.timeslots.toSlotConfig

@Composable
internal fun EpgSlotsLayout(
    epgSlotsState: EpgSlotsState,
    scrollOffset: Dp,
    eventContentProvider: @Composable (localTimeEvent: LocalTimeEvent) -> Unit
) {
    val timelineLeftPadding = epgSlotsState.epgChannelSlotConfig.timeSlotConfig.timelineLeftPadding
    val channelWidth = epgSlotsState.epgChannelSlotConfig.channelWidth
    val topHeaderHeight = epgSlotsState.epgChannelSlotConfig.topHeaderHeight
    val totalHeight = epgSlotsState.epgChannelHeight
    LazyRow(
        modifier = Modifier.height(height = totalHeight.dp)
            .padding(start = timelineLeftPadding.dp)
    ) {
        items(
            items = epgSlotsState.epgChannels,
            key = { epgChannel ->
                epgChannel.name
            }
        ) { epgChannel ->
            Column(
                modifier = Modifier.width(width = channelWidth.dp)
                    .padding(all = 1.dp).background(color = Color.Gray.copy(alpha = 0.5f))
            ) {
                Box(
                    Modifier.height(height = topHeaderHeight.dp)
                        .width(width = epgSlotsState.epgChannelSlotConfig.channelWidth.dp)
                        .offset(y = scrollOffset)
                        .padding(all = 1.dp)
                        .background(color = Color.Gray)
                        .zIndex(zIndex = 1f)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = epgChannel.name
                    )
                }
                ChannelColumn(
                    epgChannel = epgChannel,
                    epgChannelSlotConfig = epgSlotsState.epgChannelSlotConfig,
                    eventContentProvider = eventContentProvider
                )
            }
        }
    }
}

@Composable
fun ChannelColumn(
    epgChannel: EpgChannel,
    epgChannelSlotConfig: EpgChannelSlotConfig,
    eventContentProvider: @Composable (localTimeEvent: LocalTimeEvent) -> Unit
) {
    val slotsConfig = epgChannelSlotConfig.timeSlotConfig.toSlotConfig()
    val initialSlotValue = slotsConfig.initialSlotValue
    Box(modifier = Modifier.fillMaxSize()) {
        val scaleMultiplier = slotsConfig.slotHeight * slotsConfig.slotScale
        epgChannel.events.forEach { localTimeEvent ->
            val startValue = fromLocalTimeToValue(localTimeEvent.startTime)
            val endValue = fromLocalTimeToValue(localTimeEvent.endTime)
            val offsetY = (startValue - initialSlotValue) * scaleMultiplier
            val programHeight = (endValue - startValue) * scaleMultiplier
            Box(
                modifier = Modifier.offset(y = offsetY.dp)
                    .fillMaxWidth()
                    .height(height = programHeight.dp)

            ) { eventContentProvider.invoke(localTimeEvent) }
        }
    }
}
