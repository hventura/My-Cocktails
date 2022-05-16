package pt.hventura.mycoktails.cocktails.listcocktails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.hventura.mycoktails.base.BaseViewModel
import pt.hventura.mycoktails.base.NavigationCommand
import pt.hventura.mycoktails.cocktails.favoritecocktails.FavouritesCocktailFragmentDirections
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl
import pt.hventura.mycoktails.data.models.CompactDrink
import pt.hventura.mycoktails.data.models.Drink
import pt.hventura.mycoktails.data.models.ListByCategory
import pt.hventura.mycoktails.data.models.Result.Error
import pt.hventura.mycoktails.data.models.Result.Success
import pt.hventura.mycoktails.data.models.toDetail

class CocktailListViewModel(app: Application, private val repository: CocktailsRepositoryImpl) : BaseViewModel(app) {

    var userName: String = "Helder Ventura"
    var userEmail: String = "hfs.ventura@gmail.com"

    val cocktailsList = MutableLiveData<MutableList<CompactDrink>>()
    val favouriteCocktailsList = MutableLiveData<MutableList<CompactDrink>>()
    val showNoFavouritesData = MutableLiveData<Boolean>()

    fun loadCocktailList() {
        viewModelScope.launch {
            when (val list = repository.getCocktailsList()) {
                is Success<ListByCategory> -> {
                    cocktailsList.value = list.data.drinks
                }
                else -> {
                    showSnackBar.value = (list as Error).message
                }
            }
            invalidateShowNoData()
        }
    }

    fun loadCocktailDetail(drinkId: String, fromFav: Boolean) {
        viewModelScope.launch {
            when (val detail = repository.getCocktailDetail(drinkId)) {
                is Success<Drink> -> {
                    showLoading.postValue(false)
                    if (fromFav) {
                        navigationCommand.value = NavigationCommand.To(
                            FavouritesCocktailFragmentDirections.actionFavouritesCocktailFragmentToDetailsCocktailFragment(detail.data.toDetail())
                        )
                    } else {
                        navigationCommand.value = NavigationCommand.To(
                            CocktailListFragmentDirections.actionCocktailListFragmentToDetailsCocktailFragment(detail.data.toDetail())
                        )
                    }
                }
                else -> {
                    showSnackBar.value = (detail as Error).message
                }
            }
        }
    }

    fun setDrinkAsFavourite(drinkId: String) {
        viewModelScope.launch {
            when (val update = repository.getUpdateFavouriteDrink(drinkId)) {
                is Success<Boolean> -> {
                    for (drink in cocktailsList.value!!) {
                        if (drink.idDrink == drinkId) {
                            drink.favourite = update.data
                        }
                    }
                }
                else -> {
                    showErrorMessage.value = (update as Error).message
                }
            }
        }
    }

    fun loadFavouriteDrinks() {
        viewModelScope.launch {
            when (val favourites = repository.getFavouriteCocktailsList()) {
                is Success<List<CompactDrink>> -> {
                    favouriteCocktailsList.value = favourites.data.toMutableList()
                    showNoFavouritesData.value = false
                }
                else -> {
                    favouriteCocktailsList.value = mutableListOf()
                    showNoFavouritesData.value = true
                }
            }
        }
    }

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = cocktailsList.value == null || cocktailsList.value!!.isEmpty()
    }
}