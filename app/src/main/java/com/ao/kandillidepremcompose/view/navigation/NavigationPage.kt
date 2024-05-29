package com.ao.kandillidepremcompose.view.navigation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ao.kandillidepremcompose.model.KandilliModel
import com.ao.kandillidepremcompose.view.detail_map_page.DetailMapPage
import com.ao.kandillidepremcompose.view.home.HomePage
import com.ao.kandillidepremcompose.view.home.HomeViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Composable
fun NavigationPage(navHostController: NavHostController){
    Surface {
        NavHost(navController = navHostController, startDestination = Route.Home.route ){
            composable(route = Route.Home.route){
                HomePage(navController = navHostController, homeViewModel = HomeViewModel())
            }

            composable(route = Route.Detail.route){navBackStackEntry ->
                val kandilliJson = navBackStackEntry.arguments?.getString("kandilli")
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshi.adapter(KandilliModel::class.java)
                val kandilliObject = jsonAdapter.fromJson(kandilliJson!!)
                DetailMapPage(navController = navHostController, kandilliModel = kandilliObject!!)
            }
        }
    }
}