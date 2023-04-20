package edu.carroll.cs389.web.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * HTML Controller to process non-specific requests (those which do not fall into a particular category within the site)
 * Currently only processes requests to the homepage as that is the only page besides the database query page.
 */
@Controller
class IndexController() {
    companion object{
        val log: Logger = LoggerFactory.getLogger(IndexController::class.java)
    }

    /**
     * GetMapping function which returns the html template for the home page.
     *
     * @return an html template for the "/" domain
     */
    @GetMapping("/")
    fun index(): String {
        log.info("index(): User landed at index page ('/')")
        return "index"
    }
}