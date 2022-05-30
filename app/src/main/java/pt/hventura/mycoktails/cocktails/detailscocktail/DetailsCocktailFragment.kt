package pt.hventura.mycoktails.cocktails.detailscocktail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseFragment
import pt.hventura.mycoktails.cocktails.listcocktails.CocktailListViewModel
import pt.hventura.mycoktails.data.models.DrinkForDetail
import pt.hventura.mycoktails.databinding.FragmentDetailsCocktailBinding

class DetailsCocktailFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailsCocktailBinding
    val args: DetailsCocktailFragmentArgs by navArgs()
    override val viewModel: CocktailListViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_details_cocktail, container, false)

        populateDetails(args.drinkDetail!!)

        return binding.root
    }

    private fun populateDetails(drink: DrinkForDetail) {
        binding.drink = drink

        binding.drinkGlassType.text = resources.getString(R.string.glassType, drink.strGlass)

        val ingredientsList = drink.ingredientsList
        val measureList = drink.measureList
        val numberIngredients = drink.numberIngredients

        ingredientsList?.forEachIndexed { index, ingredient ->
            val view = layoutInflater.inflate(R.layout.item_ingredient, null)
            view.findViewById<TextView>(R.id.ingredientName).text = ingredient

            val measure = try {
                measureList!![index]
            } catch (e: IndexOutOfBoundsException) {
                ""
            }

            if (measure.isNotBlank()) {
                view.findViewById<TextView>(R.id.ingredientMeasure).text = measure
                binding.drinkListIngredients.addView(view)
            }

        }

        if (measureList!!.size < numberIngredients) {
            var numberTopping = numberIngredients - measureList.size
            var topWith = "Top with"
            with(ingredientsList!!.takeLast(numberTopping)) {
                for (top in this) {
                    topWith = topWith.plus(" ").plus(top)
                    if (numberTopping > 1) {
                        topWith = topWith.plus(" and")
                        numberTopping--
                    }
                }
            }
            binding.drinkTopping.text = topWith
            binding.drinkTopping.visibility = View.VISIBLE
        } else {
            binding.drinkTopping.text = null
            binding.drinkTopping.visibility = View.GONE
        }
    }

}