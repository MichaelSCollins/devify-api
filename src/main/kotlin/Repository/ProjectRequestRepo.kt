package mike.dev.Repository

import mike.dev.DTO.ProjectRequest
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface ProjectRequestRepo: MongoRepository<ProjectRequest, UUID> {
    fun findByUserId(userId: String): List<ProjectRequest>
}