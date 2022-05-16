package pt.hventura.mycoktails.data

import pt.hventura.mycoktails.data.models.*

interface CocktailsRepository {
    suspend fun getCocktailsCategory(): Result<CategoryList>
    suspend fun getCocktailsList(): Result<ListByCategory>
    suspend fun getFavouriteCocktailsList(): Result<List<CompactDrink>>
    suspend fun getCocktailDetail(drinkId: String): Result<Drink>
    suspend fun getUpdateFavouriteDrink(drinkId: String): Result<Boolean>
}