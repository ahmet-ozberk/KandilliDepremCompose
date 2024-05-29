package com.ao.kandillidepremcompose.view.home.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ao.kandillidepremcompose.R
import com.ao.kandillidepremcompose.ui.theme.Purple40
import com.ao.kandillidepremcompose.utils.enums.ShortTypes
import com.ao.kandillidepremcompose.view.home.HomeViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeTopAppBar(viewModel: HomeViewModel) {
    val isSearch = remember {
        derivedStateOf {
            viewModel.isSearchState
        }
    }
    var expanded by remember { mutableStateOf(false) }
    val focusable = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TopAppBar(
        title = {
            if (isSearch.value) {
                LaunchedEffect(Unit) {
                    focusable.requestFocus()
                }
                Box {
                    BasicTextField(
                        value = viewModel.searchText,
                        onValueChange = { viewModel.searchEarthquake(it) },
                        maxLines = 1,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        ),
                        modifier = Modifier
                            .focusRequester(focusable),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .padding(8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (viewModel.searchText.isEmpty()) {
                                    Text(
                                        text = "Ara...",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    if (viewModel.searchText.isNotEmpty()) {
                        Text(
                            text = "Ara",
                            fontSize = 14.sp,
                            color = Purple40,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .offset(y = 3.dp)
                        )
                    }
                }
                DisposableEffect(Unit) {
                    onDispose {
                        keyboardController?.hide()
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.ic_logo), contentDescription = "")
                    Text(text = "Kandilli Deprem")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.LightGray.copy(alpha = 0.2f)
        ),
        actions = {
            if (isSearch.value) {
                IconButton(onClick = { viewModel.setSearch(false) }) {
                    Icon(Icons.Rounded.Clear, contentDescription = "")
                }
            } else {
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_filter_list),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        ShortTypes.entries.forEach { type ->
                            Box(
                                modifier = Modifier.background(
                                    if (type == viewModel.shortType) Color.White.copy(
                                        alpha = .4f
                                    ) else Color.Transparent
                                )
                            ) {
                                DropdownMenuItem(text = {
                                    Text(text = type.title)
                                }, onClick = {
                                    expanded = false
                                    viewModel.setShort(type)
                                }, trailingIcon = {
                                    if (viewModel.shortType == type) {
                                        Icon(Icons.Rounded.Check, contentDescription = "")
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    )
}
