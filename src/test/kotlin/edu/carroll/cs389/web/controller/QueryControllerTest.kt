package edu.carroll.cs389.web.controller

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@Disabled
@SpringBootTest(classes = [QueryController::class])
@AutoConfigureMockMvc
class QueryControllerTest {

    @Autowired
    private lateinit var dbMvc: MockMvc

    /*************************************/
    /* Happy Path Tests (expected input) */
    /*************************************/

    @Test
    fun validGetMethodToDatastoreQueryTest() {
        val uri: String = "/ip_info"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidGetMethodToDatastoreQueryTest(): should be 200 when valid URI",
            result.response.status == 200
        )
    }

    @Test
    fun invalidGetMethodToDatastoreQueryTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidGetMethodToDatastoreQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    @Test
    fun validPostMethodToDatastoreQueryTest() {
        val uri: String = "/ip_info"
        val searchTerm: String = "127.0.0.1"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.post(uri, searchTerm)).andReturn()
        AssertionErrors.assertTrue(
            "invalidPostMethodToDatastoreQueryTest(): should not be 200 when valid URI",
            result.response.status != 200
        )
    }

    @Test
    fun invalidPostMethodToDatastoreQueryTest() {
        val uri: String = "/this_uri_is_invalid"
        val searchTerm: String = "127.0.0.1"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.post(uri, searchTerm)).andReturn()
        AssertionErrors.assertTrue(
            "invalidPostMethodToDatastoreQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    /***********************************************/
    /* Crappy Path Tests (unexpected or bad input) */
    /***********************************************/

    @Test
    fun validPutMethodToDatastoreQueryTest() {
        val uri: String = "/ip_info"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.put(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidPutMethodToDatastoreQueryTest(): should not be 200 when valid URI",
            result.response.status != 200
        )
    }

    @Test
    fun invalidPutMethodToDatastoreQueryTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.put(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidPutMethodToDatastoreQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    @Test
    fun validDeleteMethodToDatastoreQueryTest() {
        val uri: String = "/ip_info"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidDeleteMethodToDatastoreQueryTest(): should not be 200 when valid URI",
            result.response.status != 200
        )
    }

    @Test
    fun invalidDeleteMethodToDatastoreQueryTest() {
        val uri: String = "/this_uri_is_invalid"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidDeleteMethodToDatastoreQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    /******************************************************/
    /* Crazy Path Tests (wildly wrong or malicious input) */
    /******************************************************/

    @Test
    fun nullUriToDatastoreQueryTest() {
        val uri: String = ""

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidDeleteMethodToDatastoreQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }

    @Test
    fun sqlStatementAsParameterToDatastoreQueryTest() {
        val uri: String = "/?';select * from tracked_users;'"

        val result: MvcResult = dbMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn()
        AssertionErrors.assertTrue(
            "invalidDeleteMethodToDatastoreQueryTest(): should not be 200 when invalid URI",
            result.response.status != 200
        )
    }
}