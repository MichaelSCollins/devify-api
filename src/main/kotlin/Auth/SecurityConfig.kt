package mike.dev.Auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/account",
                        "/account/login"
                    ).permitAll()
                    .anyRequest().permitAll() // Public access for these endpoints
            }
            .cors { it.disable() } // Enable CORS with custom configuration
            .csrf { it.disable() } // Disable CSRF (configure as needed)

        return http.build()
    }


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "*") // Allow localhost:3000 for frontend access
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowCredentials = true // Allows cookies and credentials

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val configuration = CorsConfiguration()
//        configuration.allowedOrigins = Arrays.asList("http://localhost:3000")
//        configuration.allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE")
//        configuration.allowedHeaders = Arrays.asList("Authorization", "Content-Type")
//        val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//        return source
//    }

}

//
//@Configuration
//public class WebConfig : WebMvcConfigurer {
//    @Override
//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//            .allowedOrigins("http://localhost:3000")
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//            .allowCredentials(true);
//    }
//}
