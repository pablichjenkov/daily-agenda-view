package com.macaosoftware.ui.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.macaosoftware.ui.dailyagenda.decimalslots.DecimalEvent
import com.macaosoftware.ui.dailyagenda.decimalslots.DecimalSlotConfig
import com.macaosoftware.ui.dailyagenda.decimalslots.DecimalSlotsStateController
import com.macaosoftware.ui.dailyagenda.decimalslots.EventWidthType
import com.macaosoftware.ui.dailyagenda.decimalslots.EventsArrangement
import com.macaosoftware.ui.dailyagenda.epgslots.EpgChannel
import com.macaosoftware.ui.dailyagenda.epgslots.EpgChannelSlotConfig
import com.macaosoftware.ui.dailyagenda.epgslots.EpgSlotsStateController
import com.macaosoftware.ui.dailyagenda.timeslots.LocalTimeEvent
import com.macaosoftware.ui.dailyagenda.timeslots.TimeSlotConfig
import com.macaosoftware.ui.dailyagenda.timeslots.TimeSlotsStateController
import com.macaosoftware.ui.data.Constants
import com.macaosoftware.ui.data.DecimalSlotsDataSample
import com.macaosoftware.ui.data.EpgSlotsDataSample
import com.macaosoftware.ui.data.TimeSlotsDataSample
import com.macaosoftware.ui.ui.model.AllDayEvent
import kotlinx.datetime.LocalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DayScheduleAppViewModel {

    var slotsViewType by mutableStateOf(value = SlotsViewType.Timeline)

    // region: AllDayEvent

    val allDayEvents = mutableListOf<AllDayEvent>()

    @OptIn(ExperimentalUuidApi::class)
    fun addAllDayEvent(
        uuid: Uuid = Uuid.random(),
        title: String,
        description: String
    ): Boolean {
        return addAllDayEvent(
            AllDayEvent(
                uuid = uuid,
                title = title,
                description = description
            )
        )
    }

    fun addAllDayEvent(allDayEvent: AllDayEvent): Boolean {
        return allDayEvents.add(allDayEvent)
    }

    // endregion

    // region: timeSlotsStateController

    val timeSlotsStateController by lazy {
        TimeSlotsStateController(
            timeSlotConfig = TimeSlotConfig(
                startSlotTime = LocalTime(7, 0), // 7:00 AM
                endSlotTime = LocalTime(19, 0), // 7:00 PM
                useAmPm = true,
                slotScale = 2,
                slotHeight = 48
            ),
            eventsArrangement = EventsArrangement.MixedDirections(EventWidthType.FixedSizeFillLastEvent)
        ).apply {
            TimeSlotsDataSample(timeSlotsStateController = this)
        }
    }

    internal interface TimeSlotsUiActionListener {
        fun confirmedAddTimeEvent(
            title: String,
            description: String,
            startLocalTime: LocalTime,
            endLocalTime: LocalTime
        )

        fun showRemoveTimeEventForm(timeEvent: LocalTimeEvent)
        fun confirmedRemoveTimeEvent(eventTitle: String)
        fun onTimeEventClicked(timeEvent: LocalTimeEvent)
        fun onTimeEventDoubleClicked(timeEvent: LocalTimeEvent)
        fun onTimeEventLongClicked(timeEvent: LocalTimeEvent)
    }

    @OptIn(ExperimentalUuidApi::class)
    internal val timeSlotsUiActionListener = object : TimeSlotsUiActionListener {

        override fun confirmedAddTimeEvent(
            title: String,
            description: String,
            startLocalTime: LocalTime,
            endLocalTime: LocalTime
        ) {
            uiActionListener.dismissInputForm()
            timeSlotsStateController.timeSlotsDataUpdater.postUpdate {
                addEvent(
                    title = title,
                    description = description,
                    startTime = startLocalTime,
                    endTime = endLocalTime
                )
            }
        }

        override fun showRemoveTimeEventForm(timeEvent: LocalTimeEvent) {
            bottomSheetEventsState.value =
                BottomSheetEventsState.RemoveTimedEventRequested(localTimeEvent = timeEvent)
        }

        override fun confirmedRemoveTimeEvent(eventTitle: String) {
            uiActionListener.dismissInputForm()
            timeSlotsStateController.timeSlotsDataUpdater.postUpdate {
                removeEventByTitle(eventTitle = eventTitle)
            }
        }

        override fun onTimeEventClicked(timeEvent: LocalTimeEvent) {
            bottomSheetEventsState.value =
                BottomSheetEventsState.ShowTimedEventRequested(localTimeEvent = timeEvent)
        }

        override fun onTimeEventDoubleClicked(timeEvent: LocalTimeEvent) {
            showRemoveTimeEventForm(timeEvent)
        }

        override fun onTimeEventLongClicked(timeEvent: LocalTimeEvent) {
            showRemoveTimeEventForm(timeEvent)
        }
    }

    // endregion

    // region: DecimalSlotsStateController

    val decimalSlotsStateController by lazy {
        DecimalSlotsStateController(
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
    }

    internal interface DecimalSlotsUiActionListener {
        fun confirmedAddDecimalSegment(title: String, startValue: Float, endValue: Float)
        fun showRemoveDecimalEventForm(decimalEvent: DecimalEvent)
        fun confirmedRemoveDecimalEvent(eventTitle: String)
        fun onDecimalEventClicked(decimalEvent: DecimalEvent)
        fun onDecimalEventDoubleClicked(decimalEvent: DecimalEvent)
        fun onDecimalEventLongClicked(decimalEvent: DecimalEvent)
    }

    @OptIn(ExperimentalUuidApi::class)
    internal val decimalSlotsUiActionListener = object : DecimalSlotsUiActionListener {

        override fun confirmedAddDecimalSegment(
            title: String,
            startValue: Float,
            endValue: Float
        ) {
            uiActionListener.dismissInputForm()
            decimalSlotsStateController.decimalSlotsDataUpdater.postUpdate {
                addDecimalEvent(
                    title = title,
                    description = Constants.EmptyDescription,
                    startValue = startValue,
                    endValue = endValue
                )
            }
        }

        override fun showRemoveDecimalEventForm(decimalEvent: DecimalEvent) {
            bottomSheetEventsState.value =
                BottomSheetEventsState.RemoveDecimalEventRequested(decimalEvent)
        }

        override fun confirmedRemoveDecimalEvent(eventTitle: String) {
            uiActionListener.dismissInputForm()
            decimalSlotsStateController.decimalSlotsDataUpdater.postUpdate {
                removeDecimalEventByTittle(eventTitle = eventTitle)
            }
        }

        override fun onDecimalEventClicked(decimalEvent: DecimalEvent) {
            bottomSheetEventsState.value =
                BottomSheetEventsState.ShowDecimalEventRequested(decimalEvent)
        }

        override fun onDecimalEventDoubleClicked(decimalEvent: DecimalEvent) {
            showRemoveDecimalEventForm(decimalEvent)
        }

        override fun onDecimalEventLongClicked(decimalEvent: DecimalEvent) {
            showRemoveDecimalEventForm(decimalEvent)
        }
    }

    // endregion

    // region EpgSlotsStateController

    @OptIn(ExperimentalUuidApi::class)
    val epgSlotsStateController by lazy {
        EpgSlotsStateController(
            EpgChannelSlotConfig(
                topHeaderHeight = 48,
                channelWidth = 96,
                timeSlotConfig = TimeSlotConfig(
                    startSlotTime = LocalTime(6, 0),
                    endSlotTime = LocalTime(23, 59)
                )
            )
        ).apply {
            EpgSlotsDataSample(epgSlotsStateController = this)
        }
    }

    internal interface EpgSlotsUiActionListener {
        fun confirmedAddEpgEvent(
            title: String,
            description: String,
            startLocalTime: LocalTime,
            endLocalTime: LocalTime
        )

        fun confirmedRemoveEpgEvent(eventTitle: String)
        fun onEpgEventClicked(localTimeEvent: LocalTimeEvent)
        fun onEpgEventDoubleClicked(localTimeEvent: LocalTimeEvent)
        fun onEpgEventLongClicked(localTimeEvent: LocalTimeEvent)
    }

    @OptIn(ExperimentalUuidApi::class)
    internal val epgSlotsUiActionListener = object : EpgSlotsUiActionListener {

        override fun confirmedAddEpgEvent(
            title: String,
            description: String,
            startLocalTime: LocalTime,
            endLocalTime: LocalTime
        ) {
            uiActionListener.dismissInputForm()
            epgSlotsStateController.epgSlotsDataUpdater.postUpdate {
                addChannel(
                    EpgChannel(
                        name = "New Channel",
                        events = listOf(
                            LocalTimeEvent(
                                uuid = Uuid.random(),
                                title = title,
                                description = description,
                                startTime = startLocalTime,
                                endTime = endLocalTime
                            )
                        )
                    )
                )
            }
        }

        override fun confirmedRemoveEpgEvent(eventTitle: String) {
            TODO("Not yet implemented")
        }

        override fun onEpgEventClicked(localTimeEvent: LocalTimeEvent) {
            bottomSheetEventsState.value =
                BottomSheetEventsState.ShowEpgEventRequested(epgEvent = localTimeEvent)
        }

        override fun onEpgEventDoubleClicked(localTimeEvent: LocalTimeEvent) {
            bottomSheetEventsState.value =
                BottomSheetEventsState.RemoveEpgEventRequested(epgEvent = localTimeEvent)
        }

        override fun onEpgEventLongClicked(localTimeEvent: LocalTimeEvent) {
            bottomSheetEventsState.value =
                BottomSheetEventsState.RemoveEpgEventRequested(epgEvent = localTimeEvent)
        }
    }

    // endregion

    // region: Global screen events

    var bottomSheetEventsState =
        mutableStateOf<BottomSheetEventsState>(BottomSheetEventsState.Hidden)

    internal interface UiActionListener {
        fun showAddEventForm(slotsViewType: SlotsViewType)
        fun toggleAxisType(slotsViewType: SlotsViewType)
        fun dismissInputForm()
    }

    @OptIn(ExperimentalUuidApi::class)
    internal val uiActionListener = object : UiActionListener {

        override fun showAddEventForm(slotsViewType: SlotsViewType) {
            when (slotsViewType) {
                SlotsViewType.Decimal -> {
                    bottomSheetEventsState.value =
                        BottomSheetEventsState.AddDecimalEventRequested
                }

                SlotsViewType.Timeline -> {
                    bottomSheetEventsState.value =
                        BottomSheetEventsState.AddTimedEventRequested
                }

                SlotsViewType.Epg -> {
                    bottomSheetEventsState.value =
                        BottomSheetEventsState.AddEpgEventRequested
                }
            }
        }

        override fun toggleAxisType(slotsViewType: SlotsViewType) {
            if (this@DayScheduleAppViewModel.slotsViewType == slotsViewType) return
            this@DayScheduleAppViewModel.slotsViewType = slotsViewType
        }

        override fun dismissInputForm() {
            bottomSheetEventsState.value = BottomSheetEventsState.Hidden
        }
    }

    var alertDialogEventsState =
        mutableStateOf<AlertDialogEventsState>(value = AlertDialogEventsState.Hidden)

    internal interface AlertDialogUiActionListener {
        fun hideAlert()
        fun showAlert(text: String)
    }

    internal val alertDialogUiActionListener = object : AlertDialogUiActionListener {
        override fun hideAlert() {
            alertDialogEventsState.value = AlertDialogEventsState.Hidden
        }

        override fun showAlert(text: String) {
            alertDialogEventsState.value = AlertDialogEventsState.ShowingInfo(text = text)
        }
    }

    // endregion

}
