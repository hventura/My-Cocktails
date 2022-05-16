package pt.hventura.mycoktails.cocktails.listcocktails

import android.view.View
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseRecyclerViewAdapter
import pt.hventura.mycoktails.data.models.CompactDrink

class CocktailListAdapter(callBack: (view: View, selectedCompactDrink: CompactDrink, position: Int) -> Unit) :BaseRecyclerViewAdapter<CompactDrink>(callBack) {
    override fun getLayoutRes(viewType: Int): Int = R.layout.item_compact_drink

}