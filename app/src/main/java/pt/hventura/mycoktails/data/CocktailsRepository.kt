package pt.hventura.mycoktails.data

import pt.hventura.mycoktails.data.models.*

interface CocktailsRepository {
    // List of Categories
    suspend fun getCocktailsCategoryFromDB(): Result<List<Categories>>
    suspend fun getCocktailsCategoryFromAPI(): Result<List<Categories>>

    // List of Cocktails
    suspend fun getCocktailsListFromDB(): Result<List<Drink>>
    suspend fun getCocktailsListFromAPI(): Result<List<Drink>>

    // Cocktail Detail
    suspend fun getCocktailDetailFromDB(drinkId: String): Result<DrinkDetail>
    suspend fun getCocktailDetailFromAPI(drinkId: String): Result<DrinkDetail>

    // Favourite Cocktails
    suspend fun getFavouriteCocktailsListDB(): Result<List<Drink>>
    suspend fun setDrinkAsFavourite(drinkId: String, value: Boolean): Result<Boolean>

}