package moe.haruue.noyo.api

import moe.haruue.noyo.model.APIResultWrapper
import moe.haruue.noyo.model.Goods
import moe.haruue.noyo.model.Member
import moe.haruue.noyo.model.Order
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable
import java.util.concurrent.TimeUnit

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
interface ApiServices {

    companion object {
        private val client = with(OkHttpClient.Builder()) {
            cookieJar(PersistentCookieJar)
            followRedirects(true)
            followSslRedirects(true)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor())
            build()
        }

        private val retrofit = with(Retrofit.Builder()) {
            baseUrl(BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            client(client)
            build()
        }

        val v1service = retrofit.create(ApiServices::class.java)!!

        const val BASE_URL = "https://api.caoyue.com.cn/noyo/v1/"

    }

    @POST("account/register")
    @FormUrlEncoded
    fun register(@Field("email") email: String,
                 @Field("username") username: String,
                 @Field("nickname") nickname: String,
                 @Field("password") password: String,
                 @Field("role") role: String): Observable<APIResultWrapper<Member>>

    @POST("account/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String,
              @Field("password") password: String,
              @Field("persist") persist: Int): Observable<APIResultWrapper<Member>>

    @POST("account/info")
    fun accountInfo(): Observable<APIResultWrapper<Member>>

    @POST("account/logout")
    fun logout(): Observable<APIResultWrapper<Nothing>>

    @POST("account/update/{what}")
    @FormUrlEncoded
    fun accountUpdate(@Path("what") what: String,
                      @Field("value") value: String,
                      @Field("old") old: String = "")

    @POST("account/verify/{what}")
    @FormUrlEncoded
    fun accountVerify(@Path("what") what: String,
                      @Field("uid") uid: String,
                      @Field("op") op: String): Observable<APIResultWrapper<Nothing>>

    @POST("goods/create")
    @FormUrlEncoded
    fun createGoods(@Field("title") title: String,
                    @Field("summary") summary: String,
                    @Field("count") count: Int,
                    @Field("price") price: Double,
                    @Field("type") type: String,
                    @Field("address") address: String,
                    @Field("image") image: String = ""): Observable<APIResultWrapper<Goods>>

    @POST("goods/list")
    @FormUrlEncoded
    fun listGoods(@Field("type") type: String): Observable<APIResultWrapper<MutableList<Goods>>>

    @POST("goods/query/{what}")
    @FormUrlEncoded
    fun queryGoods(@Field("value") value: String): Observable<APIResultWrapper<MutableList<Goods>>>

    @POST("goods/remove")
    @FormUrlEncoded
    fun removeGoods(@Field("_id") id: String): Observable<APIResultWrapper<Nothing>>

    @POST("goods/update")
    @FormUrlEncoded
    fun updateGoods(@Field("_id") id: String,
                    @Field("title") title: String,
                    @Field("summary") summary: String,
                    @Field("count") count: Int,
                    @Field("price") price: Double,
                    @Field("type") type: String,
                    @Field("address") address: String,
                    @Field("image") image: String = ""): Observable<APIResultWrapper<Goods>>

    @POST("order/create")
    @FormUrlEncoded
    fun createOrder(@Field("goodsId") goodsId: String,
                    @Field("count") count: Int,
                    @Field("address") address: String,
                    @Field("external") external: String): Observable<APIResultWrapper<Order>>

    @POST("order/list")
    fun listOrder(): Observable<APIResultWrapper<MutableList<Order>>>

    @POST("order/cancel")
    @FormUrlEncoded
    fun cancelOrder(@Field("_id") id: String): Observable<APIResultWrapper<Order>>

}