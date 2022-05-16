package pt.hventura.mycoktails.data.remote

import pt.hventura.mycoktails.data.models.CategoryList
import pt.hventura.mycoktails.data.models.DrinkList
import pt.hventura.mycoktails.data.models.ListByCategory
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsDataService {

    @GET("list.php")
    suspend fun getCategoryList(
        @Query("c") category: String = "list"
    ): CategoryList

    @GET("filter.php")
    suspend fun getFilteredByCategory(
        @Query("c") category: String? = "Cocktail"
    ): ListByCategory

    @GET("lookup.php")
    suspend fun getCocktailDetail(
        @Query("i") drinkId: String
    ): DrinkList

}