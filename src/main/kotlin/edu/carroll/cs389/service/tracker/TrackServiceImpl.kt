package edu.carroll.cs389.service.tracker

import edu.carroll.cs389.jpa.model.TrackedUser
import edu.carroll.cs389.jpa.repo.TrackerRepo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**
 * Implementation of TrackService which takes raw (not passed through a load balancer or proxy)
 * Uses browscap library to process user-agent information from servlet requests
 * @see TrackService
 *
 * @param trackerRepo: JPA repository that allows search by client ipv4 address
 * @see edu.carroll.cs389.jpa.repo.TrackerRepo
 */
@Service
class TrackServiceImpl(private val trackerRepo: TrackerRepo): TrackService {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(TrackServiceImpl::class.java)
    }

    /**
     * Function to parse user-agent from the passed in request and save information to the attached database in the
     * form of a TrackedUser
     * @see edu.carroll.cs389.jpa.model.TrackedUser
     *
     * @param ipv4: String representation of the connecting client ipv4 address
     * @param browser: Name of the browser from the connecting client
     * @param browserMajorVersion: Version of the browser from the connecting client
     * @param platform: Operating System of the connecting client
     * @param platformVersion: Version of the connecting operating system
     * @param uri: the subdomain the connecting client is trying to access
     *
     * @return A boolean representing success in saving the information to the database
     */
    override fun trackClient(
        ipv4: String?,
        browser: String?,
        browserMajorVersion: String?,
        platform: String?,
        platformVersion: String?,
        uri: String?): Boolean {
        // if we are missing and ipv4 or uri then abort
        if (ipv4 == null || ipv4 == "Unknown" || ipv4 == "") {
            log.error("trackClient: client does not have a valid connecting ipv4 address, request invalid, aborting")
            return false
        }
        if (uri == null || uri == "Unknown" || uri == "") {
            log.error("trackClient: client is attempting to connect to a page that does not exist, request invalid, aborting")
            return false
        }

        // if we have a browser and corresponding version
        var saveBrowser: String? = null
        if (browser != null && browser != "" && browser != "Unknown" &&
                (browserMajorVersion != null && browserMajorVersion != "Unknown" && browserMajorVersion != "")) {
            log.debug("trackClient: $ipv4 successfully acquired browser info")
            saveBrowser = "$browser $browserMajorVersion"
        }
        // if we have a browser but not a corresponding version
        else if (browser != null && browser != "" && browser != "Unknown") {
            log.warn("trackClient: $ipv4 client browser missing version tag")
            saveBrowser = browser
        } else {
            log.warn("trackClient: $ipv4 client has no browser info")
        }

        // null-check and format operating system string (java library has potential to return null)
        var os: String? = null
        // if we have a platform and corresponding version
        if (platform != null && platform != "" && platform != "Unknown" &&
            (platformVersion != null && platformVersion != "Unknown" && platformVersion != "")) {
            log.debug("trackClient: $ipv4 successfully acquired OS info")
            os = "$platform $platformVersion"
        }
        // if we have a platform but not a corresponding version
        else if (platform != null && platform != "" && platform != "Unknown") {
            log.warn("trackClient: $ipv4 client OS missing version tag")
            os = platform
        } else {
            log.warn("trackClient: $ipv4 client has no OS info")
        }

        // save trackedUser to DB with potentially null val for OS/Browser
        val user: TrackedUser = TrackedUser(ipv4, os, saveBrowser, uri)
        log.debug("trackClient: Successfully created TrackedUser entity for client $ipv4")
        return try {
            trackerRepo.save(user)
            log.info("trackClient: Successfully tracked $ipv4")
            true
        } catch (e: Exception) {
            log.error("trackClient: Failed to save client info to database")
            false
        }
    }

    /**
     * Method which abstracts database access through a service so that controllers are not directly accessing data.
     *
     * @param ipv4: The ipv4 address to search for within the database. Leave blank to get all.
     *
     * @return A list of all entries in the database matching the search term
     */
    override fun query(ipv4: String?): List<TrackedUser> {
        return if (ipv4 == "default" || ipv4 == "" || ipv4 == null) {
            trackerRepo.findAll(Sort.by(Sort.Direction.DESC, "ClientConnectionAttemptTimestamp"))
        } else {
            trackerRepo.findByClientIpv4AddressOrderByClientConnectionAttemptTimestampDesc(ipv4)
        }
    }
}