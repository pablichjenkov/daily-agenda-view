package com.macaosoftware.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.macaosoftware.ui.data.Sample3
import com.macaosoftware.ui.data.Slots
import org.jetbrains.compose.ui.tooling.preview.Preview

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
                val dailyAgendaStateController = remember {
                    DailyAgendaStateController(
                        slots = Slots.slots,
                        slotToEventMap = Sample3(Slots.slots).slotToEventMap,
                        config = Config.MixedDirections(
                            eventWidthType = EventWidthType.VariableSize
                        )
                    )
                }
                DailyAgendaView(
                    dailyAgendaState = dailyAgendaStateController.computeNextState()
                )
            }
        }
    }
}
