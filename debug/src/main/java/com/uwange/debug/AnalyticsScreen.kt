package com.uwange.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.uwange.analytics.DebugAnalyticsHelper
import com.uwange.analytics.LocalAnalyticsHelper
import com.uwange.navigation.AnalyticsRoute
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
private fun AnalyticsRoute() {
    when (val analyticsHelper = LocalAnalyticsHelper.current) {
        is DebugAnalyticsHelper -> AnalyticsScreen(analyticsHelper)
        else -> return
    }
}

@Composable
private fun AnalyticsScreen(analyticsHelper: DebugAnalyticsHelper) {
    val listState = rememberLazyListState()

    LazyColumn(
        listState = listState,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = listState, enabled = true)
    ) {

    }



//    val listState = rememberLazyListState()
//    val logs by analyticsHelper.stackedAnalytics.collectAsStateWithLifecycle()
//    val dateFormatter = remember {
//        SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
//    }
//
//    LazyColumn(
//        state = listState,
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScrollbar(state = listState, color = PieceTheme.colors.light3),
//    ) {
//        itemsIndexed(
//            items = logs.reversed(),
//            key = { idx, item -> item.first xor idx.toLong() }
//        ) { idx, (timestamp, userId, event) ->
//
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = PieceTheme.colors.white),
//                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
//            ) {
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(14.dp),
//                ) {
//                    Text(
//                        text = "전송 시각 : ${dateFormatter.format(Date(timestamp))}",
//                        style = PieceTheme.typography.bodySSB,
//                        color = PieceTheme.colors.dark3
//                    )
//
//                    Text(
//                        text = "유저 아이디 : ${userId.ifEmpty { "익명" }}",
//                        style = PieceTheme.typography.bodySSB,
//                        color = PieceTheme.colors.dark3,
//                    )
//
//                    Text(
//                        text = "eventType : ${event.type}",
//                        style = PieceTheme.typography.bodySSB,
//                        color = PieceTheme.colors.dark2,
//                    )
//
//                    event.properties?.let {
//                        it.forEach { property ->
//                            Text(
//                                text = "${property.key} : ${property.value}",
//                                style = PieceTheme.typography.bodySSB,
//                                color = PieceTheme.colors.dark2,
//                            )
//                        }
//                    }
//                }
//            }
//
//            if (idx != logs.lastIndex) {
//                HorizontalDivider(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 4.dp),
//                    color = PieceTheme.colors.dark3.copy(alpha = 0.12f),
//                    thickness = 1.dp
//                )
//            }
//        }
//
//        item { Spacer(Modifier.height(12.dp)) }
//    }
}

fun NavGraphBuilder.analyticsNavigation() {
    composable<AnalyticsRoute> {
        AnalyticsRoute()
    }
}