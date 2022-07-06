package com.bajra.assessment

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bajra.assessment.databinding.ActivityMainBinding
import com.bajra.assessment.ui.home.HomeFragment
import com.bajra.assessment.utils.hide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> hideToolbar()
                R.id.registerFragment -> hideToolbar()
                else -> {
                    showToolbar()
                }
            }
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            lifecycleScope.launchWhenResumed {
                navController.navigate(R.id.action_nav_home_to_loginFragment)
            }
        }

        val header: View = navView.getHeaderView(0)
        val buttonLogout: Button = header.findViewById(R.id.logout) as Button

        buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            navController.navigate(R.id.action_nav_home_to_loginFragment)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun hideToolbar() {
        (this as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    private fun showToolbar() {
        (this as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}