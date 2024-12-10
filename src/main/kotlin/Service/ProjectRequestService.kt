package mike.dev.Service

import mike.dev.DTO.ProjectRequest
import mike.dev.Repository.ProjectRequestRepo
import mike.dev.enum.DemoPlan
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import java.util.*


@Service
class ProjectRequestService(private val repository: ProjectRequestRepo) {
    fun findById(id: UUID): ProjectRequest? = repository.findById(id).orElse(null)

    fun updatePricePlan(id: UUID, pricePlan: Number): ProjectRequest {
        val projectRequest: ProjectRequest? = repository.findById(id).orElseThrow()
        if (projectRequest != null) {
            projectRequest.pricePlan = pricePlan;
            return repository.save<ProjectRequest>(projectRequest)
        }

        throw(NotFoundException())
    }

    fun findByUserId(userId: String): List<ProjectRequest> = repository.findAll()

    fun save(document: ProjectRequest): ProjectRequest = repository.save(document)

    fun deleteById(id: UUID) = repository.deleteById(id)
}