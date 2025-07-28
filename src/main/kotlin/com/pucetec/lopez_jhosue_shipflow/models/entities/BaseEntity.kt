package com.pucetec.lopez_jhosue_shipflow.models.entities
import jakarta.persistence.*
import java.time.LocalDateTime


@MappedSuperclass
abstract class BaseEntity {

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @PreUpdate
    protected fun update() {
        updatedAt = LocalDateTime.now()
    }
}