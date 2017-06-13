package com.github.m3l3m01t.rxdemo.Rest

//import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by jiff.shen on 2017/06/12.
 */

interface TestService {
    @GET("values/{id}")
    fun Get(@Path("id") id: Int): Observable<String>
    @GET("values")
    fun List(): Observable<List<String>>

    @POST("values")
    fun Post(@Body value: String): Completable

/*    @PUT("values/{id}")
    fun Put(@Path("id") id: Int, @Body value: String):Observable<Unit>

    @DELETE("values/{id}")
    fun Delete(@Path("id") id :Int):Observable<Unit>*/

    companion object Factory {
        fun create(url: String): TestService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(url).build()
            return retrofit.create(TestService::class.java);
        }
    }

/*    fun fun1():String? = runBlocking<String?> {
        async(UI, CoroutineStart.DEFAULT) {
            Get(1).execute().body()
        }.await()
    }*/
}
