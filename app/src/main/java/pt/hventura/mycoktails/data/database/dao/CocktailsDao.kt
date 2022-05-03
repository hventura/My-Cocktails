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

    // DRINKS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinks(drinks: List<CompactDrink>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: Drink)

    @Delete
    suspend fun deleteDrink(drink: Drink)

    @Update
    suspend fun updateDrink(drink: Drink)

    @Query("SELECT * FROM drinks_list")
    suspend fun getDrinks(): List<CompactDrink>

    @Query("SELECT * FROM detailed_drinks WHERE idDrink = :id")
    suspend fun getDetailedDrink(id: String): Drink?

    @Query("UPDATE drinks_list SET existsInDB = 'true' WHERE idDrink = :id")
    suspend fun setExistsInDB(id: String): Int



}