package org.wit.placemark.views.editlocation

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import org.wit.placemark.R
import org.wit.placemark.models.Location
import android.view.View
import com.google.android.gms.maps.model.LatLng

class EditLocationView : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener{
    var location = Location()
    private var selectedLat = location.lat
    private var selectedLng = location.lng
    private var selectedZoom = location.zoom
    private lateinit var map: GoogleMap
    lateinit var presenter: EditLocationPresenter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        presenter = EditLocationPresenter(this)
        //location = intent.extras?.getParcelable("location",Location::class.java)!!
        location = intent.extras?.getParcelable<Location>("location")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        onBackPressedDispatcher.addCallback(this ) {
            presenter.doOnBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        presenter.initMap(map)
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude,
            marker.position.longitude,
            map.cameraPosition.zoom)
            selectedLat = marker.position.latitude
            selectedLng = marker.position.longitude
            selectedZoom = map.cameraPosition.zoom
        }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateMarker(marker)
        return false
    }

    fun onConfirmLocation(view: View) {
        presenter.doConfirmLocation()
    }
}
