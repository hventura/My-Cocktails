package pt.hventura.mycoktails.data

import pt.hventura.mycoktails.data.models.ListByCategory
import pt.hventura.mycoktails.data.models.Result

interface CocktailsRepository {
    suspend fun getCocktailsList(): Result<ListByCategory>
    suspend fun getCocktailsListDB(): Result<ListByCategory>
}