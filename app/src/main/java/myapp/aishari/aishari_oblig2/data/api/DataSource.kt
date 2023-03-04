package myapp.aishari.aishari_oblig2.data.api

import myapp.aishari.aishari_oblig2.data.model.AlpacaPartyContainer
import myapp.aishari.aishari_oblig2.data.model.DistrictThree
import myapp.aishari.aishari_oblig2.data.model.Vote
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface DataSource {
    @GET("alpacaparties")
    suspend fun getAlpacaparties(
    ): AlpacaPartyContainer


    @GET("district3")
    suspend fun getVotesD3(
    ): DistrictThree


    @GET("district{id}")
    suspend fun getVotes(
        @Path("id") distID: Int,
    ): List<Vote>

}