package org.indianmusicacademy.packages.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import org.indianmusicacademy.packages.R

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var tvHeaderTitle: TextView
    lateinit var tvHeaderSubTitle: TextView
    lateinit var drawerHeader: View
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        drawerHeader = navView.getHeaderView(0)
        tvHeaderTitle = drawerHeader.findViewById(R.id.tvHeaderTitle)
        tvHeaderSubTitle = drawerHeader.findViewById(R.id.tvHeaderSubtitle)

        val bottomView: BottomNavigationView = findViewById(R.id.bottomView)

        auth = FirebaseAuth.getInstance()

        tvHeaderTitle.text = auth.currentUser?.displayName
        tvHeaderSubTitle.text = auth.currentUser?.email

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menuStudyMaterial,
                R.id.menuAttendance,
                R.id.menuSettings,
                R.id.menuHome,
                R.id.menuBatches,
                R.id.menuMyProfile
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}