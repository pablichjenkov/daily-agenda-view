package com.macaosoftware.ui.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DayScheduleAppAlertDialog(
    alertDialogEventsState: AlertDialogEventsState,
    alertDialogUiActionListener: DayScheduleAppViewModel.AlertDialogUiActionListener
) {
    when (alertDialogEventsState) {
        AlertDialogEventsState.Hidden -> {
            // no-op
        }

        is AlertDialogEventsState.ShowingInfo -> {
            BasicAlertDialog(
                onDismissRequest = { alertDialogUiActionListener.hideAlert() }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = alertDialogEventsState.text)
                        Button(onClick = { alertDialogUiActionListener.hideAlert() }) {
                            Text("Dismiss")
                        }
                    }
                }
            }
        }
    }
}
