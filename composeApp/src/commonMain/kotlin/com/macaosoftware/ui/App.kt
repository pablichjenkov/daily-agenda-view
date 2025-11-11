package com.macaosoftware.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.macaosoftware.ui.dailyagenda.DailyAgendaStateController
import com.macaosoftware.ui.dailyagenda.DailyAgendaView
import com.macaosoftware.ui.dailyagenda.DecimalSlotsController
import com.macaosoftware.ui.dailyagenda.EventsArrangement
import com.macaosoftware.ui.dailyagenda.SlotConfig
import com.macaosoftware.ui.dailyagenda.TimeLineSlotsController
import com.macaosoftware.ui.dailyagenda.toLocalTimeEvent
import com.macaosoftware.ui.data.Sample0
import com.macaosoftware.ui.data.Sample3
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding)
            ) {
                val stateController = remember {
                    val slotConfig = SlotConfig(slotScale = 1, slotHeight = 100)
                    val slotsController = TimeLineSlotsController(slotConfig = slotConfig)

                    DailyAgendaStateController(
                        slotsController = slotsController,
                        slotToEventMap = Sample0(slotsController = slotsController).slotToEventMap,
                        eventsArrangement = EventsArrangement.LeftToRight()
                    )
                }
                DailyAgendaView(
                    dailyAgendaState = stateController.state.value
                ) { event ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(all = 2.dp)
                            .background(color = generateRandomColor())
                    ) {
                        val localTimeEvent = event.toLocalTimeEvent()
                        Text(
                            text = "${event.title}: ${localTimeEvent.startTime}-${localTimeEvent.endTime}",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

private fun generateRandomColor(): Color {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color(red, green, blue)
}

@Preview(
    showBackground = true
)
@Composable
fun CalendarViewPreview() {
    val stateController = remember {
        val demoSlotConfiguration = SlotConfig()
        val slotsGenerator = DecimalSlotsController(demoSlotConfiguration)

        DailyAgendaStateController(
            slotsController = slotsGenerator,
            slotToEventMap = Sample0(slotsController = slotsGenerator).slotToEventMap,
            eventsArrangement = EventsArrangement.MixedDirections()
        )
    }
    MaterialTheme {
        Box(modifier = Modifier.size(600.dp, 1000.dp)) {
            DailyAgendaView(
                dailyAgendaState = stateController.state.value
            ) { event ->
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(color = generateRandomColor())
                ) {
                    val localTimeEvent = event.toLocalTimeEvent()
                    Text(
                        text = "${event.title}: ${localTimeEvent.startTime}-${localTimeEvent.endTime}",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
