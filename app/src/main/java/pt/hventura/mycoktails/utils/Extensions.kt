package pt.hventura.mycoktails.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.hventura.mycoktails.base.BaseRecyclerViewAdapter
import timber.log.Timber
import kotlin.reflect.full.declaredMemberProperties

inline fun <reified T : Activity> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivity() {
    val intent = Intent(requireContext(), T::class.java)
    requireActivity().startActivity(intent)
}

fun Context.isConnected(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Timber.i("NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Timber.i("NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Timber.i("NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun Fragment.finish() {
    requireActivity().finish()
}

fun <T> RecyclerView.setup(
    adapter: BaseRecyclerViewAdapter<T>
) {
    this.apply {
        layoutManager = LinearLayoutManager(this.context)
        this.adapter = adapter
    }
}

/**
 * Thanks to Jayson Minard: https://stackoverflow.com/users/3679676/jayson-minard
 * https://stackoverflow.com/questions/35525122/kotlin-data-class-how-to-read-the-value-of-property-if-i-dont-know-its-name-at
 * And David Sanford: https://gist.github.com/DavidSanf0rd
 * https://gist.github.com/DavidSanf0rd/9725485155bc0c4c681eb038b21c457a
 * */
@Suppress("UNCHECKED_CAST")
fun <T> readProperties(instance: Any, propertyName: String): T? {
    var property: Any? = null
    val mClass = instance.javaClass.kotlin
    mClass.declaredMemberProperties.forEach {
        if (it.name == propertyName)
            property = it.getter.call(instance)
    }
    return property as T?
}

fun Fragment.setDisplayHomeAsUpEnabled(bool: Boolean) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(bool)
    }
}

//animate changing the view visibility
fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

//animate changing the view visibility
fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
}