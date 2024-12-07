package com.ccc.ncs.ui.component

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.CommonModalBottomSheet
import com.ccc.ncs.model.MusicTag

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun <T: MusicTag> MusicTagBottomSheet(
    onDismissRequest: () -> Unit,
    items: List<T>,
    onItemSelected: (T?) -> Unit
) {
    val nullContainedItems: List<T?> = remember {
        listOf(null) + items
    }

    CommonModalBottomSheet(onDismissRequest = onDismissRequest) {
        FlowRow(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            nullContainedItems.forEach { musicTag ->
                MusicTagButton(
                    item = musicTag,
                    onClick = {
                        onItemSelected(it)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}


@Composable
fun <T: MusicTag> MusicTagButton(
    modifier: Modifier = Modifier,
    item: T?,
    onClick: (T?) -> Unit
) {
    OutlinedButton(
        onClick = { onClick(item) },
        border = null
    ) {
        if (item != null) {
            Text(text = "#${item.name}")
        } else {
            Text(
                text = stringResource(R.string.Delete),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}