package pt.hventura.mycoktails.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.authentication.data.UserInfo
import pt.hventura.mycoktails.cocktails.CocktailsActivity
import pt.hventura.mycoktails.databinding.ActivityAuthenticationBinding
import pt.hventura.mycoktails.utils.PreferencesManager
import timber.log.Timber

class AuthenticationActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthenticationBinding
    val viewModel: AuthenticationViewModel by viewModel()
    var userInfo: UserInfo? = null

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Login ok", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)

        userInfo = PreferencesManager.retrieve<UserInfo?>("userData")
        if (userInfo != null) {
            Timber.e(userInfo.toString())
            startActivity(Intent(this, CocktailsActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        } else {
            initializeObservables()
        }

        binding.login.setOnClickListener {
            startLoginFlow()
        }
    }

    private fun initializeObservables() {
        viewModel.authenticationState.observe(this) { authenticationState ->
            when (authenticationState) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.login.visibility = View.GONE
                    binding.loggedInUser.text = resources.getString(
                        R.string.logged_in_user,
                        FirebaseAuth.getInstance().currentUser?.displayName
                    )
                    binding.loggedInUser.visibility = View.VISIBLE
                    Handler().postDelayed({
                        startActivity(Intent(this, CocktailsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish() // Finish current AuthenticationActivity
                    }, 3000)
                }
                else -> {
                    binding.login.visibility = View.VISIBLE
                    binding.loggedInUser.visibility = View.GONE
                }
            }
        }
    }

    private fun startLoginFlow() {
        val customLayout = AuthMethodPickerLayout.Builder(R.layout.activity_authentication_custom_login)
            .setEmailButtonId(R.id.login_with_email)
            .setGoogleButtonId(R.id.login_with_google)
            .build()

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val authUiIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setAuthMethodPickerLayout(customLayout)
            .setTheme(R.style.Theme_MyCocktails)
            .build()

        startForResult.launch(authUiIntent)

    }
}