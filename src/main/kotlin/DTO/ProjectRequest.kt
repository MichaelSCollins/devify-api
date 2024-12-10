package mike.dev.DTO

import mike.dev.enum.DemoPlan
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "project_requests")
data class ProjectRequest (
    @Id val id: UUID = UUID.randomUUID(),
    val userId: String, // Ensure this field exists
    val title: String? = null,
    val description: String? = null,
    val mvp: String? = null,
    val deadline: Date? = null,
    val requestDate: Date? = Date(),
    val step: Number = 0,
    val highestVisitedStep: Number = 0,
    var pricePlan: Number? = null
//    val platforms: List<AppPlatform>? = null
)