package mike.dev.Service

import mike.dev.DTO.UserAccount
import mike.dev.Repository.UserAccountRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class UserAccountOAuth2Service(
    private val userAccountRepository: UserAccountRepository
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oauth2User = this.loadUser(userRequest)
        val authorities = oauth2User.authorities.map { SimpleGrantedAuthority(it.authority) }

        val googleId = oauth2User.getAttribute<String>("sub") as String?
        val userName = oauth2User.getAttribute("name") as String?
        val email = oauth2User.getAttribute("email") as String?
        if(userName.isNullOrBlank() || email.isNullOrBlank() || googleId.isNullOrBlank()) {
            throw Error()
        }
        val user = userAccountRepository.findByGoogleId(googleId)
            ?: UserAccount(
                username = userName,
                email = email,
                googleId = googleId
            )
        userAccountRepository.save(user)

        return DefaultOAuth2User(authorities, oauth2User.attributes, "sub")
    }
}