package com.pucetec.lopez_jhosue_shipflow.models.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "package_history")
data class PackageHistory(
    @Enumerated(EnumType.STRING)
    val status: Status,

    val comment: String? = null,

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    @JsonBackReference
    val shipment: Package

) : BaseEntity()


enum class Status(val code: String) {
    PENDING("P"),
    IN_TRANSIT("IT"),
    DELIVERED("D"),
    ON_HOLD("OH"),
    CANCELLED("C");

    override fun toString() = code
}
