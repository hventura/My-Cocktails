package pt.hventura.mycoktails.cocktails.coctailsMap

import android.app.Application
import androidx.lifecycle.MutableLiveData
import org.joda.time.DateTime
import pt.hventura.mycoktails.base.BaseViewModel
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl

/**
 * I choose to create this viewModel so that in the future i can implement a new feature:
 * i am thinking in creating GeoFencing.
 * On user entering geofence i trigger a notification saying user is near a good place for a cocktail.
 * */
class CocktailMapViewModel(private val app: Application, private val repository: CocktailsRepositoryImpl) : BaseViewModel(app) {

    var hourOfDay = MutableLiveData(DateTime().hourOfDay)

}