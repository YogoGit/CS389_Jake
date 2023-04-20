package edu.carroll.cs389.jpa.model

import jakarta.persistence.*
import java.io.Serializable
import java.sql.Timestamp
import java.util.*

/**
 * Injectable runtime object which specifies the format of data to store in our connected tracker database.
 */
@Entity
@Table(name = "TrackedUsers")
open class TrackedUser() : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
        private val VALID_OS: List<String> = listOf<String>("Windows", "Mac OS", "Unix-Based", "iOS", "android")
        private val VALID_BROWSER: List<String> = listOf<String>()
    }

    /**
     * Convenience constructor for TrackedUser
     *
     * @param ipv4: String format of the client's ipv4 address
     * @param os: String format of the client's operating system information (including major version)
     * @param browser: String format of the client's browser information (including major version)
     * @param url: String format of the url within this webapp's domain (ex: "/index" not "https://...")
     *
     * @return A fully initialized instance of a TrackedUser object
     */
    constructor(ipv4: String, os: String?, browser: String?, url: String) : this() {
        this.clientIpv4Address = ipv4
        if (os != null) {
            this.clientOperatingSystem = os
        }
        if (browser != null) {
            this.clientBrowserInfo = browser
        }
        this.clientConnectionRequestedPage = url

        this.clientConnectionAttemptTimestamp = Timestamp(Date().time)
    }

    @Id
    @GeneratedValue
    private var id: Int? = null

    @Column(name = "ipv4_address", nullable = false)
    private lateinit var clientIpv4Address: String

    @Column(name = "operating_system")
    private var clientOperatingSystem: String = "unknown"

    @Column(name = "browser")
    private var clientBrowserInfo: String = "unknown"

    @Column(name = "timestamp", nullable = false)
    private lateinit var clientConnectionAttemptTimestamp: Timestamp

    @Column(name = "url_visited", nullable = false)
    private lateinit var clientConnectionRequestedPage: String

    fun clientIpv4Address(): String {return clientIpv4Address}
    fun clientOperatingSystem(): String {return clientOperatingSystem}
    fun clientBrowserInfo(): String {return clientBrowserInfo}
    fun clientConnectionAttemptTimestamp(): Timestamp {return clientConnectionAttemptTimestamp}
    fun clientConnectionRequestedPage(): String {return clientConnectionRequestedPage}

    /**
     * Override of the java object equals function for a TrackedUser
     *
     * @param other: Any object we wish to compare to the calling instance of TrackedUser
     *
     * @return Boolean whether this and other are considered equal
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || other::class != this::class) {
            return false
        }
        val trackedUser = other as TrackedUser
        return clientIpv4Address == trackedUser.clientIpv4Address &&
                clientBrowserInfo == trackedUser.clientBrowserInfo &&
                clientOperatingSystem == trackedUser.clientOperatingSystem &&
                clientConnectionAttemptTimestamp == trackedUser.clientConnectionAttemptTimestamp &&
                clientConnectionRequestedPage == trackedUser.clientConnectionRequestedPage
    }

    /**
     * Override of java object hashcode function for a TrackedUser
     *
     * @return Int unique hash for this instance of TrackedUser
     */
    override fun hashCode(): Int {
        return Objects.hash(clientIpv4Address, clientConnectionAttemptTimestamp, clientConnectionRequestedPage)
    }

    /**
     * Override of java object toString function for a TrackedUser
     *
     * @return String statement of client information in an easily human-readable format
     */
    override fun toString(): String {
        return "Client $clientIpv4Address accessed page $clientConnectionRequestedPage from $clientBrowserInfo running on $clientOperatingSystem at $clientConnectionAttemptTimestamp."
    }
}