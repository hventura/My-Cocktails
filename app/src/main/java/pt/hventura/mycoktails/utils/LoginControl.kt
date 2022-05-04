package pt.hventura.mycoktails.utils

import com.google.firebase.auth.FirebaseAuth
import pt.hventura.mycoktails.authentication.data.UserInfo

object LoginControl {

    fun isLoggedIn(): Boolean {
        val userData: UserInfo = PreferencesManager.retrieve<UserInfo>("userData") ?: UserInfo()
        if (userData.userName != null) {
            return true
        }
        return false
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        PreferencesManager.put(null, "use rData")
    }

}