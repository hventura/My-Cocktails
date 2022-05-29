package pt.hventura.mycoktails.cocktails.coctailsMap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.data.geojson.GeoJsonFeature
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPoint
import com.google.maps.android.data.geojson.GeoJsonPointStyle
import org.koin.androidx.viewmodel.ext.android.viewModel
import pt.hventura.mycoktails.BuildConfig
import pt.hventura.mycoktails.R
import pt.hventura.mycoktails.base.BaseFragment
import pt.hventura.mycoktails.databinding.FragmentCocktailMapBinding
import pt.hventura.mycoktails.utils.getBitmapFromDrawable


class CocktailMapFragment : BaseFragment(), OnMapReadyCallback {

    override val viewModel: CocktailMapViewModel by viewModel()
    private lateinit var binding: FragmentCocktailMapBinding
    private lateinit var map: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var latLngBoundsBuilder: LatLngBounds.Builder
    private var itemBoundingBox: LatLngBounds? = null
    private var hasCoords: Boolean = false
    private var geoJsonLayer: GeoJsonLayer? = null
    private var geoJsonInitialized = false
    private var snackbar: Snackbar? = null
    private var mapReady = false
    private var permissionsGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_cocktail_map, container, false)

        latLngBoundsBuilder = LatLngBounds.Builder()

        checkPermissions()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.areaMapLayer.setOnClickListener {
            if (map.mapType == GoogleMap.MAP_TYPE_NORMAL) {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            } else {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setPadding(10, 10, 40, 10)
        if (permissionsGranted) {
            enableMyLocation(map)
        }
        setMapStyle(map)
        mapReady = true

        if (!geoJsonInitialized) {
            initializeGeoJson()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation(map: GoogleMap) {
        map.isMyLocationEnabled = true
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = INTERVAL
            fastestInterval = FAST_INTERVAL
        }

        LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                LocationServices.getFusedLocationProviderClient(requireContext()).removeLocationUpdates(this)
                if (locationResult.locations.size > 0) {
                    val latLng = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                    val cameraPosition = CameraPosition.Builder()
                        .target(latLng)
                        .zoom(18f)
                        .bearing(locationResult.lastLocation.bearing)
                        .build()
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
                super.onLocationResult(locationResult)
            }
        }, Looper.getMainLooper())
    }

    private fun setMapStyle(map: GoogleMap) {
        /**
         * Ensures that the style is applied and changed accordingly with hour of day
         * This logic can be enhanced given the TimeZone and/or through User configuration
         * For now lets keep it simple and say that from 19:00h forward is night time
         **/
        viewModel.hourOfDay.observe(this.viewLifecycleOwner) { hour ->
            try {
                val successMapStyle = if (hour < 19) {
                    map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_day))
                } else {
                    map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_night))
                }

                if (!successMapStyle) {
                    viewModel.showErrorMessage.value = "Something went wrong with the Map Style. Contact support and provide this error"
                }

            } catch (e: Resources.NotFoundException) {
                viewModel.showErrorMessage.value = "Can't find the style. Error: ${e.message}"
            }
        }
    }

    private fun initializeGeoJson() {
        geoJsonInitialized = true
        geoJsonLayer = GeoJsonLayer(map, R.raw.cocktail_places, requireContext())
        geoJsonLayer!!.features.forEach { feature ->
            setBoundsAndStylePoint(feature)
        }

        if (hasCoords) {
            itemBoundingBox = latLngBoundsBuilder.build()
        }
        geoJsonLayer!!.addLayerToMap()
        moveToBoundings()
    }

    private fun setBoundsAndStylePoint(feature: GeoJsonFeature) {
        val mPointStyle = GeoJsonPointStyle()
        mPointStyle.icon = requireContext().getBitmapFromDrawable(R.drawable.ic_cocktail_map)
        mPointStyle.title = feature.getProperty("name") ?: "No name provided!"

        feature.pointStyle = mPointStyle

        if (feature.geometry.geometryType == "Point") {
            val geoJsonPoint = (feature.geometry) as GeoJsonPoint
            latLngBoundsBuilder.include(geoJsonPoint.coordinates)
            hasCoords = true
        }

    }

    private fun moveToBoundings() {
        map.stopAnimation()
        if (itemBoundingBox != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(itemBoundingBox!!, 50))
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(CENTER_POINT, 9F))
        }
    }

    /***************
     * PERMISSIONS *
     ***************/
    private fun checkPermissions() {
        if (isPermissionGranted()) {
            permissionsGranted = true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                permissionsGranted = true
                if (mapReady) {
                    enableMyLocation(map)
                }
            } else {
                snackbar = Snackbar.make(
                    requireView(),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.settings) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }
                snackbar!!.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (snackbar != null && snackbar!!.isShown) {
            snackbar!!.dismiss()
        }
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1001
        const val INTERVAL = 0L
        const val FAST_INTERVAL = 0L
        val CENTER_POINT = LatLng(40.203345, -8.410286)
    }
}