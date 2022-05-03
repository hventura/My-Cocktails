package pt.hventura.mycoktails.cocktails.listcocktails

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.hventura.mycoktails.base.BaseViewModel
import pt.hventura.mycoktails.base.NavigationCommand
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl
import pt.hventura.mycoktails.data.models.*
import pt.hventura.mycoktails.data.models.Result.Error
import pt.hventura.mycoktails.data.models.Result.Success

class CocktailListViewModel(app: Application, private val repository: CocktailsRepositoryImpl) : BaseViewModel(app) {

    val userName: String = "Helder Ventura"
    val userEmail: String = "hfs.ventura@gmail.com"

    val cocktailsList = MutableLiveData<List<CompactDrink>>()

    fun loadCocktailList() {
        showLoading.value = true
        viewModelScope.launch {
            when (val list = repository.getCocktailsList()) {
                is Success<ListByCategory> -> {
                    val cocktailList = ArrayList<CompactDrink>()
                    cocktailList.addAll(list.data.drinks)
                    cocktailsList.value = cocktailList
                }
                else -> {
                    showSnackBar.value = (list as Error).message
                }
            }
            invalidateShowNoData()
            showLoading.value = false

        }
    }

    fun loadCocktailDetail(drinkId: String) {
        showLoading.value = true
        viewModelScope.launch {
            when (val detail = repository.getCocktailDetail(drinkId)) {
                is Success<Drink> -> {
                    navigationCommand.value = NavigationCommand.To(
                        CocktailListFragmentDirections.actionCocktailListFragmentToDetailsCocktailFragment(detail.data.toDetail())
                    )
                }
                else -> {
                    showSnackBar.value = (detail as Error).message
                }
            }
            showLoading.value = false
        }
    }

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = cocktailsList.value == null || cocktailsList.value!!.isEmpty()
    }
}