package pt.hventura.mycoktails.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.hventura.mycoktails.data.database.dao.CocktailsDao
import pt.hventura.mycoktails.data.models.Categories
import pt.hventura.mycoktails.data.models.Drink
import pt.hventura.mycoktails.data.models.DrinkDetail
import pt.hventura.mycoktails.data.models.Result
import pt.hventura.mycoktails.data.models.Result.Error
import pt.hventura.mycoktails.data.models.Result.Success
import pt.hventura.mycoktails.data.remote.CocktailsDataService

class CocktailsRepositoryImpl(
    cocktailsDao: CocktailsDao,
    private val remote: CocktailsDataService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CocktailsRepository {

    private val database: CocktailsDao = cocktailsDao

    /*************************
     *   List of Categories  *
     *************************/
    override suspend fun getCocktailsCategoryFromDB(): Result<List<Categories>> = withContext(ioDispatcher) {
        val response: List<Categories> = database.getCategories()
        return@withContext if (response.isNotEmpty()) {
            Success(response)
        } else {
            Error("No information on Database")
        }
    }

    override suspend fun getCocktailsCategoryFromAPI(): Result<List<Categories>> = withContext(ioDispatcher) {
        return@withContext try {
            val remoteList = remote.getCategoryList()
            database.deleteCategories()
            database.insertCategories(remoteList.drinks)
            Success(remoteList.drinks)
        } catch (ex: Exception) {
            Error(ex.localizedMessage)
        }
    }

    /*************************
     *   List of Cocktails   *
     *************************/
    override suspend fun getCocktailsListFromDB(): Result<List<Drink>> = withContext(ioDispatcher) {
        val response = database.getDrinksList()
        if (response.isNotEmpty()) {
            return@withContext Success(response)
        } else {
            return@withContext getCocktailsListFromAPI()
        }
    }

    override suspend fun getCocktailsListFromAPI(): Result<List<Drink>> = withContext(ioDispatcher) {
        return@withContext try {
            val remoteList = remote.getFilteredByCategory()
            val localList = database.getDrinksList()
            if (remoteList.drinks != localList || localList.isEmpty()) { // if they are not equal || local is empty -> no need for loop
                for (remoteDrink in remoteList.drinks) {
                    val drinkFound = localList.find { it.idDrink == remoteDrink.idDrink }
                    if (drinkFound != null) {
                        remoteDrink.existsInDB = drinkFound.existsInDB
                        remoteDrink.favourite = drinkFound.favourite
                    }
                }
            }
            database.insertDrinks(remoteList.drinks)
            Success(remoteList.drinks)
        } catch (ex: Exception) {
            Error(ex.localizedMessage)
        }
    }

    /*************************
     *    Cocktail Detail    *
     *************************/
    override suspend fun getCocktailDetailFromDB(drinkId: String): Result<DrinkDetail> = withContext(ioDispatcher) {
        val response = database.getDetailedDrink(drinkId)
        if (response != null) {
            return@withContext Success(response)
        } else {
            return@withContext getCocktailDetailFromAPI(drinkId)
        }
    }

    override suspend fun getCocktailDetailFromAPI(drinkId: String): Result<DrinkDetail> = withContext(ioDispatcher) {
        return@withContext try {
            val remoteDetail = remote.getCocktailDetail(drinkId).drinks.first()
            database.insertDrink(remoteDetail)
            database.setExistsInDB(remoteDetail.idDrink, true)
            Success(remoteDetail)
        } catch (ex: Exception) {
            Error(ex.localizedMessage)
        }
    }

    /*************************
     *  Favourite Cocktails  *
     *************************/
    override suspend fun getFavouriteCocktailsListDB(): Result<List<Drink>> = withContext(ioDispatcher) {
        val response = database.getFavouriteDrinks(true)
        return@withContext if (response.isNotEmpty()) {
            Success(response)
        } else {
            Success(emptyList())
        }
    }

    override suspend fun setDrinkAsFavourite(drinkId: String, value: Boolean): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext try {
            val response = database.setFavouriteDrink(drinkId, value)
            Success(response > 0)
        } catch (ex: Exception) {
            Error(ex.localizedMessage)
        }
    }

}
