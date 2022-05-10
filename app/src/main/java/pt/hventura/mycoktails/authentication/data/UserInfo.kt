package pt.hventura.mycoktails.authentication.data

import java.io.Serializable

data class UserInfo(
    var userName: String,
    var userEmail: String
) : Serializable
