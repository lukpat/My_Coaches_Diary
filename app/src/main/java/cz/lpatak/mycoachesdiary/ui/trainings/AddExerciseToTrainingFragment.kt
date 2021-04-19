package cz.lpatak.mycoachesdiary.ui.trainings

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Exercise
import cz.lpatak.mycoachesdiary.data.model.ExerciseInTraining
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentAddExerciseToTrainingBinding
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExercisesViewModel
import cz.lpatak.mycoachesdiary.ui.trainings.util.ExerciseAdapter
import cz.lpatak.mycoachesdiary.ui.trainings.viewmodel.TrainingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddExerciseToTrainingFragment : Fragment(),
    androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private val exercisesViewModel: ExercisesViewModel by viewModel()
    private val trainingsViewModel: TrainingsViewModel by viewModel()

    private lateinit var binding: FragmentAddExerciseToTrainingBinding
    private val adapter: ExerciseAdapter = ExerciseAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_exercise_to_training,
                container,
                false
            )

        with(binding) {
            exercisesList.adapter = adapter
            filterOn = false
            exerciseLibraryFilter.btnSetFilter.setOnClickListener { applyFilter() }
        }


        adapter.setViewModel(trainingsViewModel)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadExercises()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exercise_library_menu, menu)
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as? androidx.appcompat.widget.SearchView
        searchView?.apply {
            setOnQueryTextListener(this@AddExerciseToTrainingFragment)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                binding.filterOn = binding.filterOn == false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadExercises() {
        exercisesViewModel.loadExercises().observe(viewLifecycleOwner, { result ->
            binding.result = result
            if (result is Result.Success) {
                adapter.submitList(result.data)
            }
        })
    }

    private fun loadExercisesWithFilter(exerciseOwner: Boolean, category: String) {
        exercisesViewModel.loadExercisesFilter(exerciseOwner, category)
            .observe(viewLifecycleOwner, { result ->
                binding.result = result
                if (result is Result.Success) {
                    adapter.submitList(result.data)
                }
            })
    }

    private fun applyFilter() {
        val exerciseOwner =
            getIndex(binding.exerciseLibraryFilter.exerciseOwner.selectedItem.toString()) == 0

        val category = binding.exerciseLibraryFilter.exerciseCategory.selectedItem.toString()
        loadExercisesWithFilter(exerciseOwner, category)
        binding.filterOn = false
    }

    private fun getIndex(type: String): Int {
        return when (type) {
            "Jen moje cvičení" -> 0
            else -> 1
        }
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            searchDB(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //nic - dělám až na konci
        return true
    }

    private fun searchDB(query: String) {
        exercisesViewModel.searchData(query)
            .observe(viewLifecycleOwner, { result ->
                binding.result = result
                if (result is Result.Success) {
                    adapter.submitList(result.data)
                }
            })
    }

    fun addExerciseToTraining(exercise: Exercise) {
        val exerciseInTraining = ExerciseInTraining(
            exercise.id,
            exercise.name,
            exercise.category,
            exercise.description,
            exercise.imageUrl,
            0
        )

        trainingsViewModel.addExerciseToTraining(exerciseInTraining, this)
    }
}




