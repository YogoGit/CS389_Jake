package edu.carroll.cs389.configuration.trackerinterceptor

import com.blueconic.browscap.BrowsCapField
import com.blueconic.browscap.Capabilities
import com.blueconic.browscap.UserAgentParser
import com.blueconic.browscap.UserAgentService
import edu.carroll.cs389.configuration.trackerinterceptor.TrackerInterceptorProperties
import edu.carroll.cs389.service.tracker.TrackService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Interceptor which handles client tracking outside of HTML Controllers
 * Utilizes the bluconic browscap library to capture information from User-Agent field of request
 * @see com.blueconic.browscap
 *
 * @param trackerService: Injected TrackService implementation which will be used to track client information upon
 *                        intercepting request
 * @see edu.carroll.cs389.service.tracker.TrackService
 *
 * @param trackerInterceptorProperties: Injected TrackerInterceptorProperties object which allows reference to custom
 *                                      application.properties configurations related to request tracking
 * @see edu.carroll.cs389.configuration.TrackerInterceptorProperties
 */
@Component
class TrackerInterceptor(
    private val trackerService: TrackService,
    private val trackerInterceptorProperties: TrackerInterceptorProperties
) : HandlerInterceptor {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(TrackerInterceptor::class.java)

        private val userAgentParser: UserAgentParser = UserAgentService().loadParser(
            listOf(
                BrowsCapField.BROWSER,
                BrowsCapField.BROWSER_TYPE,
                BrowsCapField.BROWSER_MAJOR_VERSION,
                BrowsCapField.PLATFORM,
                BrowsCapField.PLATFORM_VERSION
            )
        )
    }

    /**
     * Override of the preHandle function to handle client tracking
     *
     * Tracks client requests excluding those to the "/favicon.ico" subdomain and conditionally the "/error" subdomain
     * based on the properties of the values associated with the trackerInterceptorProperties object passed at object
     * construction.
     *
     * @return Boolean representing whether the client is connecting to a valid page from a valid ipv4 address
     */
    override fun preHandle(req: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.debug("preHandle: PreHandle tracking initiated")
        // ignore favicon requests
        if (req.requestURI == "/favicon.ico") {
            log.debug("preHandle: Ignoring tracking request to favicon")
        }
        // ignore error requests based on configurations read and passed at runtime
        else if (req.requestURI == "/error" && this.trackerInterceptorProperties.ignoreErrorPageWhenTracking) {
            log.debug("preHandle: Ignoring tracking request to error page")
        }
        // ignore requests to css and js
        else if ("css" in req.requestURI || "js" in req.requestURI || "vendor" in req.requestURI || "image" in req.requestURI) {
            log.debug("preHandle: Ignoring tracking request to static content")
        }
        // log any other request
        else {
            log.debug("preHandle: Tracking request for ${req.requestURI} being processed")
            lateinit var clientInfo: Capabilities
            var ipv4: String? = "Unknown"
            var uri: String? = "Unknown"
            try {
                log.debug("preHandle(): Attempting to acquire client request information")
                clientInfo = userAgentParser.parse(req.getHeader("User-Agent"))
                ipv4 = if (req.remoteAddr == "0:0:0:0:0:0:0:1") {
                    "127.0.0.1"
                } else {
                    req.remoteAddr
                }
                uri = req.requestURI
            } catch (e: Exception) {
                log.error("preHandle(): failed to parse client information")
                return false
            }
            trackerService.trackClient(
                ipv4,
                clientInfo.browser,
                clientInfo.browserMajorVersion,
                clientInfo.platform,
                clientInfo.platformVersion,
                uri
            )
        }
        log.debug("preHandle: PreHandle tracking concluded")
        return true
    }
}