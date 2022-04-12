package pt.hventura.mycoktails.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ListByCategory(
    val drinks: List<CompactDrink>
)

@Entity(tableName = "drinks_list")
data class CompactDrink(
    @PrimaryKey
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val existsInDB: Boolean = false
)