package com.macaosoftware.ui.dailyagenda

class EventsManager(val slotsController: SlotsController) {

    val slotToEventMap: MutableMap<Slot, List<Event>> = mutableMapOf()
}