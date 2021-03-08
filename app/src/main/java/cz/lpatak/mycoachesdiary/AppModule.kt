package cz.lpatak.mycoachesdiary

import cz.lpatak.mycoachesdiary.data.repositories.*
import cz.lpatak.mycoachesdiary.data.source.AuthDataSource
import cz.lpatak.mycoachesdiary.data.source.FirestoreSource
import cz.lpatak.mycoachesdiary.ui.auth.viewmodel.AuthViewModel
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExercisesViewModel
import cz.lpatak.mycoachesdiary.ui.matches.viewmodel.MatchesViewModel
import cz.lpatak.mycoachesdiary.ui.myteams.viewmodel.MyTeamsViewModel
import cz.lpatak.mycoachesdiary.ui.stats.StatsViewModel
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import cz.lpatak.mycoachesdiary.util.PreferenceManger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { PreferenceManger(get()) }

    single { AuthDataSource() }
    single { FirestoreSource() }

    single<AuthRepository> { AuthRepositoryImpl(get()) }

    single { TeamRepositoryImpl(get()) }
    single { MatchRepositoryImpl(get(), get()) }
    single { TrainingRepositoryImpl(get(), get()) }
    single { ExerciseRepositoryImpl(get()) }
    single { ExerciseInTrainingRepositoryImpl(get(), get()) }

    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { MyTeamsViewModel(get(), get()) }
    viewModel { TrainingsViewModel(get(), get(), get(), get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { MatchesViewModel(get(), get()) }
    viewModel { ExercisesViewModel(get()) }
}