package com.pucetec.lopez_jhosue_shipflow.controller


import com.fasterxml.jackson.databind.ObjectMapper
import com.pucetec.lopez_jhosue_shipflow.controllers.PackageController
import com.pucetec.lopez_jhosue_shipflow.request.PackageRequest
import com.pucetec.lopez_jhosue_shipflow.models.entities.Package
import com.pucetec.lopez_jhosue_shipflow.models.entities.Status
import com.pucetec.lopez_jhosue_shipflow.models.entities.Type
import com.pucetec.lopez_jhosue_shipflow.services.PackageService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime


@WebMvcTest(PackageController::class)
class PackageControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var packageService: PackageService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun <T> any(): T = Mockito.any<T>() ?: null as T

    @Test
    fun `should create package successfully`() {
        val request = PackageRequest(
            type = "SMALL_BOX",
            weight = 2.5f,
            description = "Paquete de prueba",
            cityFrom = "Quito",
            cityTo = "Guayaquil"
        )

        val pkgToCreate = Package(
            type = Type.SMALL_BOX,
            weight = 2.5f,
            description = "Paquete de prueba",
            cityFrom = "Quito",
            cityTo = "Guayaquil"
        )

        val createdPkg = pkgToCreate.copy(
            id = 1L,
            trackingId = "ABC12345",
            estimatedDeliveryDate = LocalDateTime.of(2025, 8, 5, 10, 0),
            status = Status.PENDING
        )

        Mockito.`when`(packageService.createPackage(any()))
            .thenReturn(createdPkg)

        mockMvc.perform(
            post("/api/packages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.type").value("SB"))
            .andExpect(jsonPath("$.description").value("Paquete de prueba"))
            .andExpect(jsonPath("$.cityFrom").value("Quito"))
            .andExpect(jsonPath("$.cityTo").value("Guayaquil"))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.trackingId").value("ABC12345"))
    }
}
