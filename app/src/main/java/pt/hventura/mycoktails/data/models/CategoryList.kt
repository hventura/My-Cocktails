package pt.hventura.mycoktails.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CategoryList(
    val drinks: List<Categories>
)

@Entity(tableName = "categories")
data class Categories(
    val strCategory: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
