package pt.hventura.mycoktails.cocktails.listcocktails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseFragment
import pt.hventura.mycoktails.databinding.FragmentCocktailListBinding
import pt.hventura.mycoktails.utils.setup
import timber.log.Timber

class CocktailListFragment : BaseFragment() {

    private lateinit var binding: FragmentCocktailListBinding
    override val viewModel: CocktailListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_cocktail_list, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCocktails()
    }

    private fun setupRecyclerView() {
        val adapter = CocktailListAdapter {
            Timber.e(it.toString())
        }
        binding.compactCocktailsRecyclerView.setup(adapter)
    }
}