package com.zachaczcompany.zzpj

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoadContextSpec extends Specification {

    @Autowired
    private ApplicationContext context

    def "context should not be null"() {
        expect: "context was created"
        context
    }
}
