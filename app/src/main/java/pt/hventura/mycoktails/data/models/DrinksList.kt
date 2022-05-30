package pt.hventura.mycoktails.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class DrinksList(
    val drinks: List<Drink>
)

@Entity(tableName = "drinks_list")
data class Drink(
    @PrimaryKey
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    var existsInDB: Boolean = false,
    var favourite: Boolean = false
) {
    fun getImgPreview(): String {
        return "$strDrinkThumb/preview"
    }
}