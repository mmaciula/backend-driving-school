package pl.superjazda.drivingschool.security


import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Shared
import spock.lang.Specification

import java.util.logging.Logger

class JwtTokenUtilTest extends Specification {
    @Shared
    def user = new JwtUserDetails("joe123", "joe@domain.com", "password")
    @Shared
    def tokenUtil = new JwtTokenUtil()

    def "generate token from user details"() {
        ReflectionTestUtils.setField(tokenUtil, "secret", "mySecret")
        ReflectionTestUtils.setField(tokenUtil, "expiration", 36000000)
        def logger = Logger.getLogger("")

        expect:
            logger.info(tokenUtil.generateToken(user))
    }

    def "getting username from token"() {
        given:
            ReflectionTestUtils.setField(tokenUtil, "secret", "mySecret")
            ReflectionTestUtils.setField(tokenUtil, "expiration", 36000000)
            def token = tokenUtil.generateToken(user)

        when:
            def username = tokenUtil.getUsernameFromToken(token)

        then:
            username == "joe123"
    }
}
