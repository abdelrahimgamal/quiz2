package myapp.aishari.aishari_oblig2.domain

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import myapp.aishari.aishari_oblig2.data.api.DataSource
import myapp.aishari.aishari_oblig2.data.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

class DataSourceImp {

    val logging = HttpLoggingInterceptor()
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://in2000-proxy.ifi.uio.no/alpacaapi/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val retrofitxml = Retrofit.Builder()
        .baseUrl("https://in2000-proxy.ifi.uio.no/alpacaapi/")
        .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
        .client(client)
        .build()

    val api = retrofit.create(DataSource::class.java)
    val api2 = retrofitxml.create(DataSource::class.java)

    suspend fun getParties(): Flow<List<AlpacaParty>?> {
        return flow {
            val result = api.getAlpacaparties().parties
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getVotesD3(): Flow<List<VotesXml>?> {
        return flow {

            val result = api2.getVotesD3().parties
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getVotes(id: Int): Flow<List<Vote>?> {
        return flow {
            val result = api.getVotes(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

}