package com.jaychang.sac

import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

typealias LogLevel = HttpLoggingInterceptor.Level

open class SimpleApiClient {

  data class Config(
    var baseUrl: String = "",
    var connectTimeout: Long = TimeUnit.MINUTES.toMillis(1),
    var readTimeout: Long = TimeUnit.MINUTES.toMillis(1),
    var writeTimeout: Long = TimeUnit.MINUTES.toMillis(1),
    var defaultHeaders: Map<String, String>? = null,
    var defaultParameters: Map<String, String>? = null,
    var certificatePins: List<CertificatePin>? = null,
    var logLevel: LogLevel = LogLevel.NONE,
    var networkInterceptors: List<Interceptor>? = null,
    var interceptors: List<Interceptor>? = null,
    var httpClient: OkHttpClient? = null,
    var isMockResponseEnabled: Boolean = false,
    var errorClass: KClass<out SimpleApiError>? = null,
    var errorMessageKeyPath: String? = null,
    var errorHandler: ((Throwable) -> Unit)? = null,
    var jsonParser: JsonParser = GsonJsonParser()
  )

  companion object {
    @JvmStatic
    inline fun <reified Api : Any> create(init: Config.() -> Unit): Api {
      val config = Config()
      config.init()
      return ApiManager.init(config).create(Api::class.java)
    }

    @JvmStatic
    fun all(vararg calls: Observable<*>): Observable<Array<Any>> {
      return Observable.zip(calls.asIterable(), { objects -> objects })
    }
  }

}