package uk.ac.tees.mad.univid.mainscreen.requests

import retrofit2.http.GET
import retrofit2.http.Path
import uk.ac.tees.mad.univid.mainscreen.model.wordsInfoItem

interface ApiService {

    @GET("entries/en/{query}")
    suspend fun getWords(@Path("query") query: String): List<wordsInfoItem>
}