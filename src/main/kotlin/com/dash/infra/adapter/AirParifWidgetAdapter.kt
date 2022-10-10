package com.dash.infra.adapter

import com.dash.infra.rest.RestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.util.*

@Service
class AirParifWidgetAdapter {

    @Autowired
    private lateinit var restClient: RestClient

    @Value("\${dash.app.AIRPARIF_API_TOKEN}")
    private lateinit var airParifToken: String

    companion object {
        private const val AIRPARIF_API_URL = "https://api.airparif.asso.fr/indices/prevision"
        private const val AIRPARIF_API_INSEE_ENDPOINTS = "$AIRPARIF_API_URL/commune"
        private const val AIRPARIF_API_COLORS_ENDPOINTS = "$AIRPARIF_API_URL/couleurs"
    }

    fun getPrevisionCommune(communeInseeCode: String): LinkedHashMap<String, List<LinkedHashMap<String, String>>> {
        val url = "$AIRPARIF_API_INSEE_ENDPOINTS?insee=$communeInseeCode"
        val httpEntity = HttpEntity<LinkedHashMap<*, *>>(getHeaders())
        return restClient.getDataFromProxy(url, LinkedHashMap::class, httpEntity) as LinkedHashMap<String, List<LinkedHashMap<String, String>>>
    }

    fun getColors(): LinkedHashMap<String, String> {
        val url = AIRPARIF_API_COLORS_ENDPOINTS
        val httpEntity = HttpEntity<LinkedHashMap<*, *>>(getHeaders())
        return restClient.getDataFromProxy(url, LinkedHashMap::class, httpEntity) as LinkedHashMap<String, String>
    }

    private fun getHeaders(): HttpHeaders {
        val requestHeaders = HttpHeaders()
        requestHeaders.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        requestHeaders.set("X-Api-Key", airParifToken)
        return requestHeaders
    }
}