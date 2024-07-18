package com.ccc.ncs.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccc.ncs.designsystem.component.CommonModalBottomSheet
import com.ccc.ncs.model.MusicTag

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun <T: MusicTag> MusicTagBottomSheet(
    onDismissRequest: () -> Unit,
    items: List<T>,
    onItemSelected: (T?) -> Unit
) {
    CommonModalBottomSheet(onDismissRequest = onDismissRequest) {
        FlowRow(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(onClick = {
                onItemSelected(null)
                onDismissRequest()
            }) {
                Text(text = "DELETE")
            }

            items.forEach {
                OutlinedButton(onClick = {
                    onItemSelected(it)
                    onDismissRequest()
                }) {
                    Text(text = it.name)
                }
            }
        }
    }
}