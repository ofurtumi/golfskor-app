package hugbo.golfskor.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hugbo.golfskor.entities.ApiAuth
import hugbo.golfskor.entities.ApiCourse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL = "https://golfskor.onrender.com"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface NetworkService {
    @GET("api/courses")
    suspend fun getCourses(): List<ApiCourse>

    @POST("api/user/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): ApiAuth
}

object GolfSkorApi {
    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}