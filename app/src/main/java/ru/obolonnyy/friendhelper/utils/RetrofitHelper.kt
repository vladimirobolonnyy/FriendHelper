package ru.obolonnyy.friendhelper.utils

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.obolonnyy.friendhelper.protect.ServerApi
import ru.obolonnyy.friendhelper.protect.Stand
import timber.log.Timber
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object RetrofitHelper {

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
                .initTls()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    fun createRetrofit(stand: StandI): ServerApi {
        return Retrofit.Builder()
                .baseUrl(stand.url)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okHttpClient)
                .build()
                .create(ServerApi::class.java)
    }

    private fun OkHttpClient.Builder.initTls(): OkHttpClient.Builder {
        val trustManager = unsafeTrustManager()
        sslSocketFactory(
                allTrustingSocketFactory(trustManager),
                trustManager
        )
        hostnameVerifier { _, _ -> true }
        return this
    }

    private fun allTrustingSocketFactory(trustManager: X509TrustManager): SSLSocketFactory {
        return try {
            val trustManagers = arrayOf(trustManager)
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManagers, SecureRandom())
            sslContext.socketFactory
        } catch (ex: NoSuchAlgorithmException) {
            Timber.e(ex)
            throw RuntimeException(ex)
        }
    }

    private fun unsafeTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    }
}