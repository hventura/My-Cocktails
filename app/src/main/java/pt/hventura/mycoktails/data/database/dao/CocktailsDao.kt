package pt.hventura.mycoktails.data.database.dao

import androidx.room.*
import pt.hventura.mycoktails.data.models.Categories
import pt.hventura.mycoktails.data.models.Drink
import pt.hventura.mycoktails.data.models.DrinkDetail

@Dao
interface CocktailsDao {

    /*************************
     *   List of Categories  *
     *************************/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Categories>)

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<Categories>

    @Query("DELETE FROM categories")
    suspend fun deleteCategories(): Int


    /*************************
     *   List of Cocktails   *
     *************************/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinks(drinks: List<Drink>)

    @Query("SELECT * FROM drinks_list")
    suspend fun getDrinksList(): List<Drink>

    @Query("UPDATE drinks_list SET existsInDB = :value WHERE idDrink = :id")
    suspend fun setExistsInDB(id: String, value: Boolean): Int

    @Query("UPDATE drinks_list SET favourite = :value WHERE idDrink = :id")
    suspend fun setFavouriteDrink(id: String, value: Boolean): Int


    /*************************
     *    Cocktail Detail    *
     *************************/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: DrinkDetail)

    @Query("SELECT * FROM drinks_details")
    suspend fun getDrinksDetailsList(): List<DrinkDetail>

    @Delete
    suspend fun deleteDrink(drink: DrinkDetail)

    @Query("SELECT * FROM drinks_details WHERE idDrink = :id")
    suspend fun getDetailedDrink(id: String): DrinkDetail?


    /*************************
     *  Favourite Cocktails  *
     *************************/
    @Query("SELECT * FROM drinks_list WHERE favourite = :value")
    suspend fun getFavouriteDrinks(value: Boolean = true): List<Drink>
}