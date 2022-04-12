package pt.hventura.mycoktails.authentication

import android.app.Application
import androidx.lifecycle.map
import pt.hventura.mycoktails.authentication.data.UserInfo
import pt.hventura.mycoktails.base.BaseViewModel
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl
import pt.hventura.mycoktails.utils.PreferencesManager

class AuthenticationViewModel(app: Application) : BaseViewModel(app) {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            val userData = UserInfo(
                user.displayName!!,
                user.email!!
            )
            PreferencesManager.put<UserInfo>(userData, "userData")
            AuthenticationState.AUTHENTICATED
        } else {
            PreferencesManager.put(null, "userData")
            AuthenticationState.UNAUTHENTICATED
        }
    }

}