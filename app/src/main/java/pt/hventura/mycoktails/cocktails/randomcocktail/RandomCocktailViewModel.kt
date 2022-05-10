package pt.hventura.mycoktails.cocktails.randomcocktail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.hventura.mycoktails.base.BaseViewModel
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl
import pt.hventura.mycoktails.data.models.Result
import timber.log.Timber

class RandomCocktailViewModel(app: Application, private val repository: CocktailsRepositoryImpl) : BaseViewModel(app) {

    private val _shakeMax = MutableLiveData<Int>(0)
    val shakeMax: LiveData<Int> get() = _shakeMax

    fun updateMaxShake(shake: Float) {
        _shakeMax.value = shake.toInt()
    }


    fun loadRandomCocktail() = viewModelScope.launch {
        val cocktail = repository.getCocktailsCategory()
        _shakeMax.value = 0
        if (cocktail is Result.Success) {
            val random = (1..cocktail.data.drinks.size).random()
            Timber.i(cocktail.data.drinks[random - 1].toString())
        }
    }
}