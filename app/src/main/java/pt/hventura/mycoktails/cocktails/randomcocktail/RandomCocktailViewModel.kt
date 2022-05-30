package pt.hventura.mycoktails.cocktails.randomcocktail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.hventura.mycoktails.base.BaseViewModel
import pt.hventura.mycoktails.base.NavigationCommand
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl
import pt.hventura.mycoktails.data.models.Result.Success
import pt.hventura.mycoktails.data.models.toDetail
import pt.hventura.mycoktails.utils.SingleLiveEvent

class RandomCocktailViewModel(app: Application, private val repository: CocktailsRepositoryImpl) : BaseViewModel(app) {

    val openWifiDefinitions = SingleLiveEvent<Boolean>()

    fun loadRandomCocktail() = viewModelScope.launch {
        when (val cocktail = repository.getCocktailsListFromDB()) {
            is Success -> {
                val random = cocktail.data[(1..cocktail.data.size).random() - 1]
                when (val drinkDetail = repository.getCocktailDetailFromDB(random.idDrink)) {
                    is Success -> {
                        navigationCommand.value = NavigationCommand.To(
                            RandomCocktailFragmentDirections.actionRandomCocktailFragmentToDetailsCocktailFragment(drinkDetail.data.toDetail())
                        )
                    }
                    else -> {
                        openWifiDefinitions.value = true
                    }
                }
            }
            else -> {
                showErrorMessage.value = "Something when wrong with internal database. Please restart application and try again."
            }
        }
    }
}