package edu.carroll.cs389.configuration.trackerinterceptor

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Registers configurations for tracker interceptor class to be used in application.properties.
 *
 * ignoreErrorPageWhenTracking: True ignores all requests to the /error domain when attempting to intercept requests.
 *                              False tracks all requests to the /error domain when attempting to intercept requests.
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="application.tracker-interceptor")
class TrackerInterceptorProperties() {
    var ignoreErrorPageWhenTracking: Boolean = false
}