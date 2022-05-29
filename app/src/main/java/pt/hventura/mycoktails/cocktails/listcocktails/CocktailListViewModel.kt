package pt.hventura.mycoktails.cocktails.listcocktails

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.hventura.mycoktails.base.BaseViewModel
import pt.hventura.mycoktails.base.NavigationCommand
import pt.hventura.mycoktails.cocktails.favoritecocktails.FavouritesCocktailFragmentDirections
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl
import pt.hventura.mycoktails.data.models.Drink
import pt.hventura.mycoktails.data.models.Result.Error
import pt.hventura.mycoktails.data.models.Result.Success
import pt.hventura.mycoktails.data.models.toDetail
import pt.hventura.mycoktails.utils.SingleLiveEvent
import pt.hventura.mycoktails.utils.isConnected
import timber.log.Timber

class CocktailListViewModel(private val app: Application, private val repository: CocktailsRepositoryImpl) : BaseViewModel(app) {

    var userName: String = "Helder Ventura"
    var userEmail: String = "hfs.ventura@gmail.com"

    val cocktailsList = MutableLiveData<List<Drink>>()
    val favouriteCocktailsList = MutableLiveData<List<Drink>>()
    val openWifiDefinitions = SingleLiveEvent<Boolean>()

    // List of Cocktails
    fun loadCocktailList() {
        showLoading.value = true
        viewModelScope.launch {
            if (app.isConnected()) {
                when (val listAPI = repository.getCocktailsListFromAPI()) {
                    is Success -> {
                        showLoading.value = false
                        cocktailsList.value = listAPI.data
                        invalidateShowNoData()
                    }
                    else -> {
                        showLoading.value = false
                        cocktailsList.value = emptyList()
                        showErrorMessage.postValue("Something went wrong getting the data from Cocktail API. Please try again latter")
                        Timber.e((listAPI as Error).message)
                        invalidateShowNoData()
                    }
                }
            } else {
                when (val listDB = repository.getCocktailsListFromDB()) {
                    is Success -> {
                        if (listDB.data.isNotEmpty()) {
                            showLoading.value = false
                            cocktailsList.value = listDB.data
                            invalidateShowNoData()
                        } else {
                            showLoading.value = false
                            openWifiDefinitions.value = true
                            invalidateShowNoData()
                        }
                    }
                    else -> {
                        showLoading.value = false
                        openWifiDefinitions.value = true
                        invalidateShowNoData()
                    }
                }
            }
        }
    }

    // Cocktail Detail
    fun loadCocktailDetail(drinkId: String, fromFav: Boolean) {
        showLoading.value = true
        viewModelScope.launch {
            if (app.isConnected()) {
                when (val drinkDetailAPI = repository.getCocktailDetailFromAPI(drinkId)) {
                    is Success -> {
                        if (fromFav) {
                            navigationCommand.value = NavigationCommand.To(
                                FavouritesCocktailFragmentDirections
                                    .actionFavouritesCocktailFragmentToDetailsCocktailFragment(drinkDetailAPI.data.toDetail())
                            )
                        } else {
                            navigationCommand.value = NavigationCommand.To(
                                CocktailListFragmentDirections
                                    .actionCocktailListFragmentToDetailsCocktailFragment(drinkDetailAPI.data.toDetail())
                            )

                        }
                    }
                    else -> {
                        showLoading.value = false
                        showErrorMessage.postValue("Something went wrong getting the data from Cocktail API. Please try again latter")
                        Timber.e((drinkDetailAPI as Error).message)
                    }
                }
            } else {
                when (val drinkDetailDB = repository.getCocktailDetailFromDB(drinkId)) {
                    is Success -> {
                        showLoading.value = false
                        if (fromFav) {
                            navigationCommand.value = NavigationCommand.To(
                                FavouritesCocktailFragmentDirections
                                    .actionFavouritesCocktailFragmentToDetailsCocktailFragment(drinkDetailDB.data.toDetail())

                            )
                        } else {
                            navigationCommand.value = NavigationCommand.To(
                                CocktailListFragmentDirections
                                    .actionCocktailListFragmentToDetailsCocktailFragment(drinkDetailDB.data.toDetail())
                            )
                        }
                    }
                    else -> {
                        showLoading.value = false
                        openWifiDefinitions.value = true
                        Timber.e((drinkDetailDB as Error).message)
                    }
                }

            }
        }
    }

    // Favourite Cocktails
    fun loadCocktailFavouritesList() {
        showLoading.value = true
        viewModelScope.launch {
            when (val favouriteList = repository.getFavouriteCocktailsListDB()) {
                is Success -> {
                    showLoading.value = false
                    favouriteCocktailsList.value = favouriteList.data
                    invalidateShowNoDataFavourites()
                }
                else -> {
                    showLoading.value = false
                    favouriteCocktailsList.value = emptyList()
                    showErrorMessage.postValue("No items in favourite. Please add cocktails to your favorite list!")
                    invalidateShowNoDataFavourites()
                }
            }
        }
    }

    private val _favouriteSaved = MutableLiveData<Boolean>(false)
    val favouriteSaved: LiveData<Boolean> get() = _favouriteSaved
    fun setDrinkAsFavourite(drinkId: String, value: Boolean) {
        viewModelScope.launch {
            when (val result = repository.setDrinkAsFavourite(drinkId, value)) {
                is Success -> {
                    _favouriteSaved.value = result.data
                }
                else -> showErrorMessage.value = "Something when wrong with internal database. Please restart application and try again."
            }
        }
    }

    /**
     * Inform the user that there's not any data if the cocktailsList is empty!
     */
    private fun invalidateShowNoData() {
        showNoData.value = cocktailsList.value == null || cocktailsList.value!!.isEmpty()
    }

    private fun invalidateShowNoDataFavourites() {
        showNoDataFavourites.value = favouriteCocktailsList.value == null || favouriteCocktailsList.value!!.isEmpty()
    }
}