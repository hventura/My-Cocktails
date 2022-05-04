package pt.hventura.mycoktails.cocktails.detailscocktail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseFragment
import pt.hventura.mycoktails.cocktails.listcocktails.CocktailListViewModel
import pt.hventura.mycoktails.databinding.FragmentDetailsCocktailBinding
import timber.log.Timber

class DetailsCocktailFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailsCocktailBinding
    val args: DetailsCocktailFragmentArgs by navArgs()
    override val viewModel: CocktailListViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_details_cocktail, container, false)
        binding.drink = args.drinkDetail

        binding.drinkGlassType.text = resources.getString(R.string.glassType, args.drinkDetail.strGlass)

        val ingredientsList = args.drinkDetail.ingredientsList
        val measureList = args.drinkDetail.measureList
        val numberIngredients = args.drinkDetail.numberIngredients

        Timber.e(ingredientsList.toString())
        Timber.e(measureList.toString())

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

        return binding.root
    }

}