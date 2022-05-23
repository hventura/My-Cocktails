package pt.hventura.mycoktails.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import pt.hventura.mycoktails.utils.readProperties

data class DrinksDetailList(
    val drinks: List<DrinkDetail>
)

@Entity(tableName = "drinks_details")
@JsonClass(generateAdapter = true)
data class DrinkDetail(
    @PrimaryKey
    val idDrink: String,
    val strDrink: String,
    val strDrinkAlternate: String?,
    val strTags: String?,
    val strVideo: String?,
    val strCategory: String?,
    val strIBA: String?,
    val strAlcoholic: String?,
    val strGlass: String?,
    val strInstructions: String?,
    val strInstructionsES: String?,
    val strInstructionsDE: String?,
    val strInstructionsFR: String?,
    val strInstructionsIT: String?,
    @Json(name = "strInstructionsZH-HANS")
    val strInstructionsZH_HANS: String?,
    @Json(name = "strInstructionsZH-HANT")
    val strInstructionsZH_HANT: String?,
    val strDrinkThumb: String?,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,
    val strImageSource: String?,
    val strImageAttribution: String?,
    val strCreativeCommonsConfirmed: String?,
    val dateModified: String?
)

@Parcelize
data class DrinkForDetail(
    val idDrink: String,
    val strDrink: String,
    val strDrinkAlternate: String?,
    val strTags: String?,
    val strVideo: String?,
    val strCategory: String?,
    val strIBA: String?,
    val strAlcoholic: String?,
    val strGlass: String?,
    val strInstructions: String?,
    val strInstructionsES: String?,
    val strInstructionsDE: String?,
    val strInstructionsFR: String?,
    val strInstructionsIT: String?,
    val strInstructionsZH_HANS: String?,
    val strInstructionsZH_HANT: String?,
    val strDrinkThumb: String?,
    var ingredientsList: ArrayList<String>?,
    var measureList: ArrayList<String>?,
    var numberIngredients: Int = 0,
    val strImageSource: String?,
    val strImageAttribution: String?,
    val strCreativeCommonsConfirmed: String?,
    val dateModified: String?
) : Parcelable

/**
 * I could use the Drink class. This was just for practicing purpose.
 * Started as an exercise for me but end up to use it as is.
 * It was just faster to manipulate the information with an array rather than by property.
 **/
fun DrinkDetail.toDetail(): DrinkForDetail {
    val ingredientsList = arrayListOf<String>()
    val measuresList = arrayListOf<String>()
    var count = 0

    for (i in 1..15) {
        readProperties<String>(this, "strIngredient$i")?.let {
            if (it.isNotEmpty()) {
                ingredientsList.add(it)
                count++
            }
        }
        readProperties<String>(this, "strMeasure$i")?.let {
            if (it.isNotEmpty()) {
                measuresList.add(it)
            }
        }
    }

    return DrinkForDetail(
        idDrink,
        strDrink,
        strDrinkAlternate,
        strTags,
        strVideo,
        strCategory,
        strIBA,
        strAlcoholic,
        strGlass,
        strInstructions,
        strInstructionsES,
        strInstructionsDE,
        strInstructionsFR,
        strInstructionsIT,
        strInstructionsZH_HANS,
        strInstructionsZH_HANT,
        strDrinkThumb,
        ingredientsList,
        measuresList,
        count,
        strImageSource,
        strImageAttribution,
        strCreativeCommonsConfirmed,
        dateModified
    )
}
