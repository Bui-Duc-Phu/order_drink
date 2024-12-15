import android.content.Context
import com.example.codes.R

import com.example.restfull_api_1.Networks.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object RetrofitClient {

    private const val BASE_URL: String = "https://192.168.0.109:3000/"
    fun getInstance(context: Context): ApiService {
        val client = getUnsafeOkHttpClient(context).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun getUnsafeOkHttpClient(context: Context): OkHttpClient.Builder {
        return try {
            // Load CAs from an InputStream
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val inputStream = context.resources.openRawResource(R.raw.cert) // Ensure this is the correct file
            val ca = certificateFactory.generateCertificate(inputStream)

            // Create a KeyStore containing our trusted CAs
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)

            // Create a TrustManager that trusts the CAs in our KeyStore
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            val trustManagers = trustManagerFactory.trustManagers

            // Create an SSLContext that uses our TrustManager
            val sslContext = SSLContext.getInstance("TLS") // Use "TLS" instead of "SSL"
            sslContext.init(null, trustManagers, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustManagers[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> hostname == "192.168.0.109" }
            builder.addInterceptor(logging)
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}
