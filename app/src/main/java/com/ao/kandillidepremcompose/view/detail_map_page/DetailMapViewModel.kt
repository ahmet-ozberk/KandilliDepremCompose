package com.ao.kandillidepremcompose.view.detail_map_page

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.ao.kandillidepremcompose.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


class DetailMapViewModel : ViewModel() {

    fun location(lat: Double, long: Double): GeoPoint {
        return GeoPoint(lat, long)
    }

    fun getMarkerIcon(context: Context, mapView: MapView, location: GeoPoint): Marker {
        val marker = Marker(mapView)
        val iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_marker)
        marker.icon = iconDrawable
        marker.position = location
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Deprem Konumu"
        return marker
    }
}