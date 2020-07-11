package com.dash.controller

import com.dash.entity.Tab
import com.dash.repository.TabDataset
import com.dash.repository.TabRepository
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TabDataset
@ExtendWith(SpringExtension::class)
class TabControllerTests(@Autowired val tabRepository: TabRepository) {

    @LocalServerPort
    private val port: Int = 0

    @Test
    fun kotlin_rest_assured_example() {
        given().port(port)
                .`when`()
                .get("/tab/")
                .then().log().all()
                .statusCode(200)
                .log().all()
                .body("size", equalTo(1))
    }

    @Test
    fun addUpdateDeleteTab() {
        val newTab = Tab(null, "LabelTest")

        val insertedTab: Tab = given().port(port)
                .contentType(ContentType.JSON)
                .`when`()
                .body(newTab)
                .post("/tab/addTab/")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(Tab::class.java)

        assertNotNull(insertedTab.id)
        assertEquals(insertedTab.label, newTab.label)

        given().port(port)
                .`when`()
                .get("/tab/")
                .then().log().all()
                .statusCode(200)
                .log().all()
                .body("size", equalTo(2))

        insertedTab.label = "Updated label"

        val updatedTab: Tab = given().port(port)
                .contentType(ContentType.JSON)
                .`when`()
                .body(insertedTab)
                .post("/tab/updateTab/")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(Tab::class.java)

        assertNotNull(updatedTab.id)
        assertEquals(updatedTab.label, insertedTab.label)

        given().port(port)
                .contentType(ContentType.JSON)
                .`when`()
                .param("id", updatedTab.id)
                .delete("/tab/deleteTab/")
                .then().log().all()
                .statusCode(200)

        given().port(port)
                .`when`()
                .get("/tab/")
                .then().log().all()
                .statusCode(200)
                .log().all()
                .body("size", equalTo(1))
    }
}