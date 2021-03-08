package cz.lpatak.mycoachesdiary.ui.exercises

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.data.model.Result
import cz.lpatak.mycoachesdiary.databinding.FragmentExerciseLibraryBinding
import cz.lpatak.mycoachesdiary.ui.exercises.util.ExerciseAdapter
import cz.lpatak.mycoachesdiary.ui.exercises.viewmodel.ExercisesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExerciseLibraryFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private val exercisesViewModel: ExercisesViewModel by viewModel()
    private lateinit var binding: FragmentExerciseLibraryBinding
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
                DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_library, container, false)
        with(binding) {
            lifecycleOwner = this@ExerciseLibraryFragment
            exercisesList.adapter = adapter
            fabAddExercise.setOnClickListener { goToAddExerciseFragment() }
            filterOn = false
            btnSetFilter.setOnClickListener { applyFilter() }
        }

        adapter.setViewModel(exercisesViewModel)
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
            setOnQueryTextListener(this@ExerciseLibraryFragment)
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
        val exerciseOwner = getIndex(binding.exerciseOwner.selectedItem.toString()) == 0

        val category = binding.category.selectedItem.toString()
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
        // nedělá nic - hledám live v onQueryTextChange
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

    override fun onQueryTextChange(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            searchDB(query)
        }
        return true
    }

    private fun goToAddExerciseFragment() {
        val directions =
                ExerciseLibraryFragmentDirections.actionNavigationExerciseLibraryToAddExerciseFragment()
        findNavController().navigate(directions)
    }

}
