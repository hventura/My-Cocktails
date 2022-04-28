package pt.hventura.mycoktails.cocktails.listcocktails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.hventura.mycoktails.base.BaseViewModel
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl
import pt.hventura.mycoktails.data.models.CompactDrink
import pt.hventura.mycoktails.data.models.ListByCategory
import pt.hventura.mycoktails.data.models.Result.Error
import pt.hventura.mycoktails.data.models.Result.Success
import timber.log.Timber

class CocktailListViewModel(app: Application, private val repository: CocktailsRepositoryImpl) : BaseViewModel(app) {

    val userName: String = "Helder Ventura"
    val userEmail: String = "hfs.ventura@gmail.com"

    val cocktailsList = MutableLiveData<List<CompactDrink>>()

    fun loadCocktails() {
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

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = cocktailsList.value == null || cocktailsList.value!!.isEmpty()
    }

    init {

    }
}