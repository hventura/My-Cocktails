package pt.hventura.mycoktails.cocktails.listcocktails

import android.view.View
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseRecyclerViewAdapter
import pt.hventura.mycoktails.data.models.Drink

class CocktailListAdapter(callBack: (view: View, selectedCompactDrink: Drink, position: Int) -> Unit) :BaseRecyclerViewAdapter<Drink>(callBack) {
    override fun getLayoutRes(viewType: Int): Int = R.layout.item_compact_drink

}