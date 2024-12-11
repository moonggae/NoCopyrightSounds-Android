package com.ccc.ncs.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.ccc.ncs.analytics.AnalyticsEvent.Param
import com.ccc.ncs.analytics.AnalyticsEvent.ParamKeys
import com.ccc.ncs.analytics.AnalyticsEvent.Types

fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = Types.SCREEN_VIEW,
            extras = listOf(
                Param(ParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}

@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}