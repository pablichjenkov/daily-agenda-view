package com.macaosoftware.ui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.macaosoftware.ui.dailyagenda.decimalslots.DecimalSlotConfig
import com.macaosoftware.ui.dailyagenda.decimalslots.DecimalSlotsStateController
import com.macaosoftware.ui.dailyagenda.decimalslots.DecimalSlotsView
import com.macaosoftware.ui.dailyagenda.decimalslots.EventWidthType
import com.macaosoftware.ui.dailyagenda.decimalslots.EventsArrangement
import com.macaosoftware.ui.dailyagenda.epgslots.EpgChannelSlotConfig
import com.macaosoftware.ui.dailyagenda.epgslots.EpgSlotsStateController
import com.macaosoftware.ui.dailyagenda.epgslots.EpgSlotsView
import com.macaosoftware.ui.dailyagenda.timeslots.TimeSlotConfig
import com.macaosoftware.ui.dailyagenda.timeslots.TimeSlotsStateController
import com.macaosoftware.ui.dailyagenda.timeslots.TimeSlotsView
import com.macaosoftware.ui.data.DecimalSlotsDataSample
import com.macaosoftware.ui.data.EpgSlotsDataSample
import com.macaosoftware.ui.ui.model.AllDayEvent
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
@Preview
fun DayScheduleApp() {
    val viewModel = remember { DayScheduleAppViewModel() }
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding)
            ) {
                when (viewModel.slotsViewType) {
                    SlotsViewType.Decimal -> {
                        DecimalSlotExample(
                            decimalSlotsStateController = viewModel.decimalSlotsStateController,
                            uiActionListener = viewModel.uiActionListener
                        )
                    }

                    SlotsViewType.Timeline -> {
                        TimeSlotExample(
                            allDayEvents = viewModel.allDayEvents,
                            timeSlotsStateController = viewModel.timeSlotsStateController,
                            uiActionListener = viewModel.uiActionListener
                        )
                    }

                    SlotsViewType.Epg -> {
                        EpgSlotExample(
                            epgSlotsStateController = viewModel.epgSlotsStateController,
                            uiActionListener = viewModel.uiActionListener
                        )
                    }
                }
                DayScheduleAppActionsBottomView(
                    slotsViewType = viewModel.slotsViewType,
                    uiActionListener = viewModel.uiActionListener
                )
                DayScheduleAppBottomSheet(
                    bottomSheetEventsState = viewModel.bottomSheetEventsState.value,
                    uiActionListener = viewModel.uiActionListener,
                    alertDialogUiActionListener = viewModel.alertDialogUiActionListener
                )
                DayScheduleAppAlertDialog(
                    alertDialogEventsState = viewModel.alertDialogEventsState.value,
                    alertDialogUiActionListener = viewModel.alertDialogUiActionListener
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun TimeSlotExample(
    allDayEvents: List<AllDayEvent>,
    timeSlotsStateController: TimeSlotsStateController,
    uiActionListener: DayScheduleAppViewModel.UiActionListener
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.height(56.dp).fillMaxWidth().background(Color.LightGray)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "All Day Events = ${allDayEvents.size}"
            )
        }
        TimeSlotsView(
            timeSlotsStateController = timeSlotsStateController
        ) { localTimeEvent ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(all = 1.dp)
                    .background(color = generateRandomColor(localTimeEvent.uuid))
                    .combinedClickable(
                        onClick = {
                            uiActionListener.onTimeEventClicked(localTimeEvent)
                        },
                        onDoubleClick = {
                            uiActionListener.onTimeEventDoubleClicked(localTimeEvent)
                        },
                        onLongClick = {
                            uiActionListener.onTimeEventLongClicked(localTimeEvent)
                        }
                    )
            ) {
                Text(
                    text = "${localTimeEvent.title}: ${localTimeEvent.startTime}-${localTimeEvent.endTime}",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun DecimalSlotExample(
    decimalSlotsStateController: DecimalSlotsStateController,
    uiActionListener: DayScheduleAppViewModel.UiActionListener
) {
    Box(modifier = Modifier.fillMaxSize()) {
        DecimalSlotsView(
            decimalSlotsStateController = decimalSlotsStateController
        ) { decimalEvent ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(all = 1.dp)
                    .background(color = generateRandomColor(decimalEvent.uuid))
                    .combinedClickable(
                        onClick = {
                            uiActionListener.onDecimalEventClicked(decimalEvent)
                        },
                        onDoubleClick = {
                            uiActionListener.onDecimalEventDoubleClicked(decimalEvent)
                        },
                        onLongClick = {
                            uiActionListener.onDecimalEventLongClicked(decimalEvent)
                        }
                    )
            ) {
                Text(
                    text = "${decimalEvent.title}: ${decimalEvent.startValue}-${decimalEvent.endValue}",
                    fontSize = 12.sp
                )
            }
        }

    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun EpgSlotExample(
    epgSlotsStateController: EpgSlotsStateController,
    uiActionListener: DayScheduleAppViewModel.UiActionListener
) {
    EpgSlotsView(
        epgSlotsStateController = epgSlotsStateController
    ) { localTimeEvent ->
        Box(
            modifier = Modifier.fillMaxSize().padding(1.dp)
                .background(generateRandomColor(localTimeEvent.uuid))
                .combinedClickable(
                    onClick = {
                        uiActionListener.onEpgEventClicked(localTimeEvent)
                    },
                    onDoubleClick = {
                        uiActionListener.onEpgEventDoubleClicked(localTimeEvent)
                    },
                    onLongClick = {
                        uiActionListener.onEpgEventLongClicked(localTimeEvent)
                    }
                )
        ) {
            Text(
                text = "${localTimeEvent.title}: ${localTimeEvent.startTime}-${localTimeEvent.endTime}",
                fontSize = 12.sp
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
private val colorPerEventMap = mutableMapOf<Uuid, Color>()

@OptIn(ExperimentalUuidApi::class)
private fun generateRandomColor(uuid: Uuid): Color {
    colorPerEventMap[uuid]?.let { return it }
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color(red, green, blue).also { colorPerEventMap[uuid] = it }
}

@Preview(
    showBackground = true
)
@OptIn(ExperimentalUuidApi::class)
@Composable
fun DecimalSlotsViewPreview() {
    val decimalSlotsStateController = remember {
        DecimalSlotsStateController(
            decimalSlotConfig = DecimalSlotConfig(slotScale = 2),
            eventsArrangement = EventsArrangement.MixedDirections(EventWidthType.FixedSizeFillLastEvent)
        ).apply {
            // Prepare the initial data
            DecimalSlotsDataSample(decimalSlotsStateController = this)
        }
    }
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            DecimalSlotsView(decimalSlotsStateController = decimalSlotsStateController) { event ->
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(all = 1.dp)
                        .background(color = generateRandomColor(event.uuid))
                ) {
                    Text(
                        text = "${event.title}: ${event.startValue}-${event.endValue}",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview(
    showBackground = true
)
@Composable
fun EpgSlotsViewPreview() {
    val epgSlotsStateController = remember {
        EpgSlotsStateController(
            EpgChannelSlotConfig(
                timeSlotConfig = TimeSlotConfig(
                    startSlotTime = LocalTime(6, 0),
                    endSlotTime = LocalTime(23, 59)
                )
            )
        ).apply {
            EpgSlotsDataSample(epgSlotsStateController = this)
        }
    }
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            EpgSlotsView(epgSlotsStateController = epgSlotsStateController) { localEvent ->
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(all = 1.dp)
                        .background(color = generateRandomColor(localEvent.uuid))
                ) {
                    Text(
                        text = "${localEvent.title}: ${localEvent.startTime}-${localEvent.endTime}",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
