package pt.hventura.mycoktails.cocktails.randomcocktail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.authentication.AuthenticationActivity
import pt.hventura.mycoktails.base.BaseFragment
import pt.hventura.mycoktails.databinding.FragmentRandomCocktailBinding
import pt.hventura.mycoktails.utils.LoginControl
import pt.hventura.mycoktails.utils.finish
import pt.hventura.mycoktails.utils.startActivity

class RandomCocktailFragment : BaseFragment() {

    private lateinit var binding: FragmentRandomCocktailBinding
    override val viewModel: RandomCocktailViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_random_cocktail, container, false)

        if (!LoginControl.isLoggedIn()) {
            startActivity<AuthenticationActivity>()
            finish()
        }



        return binding.root
    }
}