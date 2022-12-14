package com.dash.domain.service

import com.common.utils.AbstractIT
import com.dash.infra.api.response.AirParifApiResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestTemplate
import java.net.URI

@SpringBootTest
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AirParifWidgetServiceTest : AbstractIT() {

    @Autowired
    private lateinit var airParifWidgetService: AirParifWidgetService

    private lateinit var mockServer: MockRestServiceServer

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @BeforeAll
    fun setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @BeforeEach
    fun resetMockServer() {
        mockServer.reset()
    }

    @Test
    fun testGetRequest() {
        val communeInseeCode = "75101"

        val mockedResponse = AirParifApiResponse.airParifForecastResponse(communeInseeCode)

        mockServer.expect(ExpectedCount.once(), requestTo(URI("https://api.airparif.asso.fr/indices/prevision/commune?insee=$communeInseeCode")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON).body(mockedResponse)
            )
        val actualResponse = airParifWidgetService.getPrevisionCommune(communeInseeCode)
        assertEquals(2, actualResponse.size)
        mockServer.verify()
    }
}
