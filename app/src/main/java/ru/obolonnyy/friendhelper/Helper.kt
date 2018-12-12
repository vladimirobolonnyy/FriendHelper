package ru.obolonnyy.friendhelper

import android.os.Environment
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.obolonnyy.friendhelper.protect.ServerApi
import ru.obolonnyy.friendhelper.protect.Stand
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object Helper {

    private val client by lazy {
        OkHttpClient.Builder()
                .initTls()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    fun createRetrofit(stand: Stand): ServerApi {
        return Retrofit.Builder()
                .baseUrl(stand.url)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()
                .create(ServerApi::class.java)
    }

    fun OkHttpClient.Builder.initTls(): OkHttpClient.Builder {
        val trustManager = newUnsafeTrustManager()
        sslSocketFactory(
                newAllTrustingSocketFactory(trustManager),
                trustManager
        )
        hostnameVerifier { _, _ -> true }
        return this
    }

    private fun newAllTrustingSocketFactory(trustManager: X509TrustManager): SSLSocketFactory {
        try {
            val trustAllCerts = arrayOf<TrustManager>(trustManager)
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            return sslContext.socketFactory
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    private fun newUnsafeTrustManager(): X509TrustManager {
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

    fun getExturnalStorage() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)


}