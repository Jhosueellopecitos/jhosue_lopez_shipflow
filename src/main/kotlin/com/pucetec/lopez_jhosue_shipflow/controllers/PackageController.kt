package com.pucetec.lopez_jhosue_shipflow.controllers


import com.pucetec.lopez_jhosue_shipflow.models.entities.Package
import com.pucetec.lopez_jhosue_shipflow.models.entities.Status
import com.pucetec.lopez_jhosue_shipflow.request.PackageRequest
import com.pucetec.lopez_jhosue_shipflow.services.PackageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/packages")
class PackageController(
    private val shipmentService: PackageService
) {

    @PostMapping
    fun createPackage(@RequestBody req: PackageRequest): ResponseEntity<Any> {
        return try {
            val pkg = Package(
                type = req.type,
                weight = req.weight,
                description = req.description,
                cityFrom = req.cityFrom,
                cityTo = req.cityTo
            )
            val created = shipmentService.createPackage(pkg)
            ResponseEntity.ok(created)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }


    @GetMapping("/{trackingId}")
    fun getPackage(@PathVariable trackingId: String): ResponseEntity<Any> {
        return try {
            val pkg = shipmentService.getPackageByTracking(trackingId)
            ResponseEntity.ok(pkg)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{trackingId}/status")
    fun updateStatus(
        @PathVariable trackingId: String,
        @RequestParam status: Status,
        @RequestParam(required = false) comment: String?
    ): ResponseEntity<Any> {
        return try {
            shipmentService.updateStatus(trackingId, status, comment)
            ResponseEntity.ok(mapOf("message" to "Estado actualizado exitosamente"))
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
    @GetMapping("/{trackingId}/history")
    fun getHistory(@PathVariable trackingId: String): ResponseEntity<Any> {
        return try {
            val historyDtoList = shipmentService.getHistory(trackingId)
            ResponseEntity.ok(historyDtoList)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}
