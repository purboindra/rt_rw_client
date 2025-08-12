package org.purboyndradev.rt_rw

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.purboyndradev.rt_rw.features.navigation.NavigationGraph
import org.purboyndradev.rt_rw.features.navigation.StartDestinationData
import org.purboyndradev.rt_rw.theme.RTRWTheme

@Composable
@Preview
fun App(startDestination: StartDestinationData? = null) {
    RTRWTheme {
        NavigationGraph(
            modifier = Modifier.fillMaxSize(),
            startDestination = startDestination
        )
    }
}