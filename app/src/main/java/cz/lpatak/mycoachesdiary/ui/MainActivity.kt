package cz.lpatak.mycoachesdiary.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.databinding.ActivityMainBinding
import cz.lpatak.mycoachesdiary.ui.auth.LoginFragment
import cz.lpatak.mycoachesdiary.ui.auth.RegisterFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home,
                R.id.navigation_trainings,
                R.id.navigation_matches,
                R.id.navigation_stats,
                R.id.navigation_exercise_library,
                R.id.navigation_login,
                R.id.navigation_register
        ))

        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
        setupWithNavController(binding.bottomNavView, navHostFragment.navController)

        setupBottomNavigation(navHostFragment.navController)

        setContentView(view)
    }

    private fun setupBottomNavigation(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // TOP LEVEL
                R.id.navigation_home -> {
                    showBottomNavigation()
                }
                R.id.navigation_trainings -> {
                    showBottomNavigation()
                }
                R.id.navigation_matches -> {
                    showBottomNavigation()
                }
                R.id.navigation_stats -> {
                    showBottomNavigation()
                }
                R.id.navigation_exercise_library -> {
                    showBottomNavigation()
                }
                else -> {
                    hideBottomNavigation()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (getCurrentFragment() !is LoginFragment && (getCurrentFragment() !is RegisterFragment)) {
            onBackPressed()
        }
        return true
    }

    private fun hideBottomNavigation() {
        binding.bottomNavView.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        binding.bottomNavView.visibility = View.VISIBLE
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.get(
                0
        )
    }
}