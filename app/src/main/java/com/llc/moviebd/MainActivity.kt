package com.llc.moviebd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.llc.moviebd.databinding.ActivityMainBinding
import com.llc.moviebd.worker.UploadWorker

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigation()

        setUpWorker()
    }

    private fun setUpWorker() {
        val uploadWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<UploadWorker>().build()

        WorkManager
            .getInstance(this)
            .enqueue(uploadWorkRequest)
    }

    private fun setUpBottomNavigation() {
        binding.bottomNavView.setupWithNavController(navController)
        binding.bottomNavView.setOnItemReselectedListener { item ->
            if (binding.bottomNavView.selectedItemId == item.itemId) {
                return@setOnItemReselectedListener
            }
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val hideBottomNav = destination.id != R.id.movieDetailFragment
            binding.bottomNavView.isVisible = hideBottomNav
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}