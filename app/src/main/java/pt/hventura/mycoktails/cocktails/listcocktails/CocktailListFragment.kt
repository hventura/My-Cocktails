package pt.hventura.mycoktails.cocktails.listcocktails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.button.MaterialButton
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

    private val activityLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Timber.e(result.resultCode.toString())
        Timber.e(Activity.RESULT_OK.toString())
        Timber.e(result.data.toString())

        if (result.resultCode == Activity.RESULT_OK) {
            //viewModel.loadCocktailList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_cocktail_list, container, false)
        binding.viewModel = viewModel
        registerObservables()
        viewModel.loadCocktailList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
    }

    private fun registerObservables() {
        viewModel.openWifiDefinitions.observe(requireActivity()) {
            if (it) {
                val dialog = MaterialDialog(requireContext())
                dialog.customView(R.layout.dialog_wifi, noVerticalPadding = true)
                dialog.findViewById<MaterialButton>(R.id.dialog_wifi_no).setOnClickListener {
                    dialog.dismiss()
                }
                dialog.findViewById<MaterialButton>(R.id.dialog_wifi_yes).setOnClickListener {
                    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    activityLaucher.launch(intent)
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = CocktailListAdapter { view, item, position ->
            when (view) {
                is ImageView -> {
                    if (view.id == R.id.favourite) {
                        item.favourite = !item.favourite
                        viewModel.setDrinkAsFavourite(item.idDrink, item.favourite)
                        Timber.e(item.toString())
                        adapter.notifyItemChanged(position)
                    }
                }
                else -> {
                    Timber.e(item.toString())
                    viewModel.loadCocktailDetail(item.idDrink, false)
                }
            }

        }
        binding.compactCocktailsRecyclerView.setup(adapter)
    }

}