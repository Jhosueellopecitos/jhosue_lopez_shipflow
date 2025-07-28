package com.pucetec.lopez_jhosue_shipflow.models.entities


import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.*
import org.hibernate.engine.internal.Cascade
import java.time.LocalDateTime

@Entity
@Table(name = "packages")
data class Package(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
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

    companion object {
        fun fromCode(code: String): Type {
            return values().firstOrNull { it.code == code || it.name == code }
                ?: throw IllegalArgumentException("Tipo inválido: $code")
        }
    }

    @JsonValue
    fun toValue(): String {
        return code
    }
}

