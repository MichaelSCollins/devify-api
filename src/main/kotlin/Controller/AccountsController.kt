package mike.dev.Controller

import jakarta.annotation.security.PermitAll
import mike.dev.Config.ContentConfig
import mike.dev.Service.UserAccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import mike.dev.DTO.*
import mike.dev.Util.HttpAuthorizationUtil
import mike.dev.jwt.JwtUtil
import org.springframework.beans.factory.annotation.Autowired

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/account")
@PermitAll
class AccountsController(
    val jwtUtil: JwtUtil,
    val contentConfig: ContentConfig,
    val userAccountService: UserAccountService) {
    @Autowired
    lateinit var httpAuthorizationUtil: HttpAuthorizationUtil;
    @PostMapping
    @PermitAll
    @CrossOrigin(origins = ["http://localhost:3000", "*"])
    fun create(@RequestBody request: RegisterUserRequest): ResponseEntity<UserAccount> {
        val user = userAccountService.registerUser(request)
        return ResponseEntity.ok(user)
    }

    @PermitAll
    @PostMapping("/login")
    @CrossOrigin(origins = ["http://localhost:3000", "*"])
    fun loginUser(@RequestBody request: LoginUserRequest): ResponseEntity<String> {
        val user = userAccountService.loginUser(request)
        return if (user != null) {
            val token = jwtUtil.generateToken(user)
            if(token.isNotBlank()) {
                return ResponseEntity.ok(token)
            }
            ResponseEntity.unprocessableEntity().body(contentConfig.failedTokenMessage)
        } else {
            ResponseEntity.status(401).build()
        }
    }

    @CrossOrigin(origins = ["http://localhost:3000", "*"])
    @GetMapping("/user-info")
    fun getInfoFromToken(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<UserAccount> {
        val user = jwtUtil.extractUser(authorization) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @CrossOrigin(origins = ["http://localhost:3000", "*"])
    @PostMapping("/token-login")
    fun loginTokenUser(@RequestBody request: LoginGoogleUserRequest): ResponseEntity<UserAccount> {
        if(request.username == null)
            return ResponseEntity.badRequest().build()
        val valid = jwtUtil.validateToken(request.token, request.username)
        return if (valid) {
            val decodedUser = userAccountService.decodeJwtToken(request.token)
            return ResponseEntity.ok(decodedUser)
        } else {
            ResponseEntity.status(401).build()
        }
    }

    @PostMapping("/token")
    fun getAuthToken(
        @RequestBody request: LoginUserRequest
    ): ResponseEntity<String>? {
        val user = userAccountService.loginUser(request)
        return if (user != null) {
            val token = jwtUtil.generateToken(user)
            ResponseEntity.ok(token)
        } else {
            ResponseEntity.status(401).build()
        }
    }
}