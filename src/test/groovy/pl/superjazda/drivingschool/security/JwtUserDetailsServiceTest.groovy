package pl.superjazda.drivingschool.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pl.superjazda.drivingschool.exception.UserNotFoundException
import pl.superjazda.drivingschool.jwt.JwtUserDetailsService
import pl.superjazda.drivingschool.user.User
import pl.superjazda.drivingschool.user.UserRepository
import spock.lang.Specification

@DataJpaTest
class JwtUserDetailsServiceTest extends Specification {
    @Autowired
    private UserRepository userRepository;
    private User testUser = new User("joedoe", "joe@domain.com", "secret_password", "Joe", "Doe")

    def "should find user by username in repository"() {
        def savedUser = userRepository.save(testUser)

        when: "load user entity by 'joedoe' username"
            def foundUser = userRepository.findByUsername("joedoe")

        then: "loaded entity is correct"
            foundUser.get().username == "joedoe"
    }

    def "should load user by username"() {
        given:
            userRepository.save(testUser)
            def service = new JwtUserDetailsService(userRepository)

        when:
            def foundUser = service.loadUserByUsername("joedoe")

        then:
            foundUser.username == "joedoe"
    }

    def "should throw UsernameNotFoundException when try to load non-existing user"() {
        given:
            userRepository.save(testUser)
            def service = new JwtUserDetailsService(userRepository)

        when:
            service.loadUserByUsername("user")

        then:
            thrown(UserNotFoundException.class)
    }
}
