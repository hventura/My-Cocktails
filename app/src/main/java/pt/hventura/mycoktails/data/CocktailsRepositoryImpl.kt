package pt.hventura.mycoktails.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.hventura.mycoktails.data.database.dao.CocktailsDao
import pt.hventura.mycoktails.data.models.*
import pt.hventura.mycoktails.data.models.Result.Error
import pt.hventura.mycoktails.data.models.Result.Success
import pt.hventura.mycoktails.data.remote.CocktailsDataService
import timber.log.Timber

class CocktailsRepositoryImpl(
    cocktailsDao: CocktailsDao,
    private val remote: CocktailsDataService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CocktailsRepository {

    private val database: CocktailsDao = cocktailsDao

    override suspend fun getCocktailsCategory(): Result<CategoryList> = withContext(ioDispatcher) {
        val response: List<Categories> = database.getCategories()
        if (response.isNotEmpty()) {
            return@withContext Success(CategoryList(response))
        } else {
            return@withContext try {
                val remoteList = remote.getCategoryList()
                database.insertCategories(remoteList.drinks)
                Success(remoteList)
            } catch (ex: Exception) {
                Error(ex.localizedMessage)
            }
        }
    }

    override suspend fun getCocktailsList(): Result<ListByCategory> = withContext(ioDispatcher) {
        val response = database.getDrinks()
        if (response.isNotEmpty()) {
            Timber.e(response.toString())
            return@withContext Success(ListByCategory(response.toMutableList()))
        } else {
            return@withContext try {
                val remoteList = remote.getFilteredByCategory()
                database.insertDrinks(remoteList.drinks)
                Success(remoteList)
            } catch (ex: Exception) {
                Error(ex.localizedMessage)
            }
        }
    }

    override suspend fun getFavouriteCocktailsList(): Result<List<CompactDrink>> = withContext(ioDispatcher) {
        val response = database.getFavouriteDrinks(true)
        return@withContext if (response.isNotEmpty()) {
            Success(response)
        } else {
            Success(emptyList())
        }
    }

    override suspend fun getCocktailDetail(drinkId: String): Result<Drink> = withContext(ioDispatcher) {
        val currentDrink = database.getDrink(drinkId)
        val response: Drink? = database.getDetailedDrink(drinkId)
        if (response != null) {
            return@withContext Success(response)
        } else {
            return@withContext try {
                val remoteDetail = remote.getCocktailDetail(drinkId).drinks.first()
                database.insertDrink(remoteDetail)
                database.setFavouriteDetail(drinkId, currentDrink.favourite) // In case we set as favourite without get the details
                database.setExistsInDB(remoteDetail.idDrink, true)
                Success(remoteDetail)
            } catch (ex: Exception) {
                Error(ex.localizedMessage)
            }
        }
    }

    override suspend fun getUpdateFavouriteDrink(drinkId: String): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext try {
            val currentDrink = database.getDrink(drinkId)
            val favourite = !currentDrink.favourite
            var compactOK = database.setFavouriteCompact(drinkId, favourite)
            if (compactOK != 0 && currentDrink.existsInDB) {
                compactOK = database.setFavouriteDetail(drinkId, favourite)
            }
            if (compactOK != 0) {
                Success(favourite)
            } else {
                Error("Something went wrong while updating favourites")
            }
        } catch (ex: Exception) {
            Error(ex.localizedMessage)
        }
    }

}
