package com.raulespim.tictac.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.raulespim.tictac.ui.screens.login.LoginScreen
import com.raulespim.tictac.ui.screens.medication.MedicationScreen
import com.raulespim.tictac.ui.screens.task.TaskScreen

@Composable
fun RootScreen(component: RootComponent) {
    Children(stack = component.childStack) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.Login -> LoginScreen(instance.component)
            is RootComponent.Child.Medication -> MedicationScreen(instance.component)
            is RootComponent.Child.Task -> TaskScreen(instance.component)
        }
    }
}