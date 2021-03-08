package cz.lpatak.mycoachesdiary.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import cz.lpatak.mycoachesdiary.R
import cz.lpatak.mycoachesdiary.ui.auth.LoginFragment
import cz.lpatak.mycoachesdiary.ui.auth.RegisterFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupBottomNavigation()
    }

    private fun setupViews() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(bottomNavView, navController)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_trainings,
                R.id.navigation_matches,
                R.id.navigation_stats,
                R.id.navigation_exercise_library
            )
        )
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if ((getCurrentFragment() is LoginFragment) || (getCurrentFragment() is RegisterFragment)) {
            finish()
        }
        super.onBackPressed()
    }

    private fun hideBottomNavigation() {
        bottomNavView.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        bottomNavView.visibility = View.VISIBLE
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.get(
            0
        )
    }

    private fun setupBottomNavigation() {
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

}
