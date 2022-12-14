package com.common.utils

import com.common.app.controller.requests.LoginRequest
import com.common.security.response.JwtResponse
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.http.Header
import org.hamcrest.Matchers

object IntegrationTestsUtils {

    fun authenticateAdmin(port: Int): JwtResponse {
        return given()
            .port(port)
            .contentType(ContentType.JSON)
            .`when`()
            .body(LoginRequest("admintest", "adminpassword"))
            .post("/auth/login/")
            .then().log().all()
            .statusCode(200)
            .log().all()
            .body("$", Matchers.notNullValue())
            .extract().`as`(JwtResponse::class.java)
    }

    fun createAuthenticationHeader(jwtToken: String): Header = Header("Authorization", "Bearer $jwtToken")
}
