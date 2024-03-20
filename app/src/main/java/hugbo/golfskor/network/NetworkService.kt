package hugbo.golfskor.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hugbo.golfskor.entities.ApiAuth
import hugbo.golfskor.entities.ApiCourse
import hugbo.golfskor.entities.ApiRound
import hugbo.golfskor.entities.ApiUserInfo
import io.github.cdimascio.dotenv.dotenv
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

val dotenv = dotenv {
    directory = "/assets"
    filename = "env"
}
private val BASE_URL = dotenv["BASE_URL"] ?: "https://golfskor.onrender.com"

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

    @POST("api/user/refresh")
    suspend fun refreshToken(
        @Query("username") username: String,
        @Header("Authorization") token: String
    ): ApiAuth

    @GET("api/user/info")
    suspend fun getUserRounds(
        @Query("username") username: String,
        @Header("Authorization") authToken: String
    ): ApiUserInfo

    @GET("api/getround")
    suspend fun getRound(
        @Query("id") roundId: Int,
    ): ApiRound

    @PATCH("api/round")
    suspend fun updateRound(
        @Query("roundId") roundId: Int,
        @Query("holes") holes: List<Int>,
        @Query("userId") userId: Int,
        @Header("Authorization") authToken: String
    ): ApiRound

    @POST("api/round")
    suspend fun postRound(
        @Query("courseId") courseId: Int,
        @Query("holes") holes: List<Int>,
        @Query("userId") username: Int,
        @Header("Authorization") authToken: String
    ): ApiRound

    @DELETE("api/round")
    suspend fun deleteRound(
        @Header("Authorization") authToken: String,
        @Query("roundId") roundId: Int,
        @Query("userId") userId: Int
    ): Response<Void>
}

object GolfSkorApi {
    val retrofitService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}