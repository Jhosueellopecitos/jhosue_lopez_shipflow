package com.pucetec.lopez_jhosue_shipflow.response

import com.pucetec.lopez_jhosue_shipflow.models.entities.Status
import java.time.LocalDateTime


data class PackageEventResponse(
    val status: Status,
    val comment: String?,
    val changedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(event: com.pucetec.lopez_jhosue_shipflow.models.entities.PackageHistory): PackageEventResponse {
            return PackageEventResponse(
                status = event.status,
                comment = event.comment,
                changedAt = event.createdAt
            )
        }
    }
}
