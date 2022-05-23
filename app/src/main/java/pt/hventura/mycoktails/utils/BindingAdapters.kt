package pt.hventura.mycoktails.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import de.hdodenhof.circleimageview.CircleImageView
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseRecyclerViewAdapter

object BindingAdapters {

    /**
     * Use binding adapter to set the recycler view data using livedata object
     */
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("android:liveData")
    @JvmStatic
    fun <T> setRecyclerViewData(recyclerView: RecyclerView, items: LiveData<List<T>>?) {
        items?.value?.let { itemList ->
            (recyclerView.adapter as? BaseRecyclerViewAdapter<T>)?.apply {
                clear()
                addData(itemList)
            }
        }
    }

    /**
     * Set image from url using coil
     */
    @BindingAdapter("android:urlImage")
    @JvmStatic
    fun setImageFromUrl(view: ImageView, url: String) {
        view.load(url) {
            memoryCachePolicy(CachePolicy.ENABLED)
            placeholder(R.drawable.ic_image_error)
            error(R.drawable.ic_image_error)
        }
    }

    /**
     * Set image accordingly favourite flag using coil
     */
    @BindingAdapter("android:favourite")
    @JvmStatic
    fun setImageFavourite(view: ImageView, isFavourite: Boolean) {
        val drawable = if (isFavourite) {
            R.drawable.ic_favourite
        } else {
            R.drawable.ic_no_favourite
        }
        view.load(drawable) {
            crossfade(true)
            crossfade(500)
        }
    }

    /**
     * Set icon accordingly exists in Database flag using coil
     */
    @BindingAdapter("android:applyIcon")
    @JvmStatic
    fun setIconExistsInDb(view: ImageView, exists: Boolean) {
        val drawable = if (exists) {
            R.drawable.ic_ok
        } else {
            R.drawable.ic_no
        }
        view.load(drawable) {
            crossfade(true)
            crossfade(500)
        }
    }

    /**
     * Use this binding adapter to show and hide the views using boolean variables
     */
    @BindingAdapter("android:fadeVisible")
    @JvmStatic
    fun setFadeVisible(view: View, visible: Boolean? = true) {
        if (view.tag == null) {
            view.tag = true
            view.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {
            view.animate().cancel()
            if (visible == true) {
                if (view.visibility == View.GONE)
                    view.fadeIn()
            } else {
                if (view.visibility == View.VISIBLE)
                    view.fadeOut()
            }
        }
    }
}