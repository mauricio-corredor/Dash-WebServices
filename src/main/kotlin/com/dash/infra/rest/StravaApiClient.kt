package com.dash.infra.rest

import com.dash.infra.apimodel.strava.StravaActivityResponse
import com.dash.infra.apimodel.strava.StravaAthleteResponse
import com.dash.infra.apimodel.strava.StravaTokenDataResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.util.*

@Component
class StravaApiClient {

    @Autowired
    private lateinit var restClient: RestClient

    @Value("\${dash.app.STRAVA_API_URL}")
    private lateinit var stravaApiUrl: String

    @Value("\${dash.app.STRAVA_CLIENT_ID}")
    private lateinit var stravaClientId: String

    @Value("\${dash.app.STRAVA_CLIENT_SECRET}")
    private lateinit var stravaClientSecret: String

    fun getToken(apiCode: String): StravaTokenDataResponse? {
        val url = "$stravaApiUrl/oauth/token?client_id=$stravaClientId&client_secret=$stravaClientSecret" +
            "&code=$apiCode&grant_type=authorization_code"
        return restClient.postDataFromProxy(url, mapOf<String, Any>(), StravaTokenDataResponse::class)
    }

    fun getRefreshToken(refreshToken: String): StravaTokenDataResponse? {
        val url = "$stravaApiUrl/oauth/token?client_id=$stravaClientId&client_secret=$stravaClientSecret" +
            "&refresh_token=$refreshToken&grant_type=refresh_token"
        return restClient.postDataFromProxy(url, mapOf<String, Any>(), StravaTokenDataResponse::class)
    }

    fun getAthleteData(token: String): StravaAthleteResponse? {
        val url = "$stravaApiUrl/api/v3/athlete"
        val httpEntity = HttpEntity<StravaAthleteResponse>(getHeaders(token))
        return restClient.getDataFromProxy(url, StravaAthleteResponse::class, httpEntity)
    }

    fun getAthleteActivities(token: String, numberOfActivities: Int): List<StravaActivityResponse>? {
        val url = "$stravaApiUrl/api/v3/athlete/activities?page=1&per_page=$numberOfActivities"
        val httpEntity = HttpEntity<List<StravaActivityResponse>>(getHeaders(token))
        val typeReference = object : ParameterizedTypeReference<List<StravaActivityResponse>>() {}
        return restClient.getDataFromProxy(url, typeReference, httpEntity)
    }

    private fun getHeaders(token: String): HttpHeaders {
        val requestHeaders = HttpHeaders()
        requestHeaders.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        requestHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer $token")
        return requestHeaders
    }
}
