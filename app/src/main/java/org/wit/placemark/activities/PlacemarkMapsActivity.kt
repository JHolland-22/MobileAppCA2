package org.wit.placemark.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso

import org.wit.placemark.databinding.ActivityPlacemarkMapsBinding
import org.wit.placemark.databinding.ContentPlacemarkMapsBinding
import org.wit.placemark.main.MainApp

class PlacemarkMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener
{

    lateinit var app: MainApp

    private lateinit var binding: ActivityPlacemarkMapsBinding
    private lateinit var contentBinding: ContentPlacemarkMapsBinding
    lateinit var map: GoogleMap

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        map.setOnMapClickListener(this)

        val placemarks = app.placemarks.findAll()
        if (placemarks.isEmpty()) return

        placemarks.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions()
                .title(it.title)
                .position(loc)

            map.addMarker(options)?.tag = it.id
        }
        val first = placemarks.first()
        val startLoc = LatLng(first.lat, first.lng)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, first.zoom))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        app = application as MainApp
        super.onCreate(savedInstanceState)

        binding = ActivityPlacemarkMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        contentBinding = ContentPlacemarkMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)

        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        //val placemark = marker.tag as PlacemarkModel.kt
        val tag = marker.tag as Long
        val placemark = app.placemarks.findById(tag)
        contentBinding.currentTitle.text = placemark!!.title
        contentBinding.currentDescription.text = placemark.description
        Picasso.get().load(placemark.image).into(contentBinding.currentImage)
        return false
    }

    override fun onMapClick(latLng: LatLng) {
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Selected location")
            )

        println("Clicked location: ${latLng.latitude}, ${latLng.longitude}")
    }


}
