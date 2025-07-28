package com.pucetec.lopez_jhosue_shipflow.models.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "package_history")
data class PackageHistory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Enumerated(EnumType.STRING)
    val status: Status,

    val comment: String? = null,

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    @JsonBackReference
    val shipment: Package

) : BaseEntity()


enum class Status(val code: String) {
    PENDING("PENDING"),
    IN_TRANSIT("IN_TRANSIT"),
    DELIVERED("DELIVERED"),
    ON_HOLD("ON_HOLD"),
    CANCELLED("CANCELLED");

    override fun toString() = code
}
