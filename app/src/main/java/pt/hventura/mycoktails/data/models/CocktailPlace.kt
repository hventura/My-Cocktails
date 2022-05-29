package pt.hventura.mycoktails.data.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class CocktailPlace(
    val name: String,
    val coords: LatLng
) : ClusterItem {
    override fun getPosition(): LatLng {
        return coords
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String? {
        return null
    }
}
