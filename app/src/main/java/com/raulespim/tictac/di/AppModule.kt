package com.raulespim.tictac.di

import android.content.Context
import androidx.room.Room
import com.arkivanov.decompose.ComponentContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.raulespim.tictac.data.local.AppDatabase
import com.raulespim.tictac.data.repository.AuthRepository
import com.raulespim.tictac.data.repository.AuthRepositoryImpl
import com.raulespim.tictac.data.repository.MedicationFirestoreRepository
import com.raulespim.tictac.data.repository.MedicationFirestoreRepositoryImpl
import com.raulespim.tictac.data.repository.MedicationRepository
import com.raulespim.tictac.data.repository.MedicationRepositoryImpl
import com.raulespim.tictac.data.repository.TaskFirestoreRepository
import com.raulespim.tictac.data.repository.TaskFirestoreRepositoryImpl
import com.raulespim.tictac.data.repository.TaskRepository
import com.raulespim.tictac.data.repository.TaskRepositoryImpl
import com.raulespim.tictac.domain.usecase.AddMedicationUseCase
import com.raulespim.tictac.domain.usecase.AddTaskUseCase
import com.raulespim.tictac.domain.usecase.CheckAuthStatusUseCase
import com.raulespim.tictac.domain.usecase.GetMedicationsUseCase
import com.raulespim.tictac.domain.usecase.GetTasksUseCase
import com.raulespim.tictac.domain.usecase.GoogleSignInUseCase
import com.raulespim.tictac.domain.usecase.SyncMedicationsUseCase
import com.raulespim.tictac.domain.usecase.SyncTasksUseCase
import com.raulespim.tictac.domain.usecase.UpdateMedicationStatusUseCase
import com.raulespim.tictac.domain.usecase.UpdateTaskStatusUseCase
import com.raulespim.tictac.ui.navigation.RootComponent
import com.raulespim.tictac.ui.navigation.RootComponentImpl
import com.raulespim.tictac.ui.navigation.ScreenConfig
import com.raulespim.tictac.ui.screens.login.LoginComponent
import com.raulespim.tictac.ui.screens.login.LoginComponentImpl
import com.raulespim.tictac.ui.screens.medication.MedicationComponent
import com.raulespim.tictac.ui.screens.medication.MedicationComponentImpl
import com.raulespim.tictac.ui.screens.task.TaskComponent
import com.raulespim.tictac.ui.screens.task.TaskComponentImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {

    //data
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single<AppDatabase> {
        Room.databaseBuilder(
            get<Context>(),
            AppDatabase::class.java,
            "tictac_db"
        ).build()
    }
    single { get<AppDatabase>().medicationDao() }
    single { get<AppDatabase>().taskDao() }
    single<MedicationRepository> { MedicationRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<MedicationFirestoreRepository> { MedicationFirestoreRepositoryImpl(get()) }
    single<TaskFirestoreRepository> { TaskFirestoreRepositoryImpl(get()) }

    //domain
    factory { AddMedicationUseCase(get()) }
    factory { GetMedicationsUseCase(get()) }
    factory { UpdateMedicationStatusUseCase(get()) }
    factory { AddTaskUseCase(get()) }
    factory { GetTasksUseCase(get()) }
    factory { UpdateTaskStatusUseCase(get()) }
    factory { GoogleSignInUseCase(get()) }
    factory { CheckAuthStatusUseCase(get()) }
    factory { SyncMedicationsUseCase(get(), get()) }
    factory { SyncTasksUseCase(get(), get()) }

    // UI Components
    factory<RootComponent> { (componentContext: ComponentContext) ->
        RootComponentImpl(
            componentContext = componentContext,
            checkAuthStatusUseCase = get()
        )
    }
    factory<LoginComponent> { (componentContext: ComponentContext) ->
        LoginComponentImpl(
            componentContext = componentContext,
            googleSignInUseCase = get(),
            onNavigateToHome = {
                get<RootComponent>(parameters = { parametersOf(componentContext) }).navigateTo(
                    ScreenConfig.Medication
                )
            }
        )
    }
    factory<MedicationComponent> { (componentContext: ComponentContext) ->
        MedicationComponentImpl(
            componentContext = componentContext,
            addMedicationUseCase = get(),
            getMedicationsUseCase = get(),
            updateMedicationStatusUseCase = get(),
            userId = get<AuthRepository>().getCurrentUserId() ?: ""
        )
    }
    factory<TaskComponent> { (componentContext: ComponentContext) ->
        TaskComponentImpl(
            componentContext = componentContext,
            addTaskUseCase = get(),
            getTasksUseCase = get(),
            updateTaskStatusUseCase = get(),
            userId = get<AuthRepository>().getCurrentUserId() ?: ""
        )
    }
}