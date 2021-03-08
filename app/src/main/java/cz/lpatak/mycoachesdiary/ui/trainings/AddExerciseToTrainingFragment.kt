package cz.lpatak.mycoachesdiary.ui.trainings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentAddExerciseToTrainingBinding
import cz.lpatak.mycoachesdiary.ui.trainings.util.ExerciseAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddExerciseToTrainingFragment : Fragment() {
    private val trainingsViewModel: TrainingsViewModel by viewModel()
    private lateinit var binding: FragmentAddExerciseToTrainingBinding
    private val adapter: ExerciseAdapter = ExerciseAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_exercise_to_training,
                container,
                false
        )
        binding.trainingsList.adapter = adapter
        adapter.setViewModel(trainingsViewModel)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadExercises()
    }

    private fun loadExercises() {
        trainingsViewModel.loadExercises().observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }

}