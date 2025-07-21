package com.pucetec.lopez_jhosue_shipflow.repositories

import com.pucetec.lopez_jhosue_shipflow.models.entities.Package
import com.pucetec.lopez_jhosue_shipflow.models.entities.PackageHistory
import com.pucetec.lopez_jhosue_shipflow.models.entities.Status
import org.springframework.data.jpa.repository.JpaRepository

interface PackageEventRepository : JpaRepository<PackageHistory, Long> {
    fun findAllByShipmentId(packageId: Long): List<PackageHistory>
    fun existsByShipmentAndStatus(shipment: Package, status: Status): Boolean
}