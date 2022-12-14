package com.common.domain.service

import com.common.utils.AbstractIT
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest : AbstractIT() {

    @Autowired
    private lateinit var userService: UserService

    @Test
    fun getUserByIdTest() {
        val user = userService.getUserById(2)
        assertEquals("admintest", user.username)
    }
}
