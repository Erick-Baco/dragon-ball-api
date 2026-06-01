package mx.unam.network

import mx.unam.model.Character
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://dragonball-api.com/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface DragonBallApiService {
    @GET("characters")
    suspend fun getCharacter(@Query("name") name: String ): List<Character>
}

object DragonBallApi {
    val retrofitService: DragonBallApiService by lazy {
        retrofit.create(DragonBallApiService::class.java)
    }
}