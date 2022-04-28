package pt.hventura.mycoktails.data

import pt.hventura.mycoktails.data.models.CategoryList
import pt.hventura.mycoktails.data.models.ListByCategory
import pt.hventura.mycoktails.data.models.Result

interface CocktailsRepository {
    suspend fun getCocktailsCategory(): Result<CategoryList>
    suspend fun getCocktailsList(): Result<ListByCategory>
}