package com.raulespim.tictac.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.raulespim.tictac.domain.usecase.CheckAuthStatusUseCase
import com.raulespim.tictac.ui.screens.login.LoginComponent
import com.raulespim.tictac.ui.screens.medication.MedicationComponent
import com.raulespim.tictac.ui.screens.task.TaskComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@Serializable
sealed class ScreenConfig {
    @Serializable
    data object Login : ScreenConfig()

    @Serializable
    data object Medication : ScreenConfig()

    @Serializable
    data object Task : ScreenConfig()
}

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>

    fun navigateTo(screenConfig: ScreenConfig)
    fun navigateBack()

    sealed class Child {
        data class Login(val component: LoginComponent) : Child()
        data class Medication(val component: MedicationComponent) : Child()
        data class Task(val component: TaskComponent) : Child()
    }
}

class RootComponentImpl(
    componentContext: ComponentContext,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : RootComponent, ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<ScreenConfig>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = ScreenConfig.serializer(),
        initialConfiguration = if (checkAuthStatusUseCase.invoke()) ScreenConfig.Medication else ScreenConfig.Login,
        handleBackButton = true,
        childFactory = { config, componentContext ->
            when (config) {
                is ScreenConfig.Login -> RootComponent.Child.Login(
                    get<LoginComponent> { parametersOf(componentContext) }
                )
                is ScreenConfig.Medication -> RootComponent.Child.Medication(
                    get<MedicationComponent> { parametersOf(componentContext) }
                )
                is ScreenConfig.Task -> RootComponent.Child.Task(
                    get<TaskComponent> { parametersOf(componentContext) }
                )
            }
        }
    )

    @OptIn(DelicateDecomposeApi::class)
    override fun navigateTo(screenConfig: ScreenConfig) {
        navigation.push(screenConfig)
    }

    override fun navigateBack() {
        navigation.pop()
    }

}