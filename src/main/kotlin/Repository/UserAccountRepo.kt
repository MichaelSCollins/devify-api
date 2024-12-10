package mike.dev.Repository

import mike.dev.DTO.UserAccount
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository : MongoRepository<UserAccount, String> {
    fun findByEmail(email: String): UserAccount?
    fun findByGoogleId(googleId: String): UserAccount?
}