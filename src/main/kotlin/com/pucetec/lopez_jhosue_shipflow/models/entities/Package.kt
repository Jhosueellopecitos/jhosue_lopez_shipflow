package com.pucetec.lopez_jhosue_shipflow.models.entities


import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.hibernate.engine.internal.Cascade
import java.time.LocalDateTime

@Entity
@Table(name = "packages")
data class Package(
    val type: Type,
    val weight: Float,

    @Column(length = 50)
    val description: String,

    @Column(name = "city_from")
    val cityFrom: String,

    @Column(name = "city_to")
    val cityTo: String,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING,

    val trackingId: String = "",

    @Column(name = "estimated_delivery_date")
    val estimatedDeliveryDate: LocalDateTime = LocalDateTime.now().plusDays(5),


    @OneToMany(mappedBy = "shipment", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    val history: MutableList<PackageHistory> = mutableListOf()

) : BaseEntity()



enum class Type(val code: String) {
    DOCUMENT("D"),
    SMALL_BOX("SB"),
    FRAGILE("F");

    override fun toString() = code
}
