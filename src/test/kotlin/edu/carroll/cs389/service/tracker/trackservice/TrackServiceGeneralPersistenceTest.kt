package edu.carroll.cs389.service.tracker.trackservice

import edu.carroll.cs389.service.tracker.TrackService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.util.AssertionErrors.assertTrue

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TrackServiceGeneralPersistenceTest {

    @Autowired
    private lateinit var trackService: TrackService

    /*****************************************/
    /* General datastore Persistence Testing */
    /*****************************************/

    @Test
    fun datastoreIsEmptyWithoutInsertTest() {
        assertTrue(
            "datastoreIsEmptyWithoutInsertTest(): test datastore (newly instantiated and cleaned after every test" +
                    " should be empty without inserting data",
            trackService.query().isEmpty()
        )
    }

    @Test
    fun singleInsertTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        assertTrue(
            "persistenceOfTwoInsertsTest(): should succeed with valid user agent",
            trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        )

        assertTrue(
            "persistenceOfTwoInsertsTest(): inserted two records, should be of size 2",
            trackService.query().size == 1
        )

        val user = trackService.query()[0]

        assertTrue(
            "persistenceOfTwoInsertsTest(): should query information from second insert first due to timestamp",
            ipv4 == user.clientIpv4Address() &&
                    "$browser $browserMajorVersion" == user.clientBrowserInfo() &&
                    "$platform $platformVersion" == user.clientOperatingSystem() &&
                    requestURI == user.clientConnectionRequestedPage()
        )
    }


    @Test
    fun persistenceOfTwoInsertsTest() {
        val ipv41: String = "192.168.1.1"
        val browser1: String = "Firefox"
        val browserMajorVersion1: String = "110"
        val platform1: String = "Ubuntu"
        val platformVersion1: String = "22.04 LTS"
        val requestURI1: String = "/"

        val ipv42: String = "172.158.27.139"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "37"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"

        assertTrue(
            "persistenceOfTwoInsertsTest(): should succeed with valid user agent",
            trackService.trackClient(ipv41, browser1, browserMajorVersion1, platform1, platformVersion1, requestURI1)
        )

        assertTrue(
            "persistenceOfTwoInsertsTest(): should succeed with valid user agent",
            trackService.trackClient(ipv42, browser2, browserMajorVersion2, platform2, platformVersion2, requestURI2)
        )

        assertTrue(
            "persistenceOfTwoInsertsTest(): inserted two records, should be of size 2",
            trackService.query().size == 2
        )


        // assuming that queryDatabase works correctly (must assume something works)
        // we know the list is ordered by descending timestamp
        val results = trackService.query()
        val user1 = results[1]
        val user2 = results[0]

        assertTrue(
            "persistenceOfTwoInsertsTest(): should query information from second insert first due to timestamp",
            ipv41 == user1.clientIpv4Address() &&
                    "$browser1 $browserMajorVersion1" == user1.clientBrowserInfo() &&
                    "$platform1 $platformVersion1" == user1.clientOperatingSystem() &&
                    requestURI1 == user1.clientConnectionRequestedPage()
        )

        assertTrue(
            "persistenceOfTwoInsertsTest(): should query information from first insert first due to timestamp",
            ipv42 == user2.clientIpv4Address() &&
                    "$browser2 $browserMajorVersion2" == user2.clientBrowserInfo() &&
                    "$platform2 $platformVersion2" == user2.clientOperatingSystem() &&
                    requestURI2 == user2.clientConnectionRequestedPage()
        )

    }

    @Test
    fun varietyOfDataTest() {
        val ipv4s: List<String> = listOf<String>("18.354.563.541", "72.175.63.11", "174.283.69.420", "542.172.378.29")
        val browsers: List<String> = listOf<String>("Firefox", "Opera", "Brave", "Chrome")
        val browserMajorVersions: List<String> = listOf<String>("112", "7", "v1.49.132", "32")
        val platforms: List<String> = listOf<String>("Windows", "macOS", "Ubuntu", "Mint")
        val platformVersions: List<String> = listOf<String>("10 NT", "Catalina", "22.04 LTS", "Cinnamon 14")
        val requests: List<String> = listOf<String>("/", "/dummy_link", "/ip_info", "/bs-override.css")

        for (i in 0..3) {
            assertTrue(
                "varietyOfDataTest(): entry $i should insert properly",
                trackService.trackClient(
                    ipv4s[i], browsers[i], browserMajorVersions[i],
                    platforms[i], platformVersions[i], requests[i]
                )
            )

            assertTrue(
                "varietyOfDataTest(): Db should contain ${i + 1} entries",
                trackService.query().size == i + 1
            )
        }

        val results = trackService.query()

        for (i in 3 downTo 0) {
            val user = results[3-i]
            assertTrue(
                "varietyOfDataTest(): entry $i should be queried properly",
                ipv4s[i] == user.clientIpv4Address() &&
                        "${browsers[i]} ${browserMajorVersions[i]}" == user.clientBrowserInfo() &&
                        "${platforms[i]} ${platformVersions[i]}" == user.clientOperatingSystem() &&
                        requests[i] == user.clientConnectionRequestedPage()
            )
        }
    }
}