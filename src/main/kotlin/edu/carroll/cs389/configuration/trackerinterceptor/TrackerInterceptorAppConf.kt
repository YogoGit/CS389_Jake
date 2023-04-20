package edu.carroll.cs389.configuration.trackerinterceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Configuration class which registers the application interceptor as a valid interceptor to Spring
 * CLass is not specifically instantiated, rather is generated at runtime by springboot
 *
 * @param trackerInterceptor: injected object of type TrackerInterceptor used to register the interceptor to the app
 */
@Component
class TrackerInterceptorAppConf(private val trackerInterceptor: TrackerInterceptor) : WebMvcConfigurer {
    /**
     * Override function to add interceptors to the springboot application
     * function is not specifically called, rather called at runtime by springboot
     *
     * @param registry: A springboot interceptor registry object to be used with the application for registering
     *                  interceptors
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(trackerInterceptor)
    }
}