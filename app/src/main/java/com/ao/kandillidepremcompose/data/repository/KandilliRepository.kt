package com.ao.kandillidepremcompose.data.repository

import com.ao.kandillidepremcompose.model.KandilliModel
import com.ao.kandillidepremcompose.model.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class KandilliRepository {
    private val BASE_URL = "http://www.koeri.boun.edu.tr/scripts/lst0.asp"

    private fun fetchHtmlData(): Flow<State<String>> {
        return flow {
            emit(State.loading())
            try {
                val doc: Document = Jsoup.connect(BASE_URL).get()
                val result = doc.getElementsByTag("pre").text().trim()
                emit(State.success(result))
            } catch (e: Exception) {
                emit(
                    State.error(
                        e.message ?: "fetchHtmlData: Bilinmeyen bir hata oluştu ${e.message}"
                    )
                )
            }
        }
    }

    fun getEarthquakes(): Flow<State<List<KandilliModel>>> {
        return flow<State<List<KandilliModel>>> {
            try {
                fetchHtmlData().collect { htmlDataState ->
                    when (htmlDataState) {
                        is State.Error -> {
                            emit(State.error(htmlDataState.message))
                        }

                        is State.Loading -> {
                            emit(State.loading())
                        }

                        is State.Success -> {
                            val data = htmlDataState.data
                            val dataList = data.split("\n")
                            val kandilliModelList = mutableListOf<KandilliModel>()
                            dataList.forEachIndexed { index, value ->
                                if (index >= 6) {
                                    val tokens = value.split(" ")
                                    val list = mutableListOf<String>()
                                    tokens.forEachIndexed { index2, value2 ->
                                        if (value2.trim().isNotEmpty() && value2 != "-.-") {
                                            value2.trim()
                                            list.add(value2)
                                        }
                                    }
                                    val depremModel = KandilliModel(
                                        id = index,
                                        tarih = list[0],
                                        saat = list[1],
                                        enlem = list[2],
                                        boylam = list[3],
                                        derinlik = list[4],
                                        buyukluk = list[5],
                                        yer = "${list[6]} ${list[7]}"
                                    )
                                    kandilliModelList.add(depremModel)
                                    list.clear()
                                }
                            }
                            println("Data Büyüklüğü -> ${kandilliModelList.size}")
                            emit(State.success(kandilliModelList))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(State.error(e.message ?: "fetchData: Bilinmeyen bir hata oluştu ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)
            .catch { err ->
                emit(
                    State.error(
                        err.message ?: "fetchData flowOn: Bilinmeyen bir hata oluştu ${err.message}"
                    )
                )
            }
    }
}