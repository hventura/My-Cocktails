package pt.hventura.mycoktails.cocktails.randomcocktail

import android.app.Application
import pt.hventura.mycoktails.base.BaseViewModel
import pt.hventura.mycoktails.data.CocktailsRepositoryImpl

class RandomCocktailViewModel(app: Application, private val repository: CocktailsRepositoryImpl) : BaseViewModel(app) {

}