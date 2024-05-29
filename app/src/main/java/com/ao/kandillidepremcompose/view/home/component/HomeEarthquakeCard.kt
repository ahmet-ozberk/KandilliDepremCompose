package com.ao.kandillidepremcompose.view.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ao.kandillidepremcompose.R
import com.ao.kandillidepremcompose.model.KandilliModel
import com.ao.kandillidepremcompose.view.navigation.Route
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Composable
fun HomeEarthquakeCard(item: KandilliModel, navController: NavController) {
    Box(
        modifier = Modifier
            .clickable {
                val moshi = Moshi
                    .Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter = moshi
                    .adapter(KandilliModel::class.java)
                val kandilliJson = jsonAdapter.toJson(item)
                navController.navigate(Route.Detail.route.replace("{kandilli}", kandilliJson))
            }
            .padding(
                vertical = 24.dp,
                horizontal = 12.dp
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(44.dp)
                    .background(
                        buyuklukColor(item.buyukluk)
                    )
            ) {
                Text(
                    text = item.buyukluk ?: "",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.yer ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "${item.tarih ?: "Tarih Yok"} - ${item.saat ?: "Saat Yok"}")
            }
            Spacer(modifier = Modifier.size(12.dp))
            Image(
                painter = painterResource(id = R.drawable.im_arrow_right),
                contentDescription = "Arrow",
                Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(color = Color.DarkGray)
            )
        }
    }
}

private fun buyuklukColor(buyuklukStr: String?): Color {
    val buyukluk = try {
        buyuklukStr?.toDouble() ?: 0.0
    } catch (e: Exception) {
        0.0
    }

    return when {
        buyukluk < 4.0 -> Color(0xff008000)
        buyukluk >= 4.0 && buyukluk < 5.0 -> Color(0xFFFFA500) // Orange
        buyukluk >= 5.0 && buyukluk < 6.0 -> Color.Red
        buyukluk >= 6.0 -> Color(0xFF800080) // Purple
        else -> Color.Blue
    }
}