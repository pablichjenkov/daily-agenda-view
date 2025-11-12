package com.macaosoftware.ui.dailyagenda

import androidx.compose.runtime.mutableStateOf

class DailyAgendaStateController(
    eventsManager: EventsManager,
    private val eventsArrangement: EventsArrangement
) {

    private val slotToEventMap: Map<Slot, List<Event>> = eventsManager.slotToEventMap
    private val slotsController: SlotsController = eventsManager.slotsController
    private val config = Config(
        eventsArrangement = eventsArrangement,
        initialSlotValue = slotsController.firstSlot.value,
        slotScale = slotsController.slotScale,
        slotHeight = slotsController.slotHeight,
        timelineLeftPadding = 72
    )
    private val slots = slotsController.slots
    private val slotToEventMapSorted: MutableMap<Slot, MutableList<Event>> = mutableMapOf()

    init {
        /**
         * Sort the events to maximize spacing when the layout runs.
         * */
        val endTimeComparator = Comparator { event1: Event, event2: Event ->
            val diff = event2.endValue - event1.endValue
            when {
                (diff > 0F) -> 1
                (diff < 0F) -> -1
                else -> 0
            }
        }
        slotToEventMap.entries.forEach { entry ->
            val eventsSortedByEndTime = entry.value.sortedWith(endTimeComparator).toMutableList()
            slotToEventMapSorted.put(entry.key, eventsSortedByEndTime)
        }
    }

    /**
     * This have to be declared after init{} execution. To guarantee that there is data in the
     * slotToEventMapSorted and that the events are sorted.
     */
    val state = mutableStateOf<DailyAgendaState>(value = computeNextState())

    fun addEvent(
        startTime: Float,
        endTime: Float,
        title: String
    ): Boolean {
        val eventSlot = slotsController.getSlotForValue(startValue = startTime)
        val siblingEvents = slotToEventMapSorted[eventSlot]?.toMutableList() ?: return false

        val index = siblingEvents.binarySearch(fromIndex = 0, toIndex = siblingEvents.lastIndex) {
            val diff = it.endValue - endTime
            when {
                (diff > 0F) -> 1
                (diff < 0F) -> -1
                else -> 0
            }
        }
        val insertionIndex = if (index < 0) -(index + 1) else index
        siblingEvents.add(
            insertionIndex,
            Event(
                startValue = startTime,
                endValue = endTime,
                title = title
            )
        )

        // Update state
        updateState()
        return true
    }

    fun addEvent(event: Event): Boolean {
        val eventSlot = slotsController.getSlotForValue(startValue = event.startValue)
        val siblingEvents = slotToEventMapSorted[eventSlot]?.toMutableList() ?: return false

        val index = siblingEvents.binarySearch(fromIndex = 0, toIndex = siblingEvents.lastIndex) {
            val diff = it.endValue - event.endValue
            when {
                (diff > 0F) -> 1
                (diff < 0F) -> -1
                else -> 0
            }
        }
        val insertionIndex = if (index < 0) -(index + 1) else index
        siblingEvents.add(insertionIndex, event)

        // Update state
        updateState()
        return true
    }

    fun removeEvent(event: Event): Boolean {
        val eventSlot = slotsController.getSlotForValue(startValue = event.startValue)
        val siblingEvents = slotToEventMapSorted[eventSlot]?.toMutableList() ?: return false
        return if (siblingEvents.remove(event)) { // Update only if removal was success
            updateState()
            true
        } else false
    }

    private fun computeNextState(): DailyAgendaState {
        val result = computeSlotInfo(
            slots = slots,
            slotToEventMap = slotToEventMapSorted,
            config = config
        )
        return DailyAgendaState(
            slots = slots,
            slotToEventMap = slotToEventMapSorted,
            slotInfoMap = result.slotInfoMap,
            maxColumns = result.maxColumns,
            config = config
        )
    }

    private fun updateState() {
        state.value = computeNextState()
    }

    /**
     * Computes the amount of events that are contained by the given slop. The events will be the
     * sum of earlier slots events plus this slot's events.
     * */
    private fun computeSlotInfo(
        slots: List<Slot>,
        slotToEventMap: Map<Slot, List<Event>>,
        config: Config
    ): ComputeSlotInfoResult {

        val slotInfoMap = mutableMapOf<Slot, SlotInfo>()
        var maxColumns = 1

        var isLeftIter = when (config.eventsArrangement) {
            is EventsArrangement.LeftToRight,
            is EventsArrangement.MixedDirections -> true

            is EventsArrangement.RightToLeft -> false
        }

        slots.forEachIndexed { idx, slotIter ->

            val slotLeftColumnMap = mutableMapOf<Slot, Int>()
            val slotRightColumnMap = mutableMapOf<Slot, Int>()

            slotToEventMap[slotIter]?.forEachIndexed { idx, event ->

                val eventSlot = slotsController.getSlotForValue(startValue = event.startValue)

                getSlotsIncludeStartSlot(event, eventSlot, slots).forEach { containingSlot ->
                    // Update column mark
                    if (isLeftIter) {
                        slotLeftColumnMap.put(containingSlot, idx + 1)
                    } else {
                        slotRightColumnMap.put(containingSlot, idx + 1)
                    }

                    // Update events counter
                    val currentSlotInfo =
                        slotInfoMap.getOrPut(containingSlot) { SlotInfo(0, 0, 0) }
                    currentSlotInfo.numberOfContainingEvents++
                }
            }

            val iterSlotInfo = slotInfoMap.getOrPut(slotIter) { SlotInfo(0, 0, 0) }
            if (isLeftIter) {
                slotLeftColumnMap.entries.forEach { entry ->
                    if (entry.key.title != slotIter.title) {
                        val entrySlotInfo = slotInfoMap.getOrPut(entry.key) { SlotInfo(0, 0, 0) }
                        entrySlotInfo.numberOfColumnsLeft =
                            iterSlotInfo.numberOfColumnsLeft + entry.value
                    }
                }
                iterSlotInfo.numberOfColumnsLeft += slotLeftColumnMap.entries.firstOrNull()?.value
                    ?: 0
            } else {
                slotRightColumnMap.entries.forEach { entry ->
                    if (entry.key.title != slotIter.title) {
                        val entrySlotInfo = slotInfoMap.getOrPut(entry.key) { SlotInfo(0, 0, 0) }
                        entrySlotInfo.numberOfColumnsRight =
                            iterSlotInfo.numberOfColumnsRight + entry.value
                    }
                }
                iterSlotInfo.numberOfColumnsRight += slotRightColumnMap.entries.firstOrNull()?.value
                    ?: 0
            }

            // In the case of eventsArrangement == EventsArrangement.MixedDirections, then
            // lets change the layout direction.
            if (config.eventsArrangement is EventsArrangement.MixedDirections) {
                isLeftIter = !isLeftIter
            }
        }

        slotInfoMap.entries.fold(1) { acc, entry ->
            val slotColumns = entry.value.getTotalColumnSpans()
            if (slotColumns > maxColumns) maxColumns = slotColumns
            maxColumns
        }
        println("DailyAgendaState: maxColumns: $maxColumns")
        return ComputeSlotInfoResult(slotInfoMap, maxColumns)
    }
}

private class ComputeSlotInfoResult(
    val slotInfoMap: Map<Slot, SlotInfo>,
    val maxColumns: Int
)
