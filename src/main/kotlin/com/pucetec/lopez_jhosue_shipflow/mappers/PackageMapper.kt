package com.pucetec.lopez_jhosue_shipflow.mappers

import com.pucetec.lopez_jhosue_shipflow.request.PackageRequest
import org.springframework.stereotype.Component
import com.pucetec.lopez_jhosue_shipflow.models.entities.Package as PackageEntity


@Component
class PackageMapper {

    fun toEntity(request: PackageRequest): PackageEntity {
        return PackageEntity(
            type = request.type,
            weight = request.weight,
            description = request.description,
            cityFrom = request.cityFrom,
            cityTo = request.cityTo
        )
    }
}