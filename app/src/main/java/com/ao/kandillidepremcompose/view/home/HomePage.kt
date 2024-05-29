package com.ao.kandillidepremcompose.view.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ao.kandillidepremcompose.R
import com.ao.kandillidepremcompose.model.KandilliModel
import com.ao.kandillidepremcompose.model.State
import com.ao.kandillidepremcompose.view.home.component.HomeEarthquakeCard
import com.ao.kandillidepremcompose.view.home.component.HomeTopAppBar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val earthquakeData by if (homeViewModel.isSearchState) homeViewModel.searchResult.observeAsState() else homeViewModel.earthquakeData.observeAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isScrollButton = remember { mutableStateOf(false) }

    Scaffold(topBar = { HomeTopAppBar(viewModel = homeViewModel) }, floatingActionButton = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isScrollButton.value) {
                SmallFloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    containerColor = Color(0xffeeeeee),
                    elevation = FloatingActionButtonDefaults.elevation(1.dp)
                ) {
                    Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "", tint = Color.Black)
                }
            }
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.Transparent)
            )
            if (homeViewModel.isSearchState.not()) {
                FloatingActionButton(onClick = {
                    homeViewModel.setSearch(true)
                }, containerColor = Color(0xff008DDA)) {
                    Image(
                        painter = painterResource(id = R.drawable.im_search_icon),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(color = Color.White)
                    )
                }
            }
        }
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            earthquakeData.let { state ->
                when (state) {
                    is State.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Yükleniyor...")
                        }
                    }

                    is State.Success -> {
                        LaunchedEffect(listState) {
                            snapshotFlow { listState.firstVisibleItemIndex }
                                .collect { index ->
                                    isScrollButton.value = index > 0
                                }
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                        ) {
                            val data = state.data
                            items(data) { item ->
                                Column {
                                    HomeEarthquakeCard(item = item, navController = navController)
                                    if (data.indexOf(item) < data.size - 1) {
                                        Divider()
                                    }
                                }
                            }
                        }
                    }

                    is State.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = (earthquakeData as State.Error).message)
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Unknown Error")
                        }
                    }
                }
            }
        }
    }
}