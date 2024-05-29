package com.ao.kandillidepremcompose.view.detail_map_page

import android.annotation.SuppressLint

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ao.kandillidepremcompose.R
import com.ao.kandillidepremcompose.model.KandilliModel
import com.ao.kandillidepremcompose.ui.theme.Typography
import org.osmdroid.views.MapView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailMapPage(
    navController: NavController,
    kandilliModel: KandilliModel,
    viewModel: DetailMapViewModel = viewModel()
) {

    val lat = kandilliModel.enlem?.toDoubleOrNull() ?: 0.0
    val lon = kandilliModel.boylam?.toDoubleOrNull() ?: 0.0


    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    MapView(context).apply {
                        setMultiTouchControls(true)
                        controller.setZoom(10.0)
                        controller.setCenter(viewModel.location(lat, lon))
                        overlays.add(
                            viewModel.getMarkerIcon(
                                context,
                                this,
                                viewModel.location(lat, lon)
                            )
                        )
                    }
                },
                update = {}
            )
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }, colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color.Black,
                        )
                    ) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "", tint = Color.White)
                    }
                }
                Card(
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 8.dp,
                    ),
                    modifier = Modifier
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    )
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .background(Color.Red)
                                .padding(12.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = kandilliModel.yer ?: "Yer Bilgisi Yok",
                                style = Typography.titleMedium.copy(color = Color.White),
                                textAlign = TextAlign.Center
                            )
                        }
                        Row {
                            ListItem(leadingContent = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_date),
                                    contentDescription = ""
                                )
                            }, headlineContent = {
                                Text(
                                    text = kandilliModel.tarih ?: "Tarih Bilgisi Yok",
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1
                                )
                            }, supportingContent = {
                                Text(
                                    text = "Tarih"
                                )
                            }, colors = ListItemDefaults.colors(
                                containerColor = Color.White
                            ), modifier = Modifier.weight(1f)
                            )
                            ListItem(leadingContent = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_time),
                                    contentDescription = ""
                                )
                            }, headlineContent = {
                                Text(
                                    text = kandilliModel.saat ?: "Saat Bilgisi Yok",
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1
                                )
                            }, supportingContent = {
                                Text(
                                    text = "Saat"
                                )
                            }, colors = ListItemDefaults.colors(
                                containerColor = Color.White
                            ), modifier = Modifier.weight(1f)
                            )
                        }
                        Row {
                            ListItem(leadingContent = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_lens),
                                    contentDescription = ""
                                )
                            }, headlineContent = {
                                Text(
                                    text = "${kandilliModel.derinlik} km",
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1
                                )
                            }, supportingContent = {
                                Text(
                                    text = "Derinlik"
                                )
                            }, colors = ListItemDefaults.colors(
                                containerColor = Color.White
                            ), modifier = Modifier.weight(1f)
                            )
                            ListItem(leadingContent = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_monitor),
                                    contentDescription = ""
                                )
                            }, headlineContent = {
                                Text(
                                    text = kandilliModel.buyukluk ?: "Büyüklük Bilgisi Yok",
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1
                                )
                            }, supportingContent = {
                                Text(
                                    text = "Büyüklük"
                                )
                            }, colors = ListItemDefaults.colors(
                                containerColor = Color.White
                            ), modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}