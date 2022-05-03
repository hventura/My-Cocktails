package pt.hventura.mycoktails.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.hventura.mycoktails.data.database.dao.CocktailsDao
import pt.hventura.mycoktails.data.models.*
import pt.hventura.mycoktails.data.models.Result.Error
import pt.hventura.mycoktails.data.models.Result.Success
import pt.hventura.mycoktails.data.remote.CocktailsDataService

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
        val response: List<CompactDrink> = database.getDrinks()
        if (response.isNotEmpty()) {
            return@withContext Success(ListByCategory(response))
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

    override suspend fun getCocktailDetail(drinkId: String): Result<Drink> = withContext(ioDispatcher) {
        val response: Drink? = database.getDetailedDrink(drinkId)
        if (response != null) {
            return@withContext Success(response)
        } else {
            return@withContext try {
                val remoteDetail = remote.getCocktailDetail(drinkId).drinks.first()
                database.insertDrink(remoteDetail)
                database.setExistsInDB(remoteDetail.idDrink)
                Success(remoteDetail)
            } catch (ex: Exception) {
                Error(ex.localizedMessage)
            }
        }
    }

}
