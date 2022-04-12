package pt.hventura.mycoktails.authentication.data

import java.io.Serializable

data class UserInfo(
    var userName: String? = null,
    var userEmail: String? = null
) : Serializable
