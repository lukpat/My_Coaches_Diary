package cz.lpatak.mycoachesdiary.ui.trainings

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.data.model.Training
import cz.lpatak.mycoachesdiary.databinding.FragmentTrainingDetailExercisesBinding
import cz.lpatak.mycoachesdiary.ui.trainings.util.ExerciseInTrainingAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class TrainingDetailFragmentExercises(private val trainingFromArgs: Training) : Fragment() {
    private lateinit var binding: FragmentTrainingDetailExercisesBinding
    private val adapter: ExerciseInTrainingAdapter = ExerciseInTrainingAdapter()
    private val trainingsViewModel: TrainingsViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_training_detail_exercises,
                container,
                false
        )
        with(binding) {
            lifecycleOwner = this@TrainingDetailFragmentExercises
        }

        binding.exercisesList.adapter = adapter
        binding.fabAddExerciseToTraining.setOnClickListener {
            val directions =
                    TrainingDetailFragmentDirections.actionNavigationTrainingDetailToNavigationAddExerciseToTraining()
            findNavController().navigate(directions)
        }

        adapter.setViewModel(trainingsViewModel)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadExercises()
    }

    //todo: Pomáhá, ale málo.
/*
    override fun onResume() {
        super.onResume()
        loadExercises()
    }
*/
    private fun loadExercises() {
        trainingsViewModel.loadExercisesInTraining().observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }


}