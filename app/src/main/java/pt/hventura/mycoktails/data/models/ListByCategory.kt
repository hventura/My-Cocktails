package pt.hventura.mycoktails.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ListByCategory(
    val drinks: MutableList<CompactDrink>
)

@Entity(tableName = "drinks_list")
data class CompactDrink(
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