package pt.hventura.mycoktails.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.hventura.mycoktails.BuildConfig
import pt.hventura.mycoktails.data.database.dao.CocktailsDao
import pt.hventura.mycoktails.data.models.Categories
import pt.hventura.mycoktails.data.models.Drink
import pt.hventura.mycoktails.data.models.DrinkDetail

@Database(entities = [Categories::class, Drink::class, DrinkDetail::class], version = 1, exportSchema = false)
abstract class CocktailsDatabase : RoomDatabase() {

    abstract val cocktailsDao: CocktailsDao

    companion object {
        @Volatile
        private var INSTANCE: CocktailsDatabase? = null

        fun getInstance(context: Context): CocktailsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CocktailsDatabase::class.java,
                        BuildConfig.DATABASE
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}