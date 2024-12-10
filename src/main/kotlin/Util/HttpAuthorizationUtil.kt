package mike.dev.Util

import org.springframework.stereotype.Component

@Component
class HttpAuthorizationUtil {
    fun removeBearerTag(authorizationHeader: String): String {
        val bearerTag = "Bearer "
        return if(authorizationHeader.substring(0, 7) == bearerTag)
            authorizationHeader.removePrefix(bearerTag)
        else
            authorizationHeader
    }
}