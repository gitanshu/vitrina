package stoyck.vitrina.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import stoyck.vitrina.network.data.RedditAboutPage
import stoyck.vitrina.network.data.RedditPostPage

interface RedditApi {

    @GET("/r/{$SUBREDDIT}/about.json")
    suspend fun getAbout(
        @Path(SUBREDDIT) subreddit: String
    ): RedditAboutPage

    @GET("/r/{$SUBREDDIT}.json")
    suspend fun getPosts(
        @Path(SUBREDDIT) subreddit: String,
        @Query("limit") limit: Int
    ): RedditPostPage

    companion object {
        private const val SUBREDDIT = "subreddit"

        fun create(modifier: (builder: Retrofit.Builder) -> Unit = {}): RedditApi {
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val client: OkHttpClient = OkHttpClient.Builder().apply {
                this.addInterceptor(interceptor)
            }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.reddit.com")
//                    .addCallAdapterFactory(
//                            RxJava2CallAdapterFactory.create()
//                    )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .client(client)
                .apply(modifier)
                .build()

            return retrofit.create(RedditApi::class.java)
        }
    }

}