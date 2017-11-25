package moe.haruue.noyo.api

import moe.haruue.noyo.model.ApiResultWrapper
import moe.haruue.noyo.model.Goods
import moe.haruue.noyo.model.Member
import moe.haruue.noyo.model.Order
import okhttp3.OkHttpClient
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
        private val client by lazy {
            with(OkHttpClient.Builder()) {
                cookieJar(PersistentCookieJar())
                followRedirects(true)
                followSslRedirects(true)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
                connectTimeout(60, TimeUnit.SECONDS)
                build()
            }
        }

        private val retrofit by lazy {
            with(Retrofit.Builder()) {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                client(client)
                build()
            }
        }

        val v1service by lazy {
            retrofit.create(ApiServices::class.java)
        }

        const val BASE_URL = "https://api.caoyue.com.cn/noyo/v1/"

    }

    @POST("account/register")
    @FormUrlEncoded
    fun register(@Field("email") email: String,
                 @Field("username") username: String,
                 @Field("nickname") nickname: String,
                 @Field("password") password: String,
                 @Field("role") role: String): Observable<ApiResultWrapper<Member>>

    @POST("account/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String,
              @Field("password") password: String,
              @Field("persist") persist: Int): Observable<ApiResultWrapper<Member>>

    @POST("account/info")
    @FormUrlEncoded
    fun accountInfo(): Observable<ApiResultWrapper<Member>>

    @POST("account/logout")
    @FormUrlEncoded
    fun logout(): Observable<ApiResultWrapper<Nothing>>

    @POST("account/logout/{what}")
    @FormUrlEncoded
    fun accountVerify(@Path("what") what: String,
                      @Field("uid") uid: String,
                      @Field("op") op: String): Observable<ApiResultWrapper<Nothing>>

    @POST("goods/create")
    @FormUrlEncoded
    fun createGoods(@Field("title") title: String,
                    @Field("summary") summary: String,
                    @Field("count") count: Int,
                    @Field("price") price: Double,
                    @Field("type") type: String,
                    @Field("address") address: String,
                    @Field("image") image: String = ""): Observable<ApiResultWrapper<Goods>>

    @POST("goods/list")
    @FormUrlEncoded
    fun listGoods(@Field("type") type: String): Observable<ApiResultWrapper<MutableList<Goods>>>

    @POST("goods/query/{what}")
    @FormUrlEncoded
    fun queryGoods(@Field("value") value: String): Observable<ApiResultWrapper<MutableList<Goods>>>

    @POST("goods/remove")
    @FormUrlEncoded
    fun removeGoods(@Field("_id") id: String): Observable<ApiResultWrapper<Nothing>>

    @POST("goods/update")
    @FormUrlEncoded
    fun updateGoods(@Field("_id") id: String,
                    @Field("title") title: String,
                    @Field("summary") summary: String,
                    @Field("count") count: Int,
                    @Field("price") price: Double,
                    @Field("type") type: String,
                    @Field("address") address: String,
                    @Field("image") image: String = ""): Observable<ApiResultWrapper<Goods>>

    @POST("order/create")
    @FormUrlEncoded
    fun createOrder(@Field("goodsId") goodsId: String,
                    @Field("count") count: Int,
                    @Field("address") address: String,
                    @Field("external") external: String): Observable<ApiResultWrapper<Order>>

    @POST("order/list")
    @FormUrlEncoded
    fun listOrder(): Observable<ApiResultWrapper<MutableList<Order>>>

    @POST("order/cancel")
    @FormUrlEncoded
    fun cancelOrder(@Field("_id") id: String): Observable<ApiResultWrapper<Order>>

}