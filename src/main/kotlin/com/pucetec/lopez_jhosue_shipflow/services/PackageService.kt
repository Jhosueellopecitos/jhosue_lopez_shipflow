package com.pucetec.lopez_jhosue_shipflow.services


import com.pucetec.lopez_jhosue_shipflow.models.entities.Package
import com.pucetec.lopez_jhosue_shipflow.models.entities.PackageHistory
import com.pucetec.lopez_jhosue_shipflow.models.entities.Status
import com.pucetec.lopez_jhosue_shipflow.repositories.PackageEventRepository
import com.pucetec.lopez_jhosue_shipflow.repositories.PackageRepository
import com.pucetec.lopez_jhosue_shipflow.response.PackageEventResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class PackageService(
    private val packageRepo: PackageRepository,
    private val historyRepo: PackageEventRepository
) {



    fun getPackageByTracking(trackingId: String): Package {
        return packageRepo.findByTrackingId(trackingId)
            ?: throw IllegalArgumentException("Paquete con tracking $trackingId no encontrado")
    }

    @Transactional
    fun createPackage(pkg: Package): Package {
        if (pkg.cityFrom.equals(pkg.cityTo, ignoreCase = true))
            throw IllegalArgumentException("La ciudad de origen y destino no pueden ser iguales")

        if (pkg.description.length > 50)
            throw IllegalArgumentException("La descripción no puede tener más de 50 caracteres")

        if (pkg.weight <= 0)
            throw IllegalArgumentException("El peso debe ser mayor que cero")

        val trackingId = UUID.randomUUID().toString().substring(0, 8).uppercase()
        val createdPkg = pkg.copy(
            status = Status.PENDING,
            estimatedDeliveryDate = LocalDateTime.now().plusDays(5),
            trackingId = trackingId
        )

        return packageRepo.save(createdPkg)
    }


    @Transactional
    fun updateStatus(trackingId: String, newStatus: Status, comment: String?) {
        val shipment = packageRepo.findByTrackingId(trackingId)
            ?: throw IllegalArgumentException("Envío no encontrado")


        val validTransitions = mapOf(
            Status.PENDING to listOf(Status.IN_TRANSIT),
            Status.IN_TRANSIT to listOf(Status.DELIVERED, Status.ON_HOLD, Status.CANCELLED),
            Status.ON_HOLD to listOf(Status.IN_TRANSIT, Status.CANCELLED)
        )

        val current = shipment.status

        if (current == Status.DELIVERED || current == Status.CANCELLED) {
            throw IllegalStateException("El estado $current es final y no puede cambiarse")
        }

        val validNextStates = validTransitions[current] ?: emptyList()

        if (!validNextStates.contains(newStatus) && current != newStatus) {
            throw IllegalStateException("No se puede pasar de $current a $newStatus")
        }

        if (newStatus == Status.DELIVERED) {
            val everInTransit = historyRepo.existsByShipmentAndStatus(shipment, Status.IN_TRANSIT)
            if (!everInTransit && current != Status.IN_TRANSIT) {
                throw IllegalStateException("Solo se puede marcar como entregado si estuvo en tránsito")
            }
        }


        val history = PackageHistory(
            status = newStatus,
            shipment = shipment,
            comment = comment
        )
        historyRepo.save(history)


        shipment.status = newStatus
        packageRepo.save(shipment)
    }


    fun getHistory(trackingId: String): List<PackageEventResponse> {
        val shipment = getPackageByTracking(trackingId)
        val history = historyRepo.findAllByShipmentId(shipment.id)
        return history.map { PackageEventResponse.fromEntity(it) }
    }

}
