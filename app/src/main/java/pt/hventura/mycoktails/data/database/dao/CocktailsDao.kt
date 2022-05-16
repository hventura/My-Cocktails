package pt.hventura.mycoktails.data.database.dao

import androidx.room.*
import pt.hventura.mycoktails.data.models.Categories
import pt.hventura.mycoktails.data.models.CompactDrink
import pt.hventura.mycoktails.data.models.Drink

@Dao
interface CocktailsDao {

    // CATEGORIES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Categories>)

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<Categories>

    // DRINKS LIST
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinks(drinks: List<CompactDrink>)

    @Query("SELECT * FROM drinks_list")
    suspend fun getDrinks(): List<CompactDrink>

    @Query("SELECT * FROM drinks_list WHERE idDrink = :id")
    suspend fun getDrink(id: String): CompactDrink

    @Query("UPDATE drinks_list SET existsInDB = :value WHERE idDrink = :id")
    suspend fun setExistsInDB(id: String, value: Boolean): Int

    @Query("UPDATE drinks_list SET favourite = :value WHERE idDrink = :id")
    suspend fun setFavouriteCompact(id: String, value: Boolean): Int

    @Query("SELECT * FROM drinks_list WHERE favourite = :value")
    suspend fun getFavouriteDrinks(value: Boolean = true): List<CompactDrink>

    // DRINKS DETAILS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: Drink)

    @Delete
    suspend fun deleteDrink(drink: Drink)

    @Query("UPDATE detailed_drinks SET isFavourite = :value WHERE idDrink = :id")
    suspend fun setFavouriteDetail(id: String, value: Boolean): Int

    @Query("SELECT * FROM detailed_drinks WHERE idDrink = :id")
    suspend fun getDetailedDrink(id: String): Drink?

}