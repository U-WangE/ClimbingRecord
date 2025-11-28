package com.uwange.debug

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uwange.common.ui.isRelease
import com.uwange.designsystem.foundation.ClimbingRecordTheme
import com.uwange.navigation.AnalyticsRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DebugDrawer(
    navController: NavController,
    content: @Composable () -> Unit
) {
    if (isRelease()) {
        content()
    } else {
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = ClimbingRecordTheme.colors.white,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(vertical = 12.dp)
                ) {
                    DebugDrawerItem(
                        title = "로그 센터",
                        drawerState = drawerState,
                        scope = scope,
                        onClick = { navController.navigate(AnalyticsRoute) }
                    )
                }
            },
            content = content
        )
    }
}

@Composable
private fun DebugDrawerItem(
    title: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(title) },
        selected = false,
        onClick = {
            scope.launch { drawerState.close() }
            onClick()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
