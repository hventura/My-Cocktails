package pt.hventura.mycoktails.cocktails.listcocktails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseFragment
import pt.hventura.mycoktails.databinding.FragmentCocktailListBinding
import pt.hventura.mycoktails.utils.setup
import timber.log.Timber

class CocktailListFragment : BaseFragment() {

    private lateinit var binding: FragmentCocktailListBinding
    private lateinit var adapter: CocktailListAdapter
    override val viewModel: CocktailListViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_cocktail_list, container, false)
        binding.viewModel = viewModel
        Timber.e("onCreateView -> loadCocktailList()")
        viewModel.loadCocktailList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = CocktailListAdapter { view, item, position ->
            when (view) {
                is ImageView -> {
                    if (view.id == R.id.favourite) {
                        viewModel.setDrinkAsFavourite(item.idDrink)
                    }
                }
                else -> viewModel.loadCocktailDetail(item.idDrink, false)
            }
            adapter.notifyItemChanged(position)
        }
        binding.compactCocktailsRecyclerView.setup(adapter)
    }

}