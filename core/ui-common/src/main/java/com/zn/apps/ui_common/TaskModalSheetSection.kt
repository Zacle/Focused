package com.zn.apps.ui_common

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.ime
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.zn.apps.model.data.task.Task
import com.zn.apps.ui_common.delegate.TasksUiStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TaskModalSheetSection(
    defaultTaskReminder: Int,
    coroutineScope: CoroutineScope,
    upsertTask: (Task) -> Unit,
    shouldShowModalSheet: (Boolean) -> Unit,
    uiStateHolder: TasksUiStateHolder,
    showModalBottomSheet: Boolean
) {
    val bottomSheetState = rememberModalBottomSheetState()

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                shouldShowModalSheet(false)
            }
        }
    }

    AnimatedVisibility(
        visible = showModalBottomSheet,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically()
    ) {
        ModalBottomSheet(
            onDismissRequest = { shouldShowModalSheet(false)},
            sheetState = bottomSheetState,
            windowInsets = WindowInsets.ime.add(BottomSheetDefaults.windowInsets)
        ) {
            InsertTaskBottomSheetContent(
                upsertTask = upsertTask,
                projects = uiStateHolder.projects,
                tags = uiStateHolder.tags,
                shouldShowModalSheet = { shouldShowModalSheet(it) },
                taskReminder = defaultTaskReminder
            )
        }
    }
}