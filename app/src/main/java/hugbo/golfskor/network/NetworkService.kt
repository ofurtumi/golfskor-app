package hugbo.golfskor.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hugbo.golfskor.entities.ApiAuth
import hugbo.golfskor.entities.ApiCourse
import hugbo.golfskor.entities.ApiUserInfo
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL = "https://golfskor.onrender.com"

val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.HEADERS
}

val client: OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(interceptor)
}.build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(client)
    .build()


interface NetworkService {
    @GET("api/courses")
    suspend fun getCourses(): List<ApiCourse>

    @POST("api/user/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): ApiAuth

    @POST("api/user/register")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String
    )

    @GET("api/user/info")
    suspend fun getUserRounds(
        @Query("username") username: String,
        @Header("Authorization") authToken: String
    ): ApiUserInfo
}

object GolfSkorApi {
    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}