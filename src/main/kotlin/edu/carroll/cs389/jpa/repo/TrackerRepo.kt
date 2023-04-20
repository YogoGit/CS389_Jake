package edu.carroll.cs389.jpa.repo

import edu.carroll.cs389.jpa.model.TrackedUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Extension of JpaRepository interface
 *
 * Allows for searching by fields of this application's database tables.
 */
@Repository
interface TrackerRepo : JpaRepository<TrackedUser, Int> {
    /**
     * JPA Magic to define a function which will search the attached database by ipv4_address
     *
     * @param ipv4: The ipv4 address we would like to search the database for
     *
     * @return A list of all TrackedUser entities in the database which match the search condition
     * @see edu.carroll.cs389.jpa.model.TrackedUser
     */
    fun findByClientIpv4AddressOrderByClientConnectionAttemptTimestampDesc(ipv4: String): List<TrackedUser>

    /**
     * JPA Magic to define a function which will search the attached database by the page the client lands on
     *
     * @param requestedPage: The subdomain we would like to search the database for
     *
     * @return A list of all TrackedUser entities in the database which match the search condition
     * @see edu.carroll.cs389.jpa.model.TrackedUser
     */
    fun findByClientConnectionRequestedPage(requestedPage: String): List<TrackedUser>
}