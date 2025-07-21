package com.pucetec.lopez_jhosue_shipflow.repositories

import com.pucetec.lopez_jhosue_shipflow.models.entities.Package
import org.springframework.data.jpa.repository.JpaRepository

interface PackageRepository : JpaRepository<Package, Long> {
    fun findByTrackingId(trackingId: String): Package?
}