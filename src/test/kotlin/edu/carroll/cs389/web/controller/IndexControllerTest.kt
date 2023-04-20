package edu.carroll.cs389.web.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors.assertTrue
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete

@SpringBootTest(classes = [IndexController::class])
@AutoConfigureMockMvc
class IndexControllerTest {

    @Autowired
    private lateinit var idxMvc: MockMvc

    /*************************************/
    /* Happy Path Tests (expected input) */
    /*************************************/

    @Test
    fun validGetMethodToIndexTest() {
        val uri: String = "/"

        val result: MvcResult = idxMvc.perform(get(uri)).andReturn()
        assertTrue(
            "invalidGetMethodToIndexTest(): should be 200 when valid URI",
            result.response.status == 200
        )
    }

    @Test
    fun invalidGetMethodToIndexTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = idxMvc.perform(get(uri)).andReturn()
        assertTrue(
            "invalidGetMethodToIndexTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    /***********************************************/
    /* Crappy Path Tests (unexpected or bad input) */
    /***********************************************/

    @Test
    fun validPostMethodToIndexTest() {
        val uri: String = "/"

        val result: MvcResult = idxMvc.perform(post(uri)).andReturn()
        assertTrue(
            "invalidPostMethodToIndexTest(): should not be 200 when valid URI",
            result.response.status != 200
        )
    }

    @Test
    fun invalidPostMethodToIndexTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = idxMvc.perform(post(uri)).andReturn()
        assertTrue(
            "invalidPostMethodToIndexTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    @Test
    fun validPutMethodToIndexTest() {
        val uri: String = "/"

        val result: MvcResult = idxMvc.perform(put(uri)).andReturn()
        assertTrue(
            "invalidPutMethodToIndexTest(): should not be 200 when valid URI",
            result.response.status != 200
        )
    }

    @Test
    fun invalidPutMethodToIndexTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = idxMvc.perform(put(uri)).andReturn()
        assertTrue(
            "invalidPutMethodToIndexTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    @Test
    fun validDeleteMethodToIndexTest() {
        val uri: String = "/"

        val result: MvcResult = idxMvc.perform(delete(uri)).andReturn()
        assertTrue(
            "invalidDeleteMethodToIndexTest(): should not be 200 when valid URI",
            result.response.status != 200
        )
    }

    @Test
    fun invalidDeleteMethodToIndexTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = idxMvc.perform(delete(uri)).andReturn()
        assertTrue(
            "invalidDeleteMethodToIndexTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    /******************************************************/
    /* Crazy Path Tests (wildly wrong or malicious input) */
    /******************************************************/

    @Test
    fun nullUriToIndexTest() {
        val uri: String = ""

        val result: MvcResult = idxMvc.perform(get(uri)).andReturn()
        assertTrue(
            "invalidDeleteMethodToIndexTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    @Test
    fun sqlStatementAsParameterToIndexTest() {
        val uri: String = "/?';select * from tracked_users;'"

        val result: MvcResult = idxMvc.perform(delete(uri)).andReturn()
        assertTrue(
            "invalidDeleteMethodToIndexTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }
}