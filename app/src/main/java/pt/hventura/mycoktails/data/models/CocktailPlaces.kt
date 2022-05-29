package pt.hventura.mycoktails.data.models

import com.google.android.gms.maps.model.LatLng

object CocktailPlaces {

    val places = listOf<CocktailPlace>(
        CocktailPlace("Papa", LatLng(40.207167, -8.420315)),
        CocktailPlace("Spaghetti Notte Ristorante Italiano", LatLng(40.214071, -8.393785)),
        CocktailPlace("Docas Rio", LatLng(40.202701, -8.426310)),
        CocktailPlace("Tertúlia d'Eventos", LatLng(40.201932, -8.431340)),
        CocktailPlace("Galeria Santa Clara", LatLng(40.201625, -8.433576)),
        CocktailPlace("PASSAPORTE Coimbra", LatLng(40.207024, -8.428603)),
        CocktailPlace("Café Tropical", LatLng(40.209064, -8.419567)),
        CocktailPlace("What's up Doc Bar", LatLng(40.210018, -8.419793)),
        CocktailPlace("Liquidâmbar", LatLng(40.209994, -8.419119)),
        CocktailPlace("Pastelaria Mildoce", LatLng(40.212002, -8.422936)),
    )

}