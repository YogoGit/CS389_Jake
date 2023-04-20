package edu.carroll.cs389.service.tracker.trackservice

import edu.carroll.cs389.service.tracker.TrackService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.util.AssertionErrors.assertTrue
import java.sql.Timestamp
import java.util.Date

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TrackServiceQueryTest {

    @Autowired
    private lateinit var trackService: TrackService

    /*******************************************************/
    /*******************************************************/
    /* THESE TESTS ALL PERTAIN TO THE queryDatabase METHOD */
    /*******************************************************/
    /*******************************************************/

    /*************************************/
    /* Happy Path Tests (expected input) */
    /*************************************/

    @Test
    fun validUserTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        // logging time to be between
        val timeU1i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        Thread.sleep(100)
        val timeU1f = Timestamp(Date().time)

        val results = trackService.query()
        assertTrue(
            "validUserTest(): Tracked user should be equal",
            results.size  == 1
        )

        assertTrue(
            "validTrackedUserTest(): Tracked user should be equal to string information given " +
                    "we set the timestamp of the inserted user to a known value",
            ipv4 == results[0].clientIpv4Address() &&
            "$browser $browserMajorVersion" == results[0].clientBrowserInfo() &&
            "$platform $platformVersion" == results[0].clientOperatingSystem() &&
            requestURI == results[0].clientConnectionRequestedPage() &&
            timeU1i <= results[0].clientConnectionAttemptTimestamp() && 
                    results[0].clientConnectionAttemptTimestamp() <= timeU1f
        )
    }

    @Test
    fun validTwoTrackedUserTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"

        // logging time to be between
        val timeU1i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        Thread.sleep(100)
        val timeU1f = Timestamp(Date().time)
        
        val timeU2i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv42, browser2, browserMajorVersion2, platform2, platformVersion2, requestURI2)
        Thread.sleep(100)
        val timeU2f = Timestamp(Date().time)

        val results = trackService.query()
        assertTrue(
            "validTwoTrackedUserTest(): Tracked user should be equal",
            results.size == 2
        )

        assertTrue(
            "validTwoTrackedUserTest(): Tracked user should be equal to string information given " +
                    "we set the timestamp of the inserted user to a known value",
            ipv4 == results[1].clientIpv4Address() &&
                    "$browser $browserMajorVersion" == results[1].clientBrowserInfo() &&
                    "$platform $platformVersion" == results[1].clientOperatingSystem() &&
                    requestURI == results[1].clientConnectionRequestedPage() &&
                    timeU1i <= results[1].clientConnectionAttemptTimestamp() &&
                    results[1].clientConnectionAttemptTimestamp() <= timeU1f
        )

        assertTrue(
            "validTwoTrackedUserTest(): Tracked user2 should be equal to string information given " +
                    "we set the timestamp of the inserted user to a known value",
            ipv42 == results[0].clientIpv4Address() &&
                    "$browser2 $browserMajorVersion2" == results[0].clientBrowserInfo() &&
                    "$platform2 $platformVersion2" == results[0].clientOperatingSystem() &&
                    requestURI2 == results[0].clientConnectionRequestedPage() &&
                    timeU2i <= results[0].clientConnectionAttemptTimestamp() &&
                    results[0].clientConnectionAttemptTimestamp() <= timeU2f
        )
    }

    @Test
    fun insertThreeAndCheckOrderByTimestampTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"

        val ipv43: String = "214.72.68.138"
        val browser3: String = "Brave"
        val browserMajorVersion3: String = "1.49"
        val platform3: String = "macOS"
        val platformVersion3: String = "Catalina"
        val requestURI3: String = "/bs-override.css"
        
        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        
        Thread.sleep(100)
        trackService.trackClient(ipv42, browser2, browserMajorVersion2, platform2, platformVersion2, requestURI2)
        
        Thread.sleep(100)
        trackService.trackClient(ipv43, browser3, browserMajorVersion3, platform3, platformVersion3, requestURI3)

        val results = trackService.query()

        assertTrue(
            "insertThreeAndCheckOrderByTimestampTest(): Should contain 3 entries",
            results.size == 3
        )

        assertTrue(
            "insertThreeAndCheckOrderByTimestampTest(): Should be in order starting with greatest " +
                    "timestamp and ending with least",
            results[0].clientConnectionAttemptTimestamp() > results[1].clientConnectionAttemptTimestamp() &&
            results[1].clientConnectionAttemptTimestamp() > results[2].clientConnectionAttemptTimestamp()
        )
    }
    
    @Test
    fun insertThreeAndSearchTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"

        val ipv43: String = "214.72.68.138"
        val browser3: String = "Brave"
        val browserMajorVersion3: String = "1.49"
        val platform3: String = "macOS"
        val platformVersion3: String = "Catalina"
        val requestURI3: String = "/bs-override.css"

        // logging time to be between
        val timeU1i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        Thread.sleep(100)
        val timeU1f = Timestamp(Date().time)
        
        trackService.trackClient(ipv42, browser2, browserMajorVersion2, platform2, platformVersion2, requestURI2)
        trackService.trackClient(ipv43, browser3, browserMajorVersion3, platform3, platformVersion3, requestURI3)

        val results = trackService.query("192.168.1.1")

        assertTrue(
            "insertThreeAndSearchTest(): Based on search term, should only have one entry that is returned",
            results.size == 1
        )

        assertTrue(
            "insertThreeAndSearchTest(): Based on search term, returned user should equal user 1 (user)",
            ipv4 == results[0].clientIpv4Address() &&
                    "$browser $browserMajorVersion" == results[0].clientBrowserInfo() &&
                    "$platform $platformVersion" == results[0].clientOperatingSystem() &&
                    requestURI == results[0].clientConnectionRequestedPage() &&
                    timeU1i <= results[0].clientConnectionAttemptTimestamp() &&
                    results[0].clientConnectionAttemptTimestamp() <= timeU1f
        )
    }

    @Test
    fun insertThreeWithTwoSameIPv4AndSearchTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        val ipv42: String = "192.168.1.1"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"

        val ipv43: String = "214.72.68.138"
        val browser3: String = "Brave"
        val browserMajorVersion3: String = "1.49"
        val platform3: String = "macOS"
        val platformVersion3: String = "Catalina"
        val requestURI3: String = "/bs-override.css"

        // logging time to be between
        val timeU1i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        Thread.sleep(100)
        val timeU1f = Timestamp(Date().time)

        val timeU2i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv42, browser2, browserMajorVersion2, platform2, platformVersion2, requestURI2)
        Thread.sleep(100)
        val timeU2f = Timestamp(Date().time)
        
        trackService.trackClient(ipv43, browser3, browserMajorVersion3, platform3, platformVersion3, requestURI3)

        val results = trackService.query("192.168.1.1")

        assertTrue(
            "insertThreeWithTwoSameAndSearchTest(): Should have query size of 2 because 2 have same IP",
            results.size == 2
        )

        assertTrue(
            "insertThreeWithTwoSameAndSearchTest(): Should have query 0 equal user 2 and query 1 equal user 1",
            ipv42 == results[0].clientIpv4Address() &&
                    "$browser2 $browserMajorVersion2" == results[0].clientBrowserInfo() &&
                    "$platform2 $platformVersion2" == results[0].clientOperatingSystem() &&
                    requestURI2 == results[0].clientConnectionRequestedPage() &&
                    timeU2i <= results[0].clientConnectionAttemptTimestamp() &&
                    results[0].clientConnectionAttemptTimestamp() <= timeU2f &&

                    ipv4 == results[1].clientIpv4Address() &&
                    "$browser $browserMajorVersion" == results[1].clientBrowserInfo() &&
                    "$platform $platformVersion" == results[1].clientOperatingSystem() &&
                    requestURI == results[1].clientConnectionRequestedPage() &&
                    timeU1i <= results[1].clientConnectionAttemptTimestamp() &&
                    results[1].clientConnectionAttemptTimestamp() <= timeU1f
        )
    }

    @Test
    fun insertThreeOfSameThenSearchAndCheckOrderByTimestampTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        val ipv42: String = "192.168.1.1"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"

        val ipv43: String = "192.168.1.1"
        val browser3: String = "Brave"
        val browserMajorVersion3: String = "1.49"
        val platform3: String = "macOS"
        val platformVersion3: String = "Catalina"
        val requestURI3: String = "/bs-override.css"

        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)

        Thread.sleep(100)
        trackService.trackClient(ipv42, browser2, browserMajorVersion2, platform2, platformVersion2, requestURI2)

        Thread.sleep(100)
        trackService.trackClient(ipv43, browser3, browserMajorVersion3, platform3, platformVersion3, requestURI3)

        val results = trackService.query("192.168.1.1")

        assertTrue(
            "insertThreeOfSameThenSearchAndCheckOrderByTimestampTest(): Should contain 3 entries",
            results.size == 3
        )

        assertTrue(
            "insertThreeOfSameThenSearchAndCheckOrderByTimestampTest(): Should be in order starting with greatest " +
                    "timestamp and ending with least",
            results[0].clientConnectionAttemptTimestamp() > results[1].clientConnectionAttemptTimestamp() &&
                    results[1].clientConnectionAttemptTimestamp() > results[2].clientConnectionAttemptTimestamp()
        )
    }

    /***********************************************/
    /* Crappy Path Tests (unexpected or bad input) */
    /***********************************************/

    @Test
    fun queryWithNoEntriesTest() {
        val results = trackService.query()

        assertTrue(
            "queryWithNoEntriesTest(): Should have length 0 in results",
            results.isEmpty()
        )
    }

    @Test
    fun searchWithNoEntriesTest() {
        val results = trackService.query("186.128.74.129")

        assertTrue(
            "searchWithNoEntriesTest(): Should have length 0 in results",
            results.isEmpty()
        )
    }

    @Test
    fun searchForEmptyStringTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        val timeU1i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        Thread.sleep(100)
        val timeU1f = Timestamp(Date().time)

        var results = trackService.query("")

        assertTrue(
            "queryWithNoEntriesTest(): Should have all results for empty search (1)",
            results.size == 1
        )

        assertTrue(
            "queryWithNoEntriesTest(): Results should match users according to sort order (1)",
            ipv4 == results[0].clientIpv4Address() &&
                    "$browser $browserMajorVersion" == results[0].clientBrowserInfo() &&
                    "$platform $platformVersion" == results[0].clientOperatingSystem() &&
                    requestURI == results[0].clientConnectionRequestedPage() &&
                    timeU1i <= results[0].clientConnectionAttemptTimestamp() &&
                    results[0].clientConnectionAttemptTimestamp() <= timeU1f
        )

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"

        val timeU2i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv42, browser2, browserMajorVersion2, platform2, platformVersion2, requestURI2)
        Thread.sleep(100)
        val timeU2f = Timestamp(Date().time)

        results = trackService.query("")

        assertTrue(
            "queryWithNoEntriesTest(): Should have all results for empty search (2)",
            results.size == 2
        )

        assertTrue(
            "queryWithNoEntriesTest(): Results should match users according to sort order (2)",
            ipv42 == results[0].clientIpv4Address() &&
                    "$browser2 $browserMajorVersion2" == results[0].clientBrowserInfo() &&
                    "$platform2 $platformVersion2" == results[0].clientOperatingSystem() &&
                    requestURI2 == results[0].clientConnectionRequestedPage() &&
                    timeU2i <= results[0].clientConnectionAttemptTimestamp() &&
                    results[0].clientConnectionAttemptTimestamp() <= timeU2f &&

                    ipv4 == results[1].clientIpv4Address() &&
                    "$browser $browserMajorVersion" == results[1].clientBrowserInfo() &&
                    "$platform $platformVersion" == results[1].clientOperatingSystem() &&
                    requestURI == results[1].clientConnectionRequestedPage() &&
                    timeU1i <= results[1].clientConnectionAttemptTimestamp() &&
                    results[1].clientConnectionAttemptTimestamp() <= timeU1f
        )
    }

    @Test
    fun searchForEntryThatDoesNotExistTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)

        val results = trackService.query("162.187.27.147")

        assertTrue(
            "searchForEntryThatDoesNotExistTest(): when IP does not exist in Db, there should be no result",
            results.isEmpty()
        )
    }


    /******************************************************/
    /* Crazy Path Tests (wildly wrong or malicious input) */
    /******************************************************/

    @Test
    fun searchForNullTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        val timeU1i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        Thread.sleep(100)
        val timeU1f = Timestamp(Date().time)

        var results = trackService.query(null)

        assertTrue(
            "queryWithNoEntriesTest(): Should have all results for empty search (1)",
            results.size == 1
        )

        assertTrue(
            "queryWithNoEntriesTest(): Results should match users according to sort order (1)",
            ipv4 == results[0].clientIpv4Address() &&
                    "$browser $browserMajorVersion" == results[0].clientBrowserInfo() &&
                    "$platform $platformVersion" == results[0].clientOperatingSystem() &&
                    requestURI == results[0].clientConnectionRequestedPage() &&
                    timeU1i <= results[0].clientConnectionAttemptTimestamp() &&
                    results[0].clientConnectionAttemptTimestamp() <= timeU1f
        )

        val ipv42: String = "172.98.272.47"
        val browser2: String = "Chrome"
        val browserMajorVersion2: String = "35"
        val platform2: String = "Windows"
        val platformVersion2: String = "10 NT"
        val requestURI2: String = "/ip_info"

        val timeU2i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv42, browser2, browserMajorVersion2, platform2, platformVersion2, requestURI2)
        Thread.sleep(100)
        val timeU2f = Timestamp(Date().time)

        results = trackService.query(null)

        assertTrue(
            "queryWithNoEntriesTest(): Should have all results for empty search (2)",
            results.size == 2
        )

        assertTrue(
            "queryWithNoEntriesTest(): Results should match users according to sort order (2)",
            ipv42 == results[0].clientIpv4Address() &&
                    "$browser2 $browserMajorVersion2" == results[0].clientBrowserInfo() &&
                    "$platform2 $platformVersion2" == results[0].clientOperatingSystem() &&
                    requestURI2 == results[0].clientConnectionRequestedPage() &&
                    timeU2i <= results[0].clientConnectionAttemptTimestamp() &&
                    results[0].clientConnectionAttemptTimestamp() <= timeU2f &&

                    ipv4 == results[1].clientIpv4Address() &&
                    "$browser $browserMajorVersion" == results[1].clientBrowserInfo() &&
                    "$platform $platformVersion" == results[1].clientOperatingSystem() &&
                    requestURI == results[1].clientConnectionRequestedPage() &&
                    timeU1i <= results[1].clientConnectionAttemptTimestamp() &&
                    results[1].clientConnectionAttemptTimestamp() <= timeU1f
        )
    }

    // test method disabled as there is currently no sanitization in place. this is a task for the later life
    // of the project (pre-deployment but outside the scope of this semester project)
    @Disabled
    @Test
    fun searchForSQLCodeTest() {
        val ipv4: String = "192.168.1.1"
        val browser: String = "Firefox"
        val browserMajorVersion: String = "110"
        val platform: String = "Ubuntu"
        val platformVersion: String = "22.04 LTS"
        val requestURI: String = "/"

        val timeU1i = Timestamp(Date().time)
        Thread.sleep(100)
        trackService.trackClient(ipv4, browser, browserMajorVersion, platform, platformVersion, requestURI)
        Thread.sleep(100)
        val timeU1f = Timestamp(Date().time)

        var results = trackService.query()

        assertTrue(
            "searchForSQLCodeTest(): Should have one datapoint that matches user",
            results.size == 1 &&
                    ipv4 == results[0].clientIpv4Address() &&
                    "$browser $browserMajorVersion" == results[0].clientBrowserInfo() &&
                    "$platform $platformVersion" == results[0].clientOperatingSystem() &&
                    requestURI == results[0].clientConnectionRequestedPage() &&
                    timeU1i <= results[0].clientConnectionAttemptTimestamp() &&
                    results[0].clientConnectionAttemptTimestamp() <= timeU1f
        )

        results = trackService.query("; DELETE FROM tracked_users WHERE *;")

        assertTrue(
            "searchForSQLCodeTest(): should still keep data after searching for a delete statement",
            results.size == 1 &&
                    ipv4 == results[0].clientIpv4Address() &&
                    "$browser $browserMajorVersion" == results[0].clientBrowserInfo() &&
                    "$platform $platformVersion" == results[0].clientOperatingSystem() &&
                    requestURI == results[0].clientConnectionRequestedPage() &&
                    timeU1i <= results[0].clientConnectionAttemptTimestamp() &&
                    results[0].clientConnectionAttemptTimestamp() <= timeU1f
        )
    }

}