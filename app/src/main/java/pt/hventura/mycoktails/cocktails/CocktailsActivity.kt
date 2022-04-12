package pt.hventura.mycoktails.cocktails

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.authentication.AuthenticationActivity
import pt.hventura.mycoktails.base.DrawerController
import pt.hventura.mycoktails.databinding.ActivityCocktailsBinding
import pt.hventura.mycoktails.utils.LoginControl
import pt.hventura.mycoktails.utils.startActivity
import timber.log.Timber

class CocktailsActivity : AppCompatActivity(), DrawerController {

    private lateinit var binding: ActivityCocktailsBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCocktailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!LoginControl.isLoggedIn()) {
            startActivity<AuthenticationActivity>()
            finish()
        }

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerLayout = binding.drawerLayout
        navView = binding.navigationView

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Timber.e(destination.displayName)
            when (destination.id) {
                R.id.cocktailListFragment -> {
                    binding.tvTitle.text = resources.getString(R.string.list_cocktails)
                }
                R.id.randomCocktailFragment -> {
                    binding.tvTitle.text = resources.getText(R.string.random_cocktail)
                }
                R.id.detailsCocktailFragment -> {
                    binding.tvTitle.text = resources.getString(R.string.details_cocktail)
                }
            }
        }

    }

    override fun setDrawerLocked() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.navigationIcon = null
    }

    override fun setDrawerUnLocked() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

}

