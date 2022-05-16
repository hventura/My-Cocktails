package pt.hventura.mycoktails.cocktails.favoritecocktails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseFragment
import pt.hventura.mycoktails.cocktails.listcocktails.CocktailListAdapter
import pt.hventura.mycoktails.cocktails.listcocktails.CocktailListViewModel
import pt.hventura.mycoktails.data.models.CompactDrink
import pt.hventura.mycoktails.databinding.FragmentFavouritesCocktailBinding
import pt.hventura.mycoktails.utils.setup
import timber.log.Timber

class FavouritesCocktailFragment : BaseFragment() {

    private lateinit var binding: FragmentFavouritesCocktailBinding
    private lateinit var adapter: CocktailListAdapter
    private var favouriteList = mutableListOf<CompactDrink>()
    override val viewModel: CocktailListViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favourites_cocktail, container, false)
        binding.viewModel = viewModel

        initializeObservables()

        viewModel.loadFavouriteDrinks()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupFavouritesRecyclerView()
    }

    private fun initializeObservables() {
        viewModel.favouriteCocktailsList.observe(requireActivity()) {
            favouriteList = it
        }
    }

    private fun setupFavouritesRecyclerView() {
        adapter = CocktailListAdapter { view, item, position ->
            when (view) {
                is ImageView -> {
                    if (view.id == R.id.favourite) {
                        viewModel.setDrinkAsFavourite(item.idDrink)
                    }
                }
                else -> viewModel.loadCocktailDetail(item.idDrink, true)
            }
            adapter.notifyItemChanged(position)
        }
        binding.favouritesCocktailsRecyclerView.setup(adapter)
    }

}