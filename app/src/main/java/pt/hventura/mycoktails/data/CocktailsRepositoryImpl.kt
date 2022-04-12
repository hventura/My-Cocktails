package pt.hventura.mycoktails.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.hventura.mycoktails.data.database.dao.CocktailsDao
import pt.hventura.mycoktails.data.models.CompactDrink
import pt.hventura.mycoktails.data.models.ListByCategory
import pt.hventura.mycoktails.data.models.Result
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

    override suspend fun getCocktailsList(): Result<ListByCategory> = withContext(ioDispatcher) {
        return@withContext try {
            Success(remote.getFilteredByCategory())
        } catch (ex: Exception) {
            Error(ex.localizedMessage)
        }
    }

    override suspend fun getCocktailsListDB(): Result<ListByCategory> = withContext(ioDispatcher) {
        return@withContext try {
            val response: List<CompactDrink> = database.getDrinks()
            Timber.e(response.toString())
            if (response.isNotEmpty()) {
                Success(ListByCategory(response))
            } else {
                Error("No items in DB")
            }
        } catch (ex: Exception) {
            Error(ex.localizedMessage)
        }

    }

}
