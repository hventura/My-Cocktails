package pt.hventura.mycoktails.cocktails

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.authentication.AuthenticationActivity
import pt.hventura.mycoktails.authentication.data.UserInfo
import pt.hventura.mycoktails.base.DrawerController
import pt.hventura.mycoktails.cocktails.listcocktails.CocktailListViewModel
import pt.hventura.mycoktails.databinding.ActivityCocktailsBinding
import pt.hventura.mycoktails.utils.LoginControl
import pt.hventura.mycoktails.utils.PreferencesManager
import pt.hventura.mycoktails.utils.startActivity
import timber.log.Timber

class CocktailsActivity : AppCompatActivity(), DrawerController {

    private lateinit var binding: ActivityCocktailsBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    val viewModel: CocktailListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCocktailsBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

        if (!LoginControl.isLoggedIn()) {
            startActivity<AuthenticationActivity>()
            finish()
        } else {
            val userInfo = PreferencesManager.retrieve<UserInfo>("userData") ?: UserInfo("John Doe", "ask@me.first")
            viewModel.userName = userInfo.userName
            viewModel.userEmail = userInfo.userEmail
        }

        // TOOLBAR
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // DRAWER
        drawerLayout = binding.drawerLayout
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.setToolbarNavigationClickListener {
            if (toggle.isDrawerIndicatorEnabled) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                onBackPressed()
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // NAVIGATION
        navView = binding.navigationView
        navView.menu.findItem(R.id.btnLogout).setOnMenuItemClickListener {
            LoginControl.logout()
            startActivity<AuthenticationActivity>()
            finish()
            false
        }
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Timber.e(destination.displayName)
            when (destination.id) {
                R.id.cocktailListFragment -> {
                    binding.tvTitle.text = resources.getString(R.string.list_cocktails)
                    showBackButton(false)
                }
                R.id.randomCocktailFragment -> {
                    binding.tvTitle.text = resources.getText(R.string.random_cocktail)
                    showBackButton(true)
                }
                R.id.favouritesCocktailFragment -> {
                    binding.tvTitle.text = resources.getText(R.string.favourites_cocktail)
                    showBackButton(true)
                }
                R.id.detailsCocktailFragment -> {
                    binding.tvTitle.text = resources.getString(R.string.details_cocktail)
                    showBackButton(true)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnLogout -> {
                LoginControl.logout()
                startActivity<AuthenticationActivity>()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showBackButton(hasBack: Boolean) {
        toggle.isDrawerIndicatorEnabled = !hasBack
        supportActionBar?.setDisplayHomeAsUpEnabled(hasBack)
        toggle.syncState()
    }

}

