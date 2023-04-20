package edu.carroll.cs389.service.tracker

import edu.carroll.cs389.jpa.model.TrackedUser
import jakarta.servlet.http.HttpServletRequest

/**
 * Interface which defines the methods required by a TrackService
 * Indented purpose is to track client connection information when the client accesses any page within the webapp
 *
 * Currently, this class tracks connecting ipv4 address, browser & version, operating system & version, the page the request
 * was directed to, and the timestamp of the request
 */
interface TrackService {
    /**
     * Method which processes incoming requests and converts them into a storable entity in the form of a TrackedUser
     * Once a TrackedUser instance is created from the data in the passed servlet request, it is then saved to the
     * application attached database.
     *
     * @param ipv4: String representation of the connecting client ipv4 address
     * @param browser: Name of the browser from the connecting client
     * @param browserMajorVersion: Version of the browser from the connecting client
     * @param platform: Operating System of the connecting client
     * @param platformVersion: Version of the connecting operating system
     * @param uri: the subdomain the connecting client is trying to access
     *
     * @see edu.carroll.cs389.jpa.model.TrackedUser
     */
    fun trackClient(
        ipv4: String?,
        browser: String?,
        browserMajorVersion: String?,
        platform: String?,
        platformVersion: String?,
        uri: String?
    ): Boolean

    /**
     * Method which abstracts database access through a service so that controllers are not directly accessing data.
     *
     * @param ipv4: The ipv4 address to search for within the database. Leave blank to get all.
     *
     * @return A list of all entries in the database matching the search term
     */
    fun query(ipv4: String? = "default"): List<TrackedUser>
}