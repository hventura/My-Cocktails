package pt.hventura.mycoktails.cocktails.detailscocktail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseFragment
import pt.hventura.mycoktails.cocktails.listcocktails.CocktailListViewModel

class DetailsCocktailFragment : BaseFragment() {

    override val viewModel: CocktailListViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_cocktail, container, false)
    }

}