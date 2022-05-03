package pt.hventura.mycoktails.cocktails.detailscocktail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.drinkGlassType.text = resources.getText(R.string.glassType, args.drinkDetail.strGlass)

        Timber.e(args.drinkDetail.ingredientsList.toString())
        Timber.e(args.drinkDetail.measureList.toString())

        return binding.root
    }

}