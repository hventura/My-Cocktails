package pt.hventura.mycoktails.utils

import com.google.firebase.auth.FirebaseAuth
import pt.hventura.mycoktails.authentication.data.UserInfo

object LoginControl {

    fun isLoggedIn(): Boolean {
        val userData = PreferencesManager.retrieve<UserInfo>("userData")
        if (userData != null) {
            return true
        }
        return false
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        PreferencesManager.put(null, "userData")
    }

}