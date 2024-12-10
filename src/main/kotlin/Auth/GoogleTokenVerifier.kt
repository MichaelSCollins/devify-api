package mike.dev.jwt

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory.getDefaultInstance

class GoogleTokenVerifier {
    private val transport = NetHttpTransport()
    private val jsonFactory = getDefaultInstance()

    fun verifyToken(token: String, clientId: String): GoogleIdToken? {
        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(listOf(clientId))
            .build()

        return verifier.verify(token)
    }
}