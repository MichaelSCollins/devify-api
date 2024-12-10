package mike.dev

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("mike.dev")
@ComponentScan("Auth")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}