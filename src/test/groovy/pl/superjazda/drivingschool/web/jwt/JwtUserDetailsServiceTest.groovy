package pl.superjazda.drivingschool.web.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pl.superjazda.drivingschool.api.user.UserRepository
import pl.superjazda.drivingschool.web.exception.UserNotFoundException
import spock.lang.Specification

@DataJpaTest
class JwtUserDetailsServiceTest extends Specification {
    @Autowired
    private UserRepository userRepository;

    def "should find user by username in repository"() {
        when: "load user entity by 'student' username"
            def foundUser = userRepository.findByUsername("student")

        then: "loaded entity is correct"
            foundUser.get().username == "student"
    }

    def "should load user by username"() {
        given:
            def service = new JwtUserDetailsService(userRepository)

        when:
            def foundUser = service.loadUserByUsername("student")

        then:
            foundUser.username == "student"
    }

    def "should throw UsernameNotFoundException when try to load non-existing user"() {
        given:
            def service = new JwtUserDetailsService(userRepository)

        when:
            service.loadUserByUsername("user")

        then:
            thrown(UserNotFoundException.class)
    }
}
