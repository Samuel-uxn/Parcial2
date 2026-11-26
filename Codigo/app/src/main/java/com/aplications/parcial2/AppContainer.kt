package com.aplications.parcial2

import com.aplications.parcial2.data.remote.RetrofitClient
import com.aplications.parcial2.data.repository.TravelRepositoryImpl

/** Punto único donde se crean las dependencias. */
object AppContainer {
    val repository: TravelRepositoryImpl by lazy { TravelRepositoryImpl(RetrofitClient.api) }
}