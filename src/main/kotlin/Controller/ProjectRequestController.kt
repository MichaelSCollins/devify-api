package mike.dev.Controller

import mike.dev.DTO.ProjectRequest
import mike.dev.Service.ProjectRequestService
import mike.dev.Util.HttpAuthorizationUtil
import mike.dev.jwt.JwtUtil
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/app-requests")
@CrossOrigin(origins = ["*"])
class ProjectRequestController(
    private val projectRequestService: ProjectRequestService,
    private val jwtUtil: JwtUtil,
    private val httpAuthorizationUtil: HttpAuthorizationUtil,
) {
    @CrossOrigin(origins = ["*"])
    @GetMapping
    fun getAppRequestsForCurrentUser(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<out Any>? {
        val token = httpAuthorizationUtil.removeBearerTag(authorizationHeader)
        val userId = jwtUtil.extractUserId(token)
        if(userId.isNullOrBlank()) {
            return ResponseEntity
                .badRequest()
                .body<Any>("UserID <$userId>: Null or Empty.")
        }
        val appRequests = projectRequestService.findByUserId(userId)
        return ResponseEntity.ok(appRequests)
    }

    @GetMapping("/{id}")
    fun getAppRequestById(@PathVariable id: UUID): ResponseEntity<ProjectRequest> {
        val appRequest = projectRequestService.findById(id)
        return if (appRequest != null) {
            ResponseEntity.ok(appRequest)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    interface PricePlanBody {
        val pricePlan: Number
    }
    @PatchMapping("/{id}/price-plan")
    @CrossOrigin(origins = ["*"])
    fun updatePricePlan(@PathVariable id: UUID, @RequestBody body: PricePlanBody): ResponseEntity<ProjectRequest> {
        try {
            return ResponseEntity.ok(projectRequestService.updatePricePlan(id, body.pricePlan))
        } catch(e: NotFoundException) {
            return ResponseEntity.notFound().build()
        }
    }
    @PostMapping
    @CrossOrigin(origins = ["*"])
    fun createAppRequest(@RequestBody projectRequest: ProjectRequest): ResponseEntity<ProjectRequest> {
        val savedAppRequest = projectRequestService.save(projectRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body   (savedAppRequest)
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = ["*"])
    fun updateAppRequest(
        @PathVariable id: UUID,
        @RequestBody updatedProjectRequest: ProjectRequest
    ): ResponseEntity<Any> {
            val existingAppRequest = projectRequestService.findById(id)
            ?: return ResponseEntity.badRequest().body("Invalid Project UUID")
        return run {
            val appRequestWithId = updatedProjectRequest.copy(id = id)
            val savedAppRequest = projectRequestService.save(appRequestWithId)
            ResponseEntity.ok(savedAppRequest)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteAppRequest(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (projectRequestService.findById(id) != null) {
            projectRequestService.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}