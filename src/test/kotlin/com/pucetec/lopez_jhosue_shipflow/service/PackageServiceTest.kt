package com.pucetec.lopez_jhosue_shipflow.service


import com.pucetec.lopez_jhosue_shipflow.models.entities.Package
import com.pucetec.lopez_jhosue_shipflow.models.entities.PackageHistory
import com.pucetec.lopez_jhosue_shipflow.models.entities.Status
import com.pucetec.lopez_jhosue_shipflow.models.entities.Type
import com.pucetec.lopez_jhosue_shipflow.repositories.PackageEventRepository
import com.pucetec.lopez_jhosue_shipflow.repositories.PackageRepository
import com.pucetec.lopez_jhosue_shipflow.services.PackageService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import java.time.LocalDateTime
import java.util.*


/*
@ExtendWith(MockitoExtension::class)
class PackageServiceTest {

    @Mock
    lateinit var packageRepo: PackageRepository

    @Mock
    lateinit var packageEventRepo: PackageEventRepository

    lateinit var packageService: PackageService

    private lateinit var mockPackage: Package

    @BeforeEach
    fun setUp() {
        packageService = PackageService(packageRepo, packageEventRepo)

        mockPackage = Package(
            type = Type.SMALL_BOX,
            weight = 2.0f,
            description = "Test Package",
            cityFrom = "Quito",
            cityTo = "Guayaquil",
            status = Status.IN_TRANSIT,
            trackingId = "ABC123"
        )
    }

    @Test
    fun `updateStatus exitoso de IN_TRANSIT a DELIVERED`() {
        val mockEvent = PackageHistory(
            status = Status.DELIVERED,
            shipment = mockPackage,
            comment = "Entregado correctamente"
        )

        whenever(packageRepo.findByTrackingId("ABC123")).thenReturn(mockPackage)
        whenever(packageEventRepo.existsByShipmentAndStatus(mockPackage, Status.IN_TRANSIT)).thenReturn(true)
        whenever(packageEventRepo.save(any<PackageHistory>())).thenReturn(mockEvent)

        packageService.updateStatus("ABC123", Status.DELIVERED, "Entregado correctamente")

        assertEquals(Status.DELIVERED, mockPackage.status)
        verify(packageEventRepo).save(any())
        verify(packageRepo).save(mockPackage)
    }

    @Test
    fun `no se permite pasar de PENDING a DELIVERED directamente`() {
        val p = mockPackage.copy(status = Status.PENDING)
        whenever(packageRepo.findByTrackingId("ABC123")).thenReturn(p)

        val ex = assertThrows<IllegalStateException> {
            packageService.updateStatus("ABC123", Status.DELIVERED, null)
        }

        assertTrue(ex.message!!.contains("No se puede pasar de"))
    }

    @Test
    fun `no se permite marcar como DELIVERED si nunca estuvo en tránsito`() {
        val p = mockPackage.copy(status = Status.ON_HOLD)
        whenever(packageRepo.findByTrackingId("ABC123")).thenReturn(p)
        whenever(packageEventRepo.existsByShipmentAndStatus(p, Status.IN_TRANSIT)).thenReturn(false)

        val ex = assertThrows<IllegalStateException> {
            packageService.updateStatus("ABC123", Status.DELIVERED, null)
        }

        assertTrue(ex.message!!.contains("Solo se puede marcar como entregado"))
    }

    @Test
    fun `lanzar error si el paquete no existe`() {
        whenever(packageRepo.findByTrackingId("NO_EXISTE")).thenReturn(null)

        val ex = assertThrows<IllegalArgumentException> {
            packageService.updateStatus("NO_EXISTE", Status.CANCELLED, "No se encontró")
        }

        assertEquals("Envío no encontrado", ex.message)
    }

    @Test
    fun `se permite cancelar desde IN_TRANSIT`() {
        val mockEvent = PackageHistory(
            status = Status.CANCELLED,
            shipment = mockPackage,
            comment = "Cancelado por el usuario"
        )

        // Preparar mocks
        whenever(packageRepo.findByTrackingId("ABC123")).thenReturn(mockPackage)
        whenever(packageEventRepo.existsByShipmentAndStatus(any<Package>(), eq(Status.IN_TRANSIT))).thenReturn(true)
        whenever(packageEventRepo.save(any())).thenReturn(mockEvent)

        // Ejecutar método bajo prueba
        packageService.updateStatus("ABC123", Status.CANCELLED, "Cancelado por el usuario")

        // Verificaciones y aserciones
        assertEquals(Status.CANCELLED, mockPackage.status)
        verify(packageEventRepo).save(any())
        verify(packageRepo).save(mockPackage)
    }
}

*/

class PackageServiceTest {

    private val packageRepo = mock(PackageRepository::class.java)
    private val historyRepo = mock(PackageEventRepository::class.java)
    private val service = PackageService(packageRepo, historyRepo)

    private val testPackage = Package(
        type = Type.SMALL_BOX,
        weight = 2.5f,
        description = "Documentos legales",
        cityFrom = "Quito",
        cityTo = "Guayaquil",
        status = Status.PENDING,
        trackingId = "ABC123",
        estimatedDeliveryDate = LocalDateTime.now().plusDays(5)
    )

    @Test
    fun `createPackage crea correctamente`() {
        `when`(packageRepo.save(any())).thenAnswer { it.arguments[0] }
        val result = service.createPackage(testPackage)
        assertEquals(Status.PENDING, result.status)
        assertNotEquals("", result.trackingId)
    }

    @Test
    fun `createPackage lanza error si origen y destino son iguales`() {
        val invalidPackage = testPackage.copy(cityTo = "Quito")
        val ex = assertThrows<IllegalArgumentException> {
            service.createPackage(invalidPackage)
        }
        assertEquals("La ciudad de origen y destino no pueden ser iguales", ex.message)
    }

    @Test
    fun `createPackage lanza error si descripcion es muy larga`() {
        val invalidPackage = testPackage.copy(description = "a".repeat(51))
        val ex = assertThrows<IllegalArgumentException> {
            service.createPackage(invalidPackage)
        }
        assertEquals("La descripción no puede tener más de 50 caracteres", ex.message)
    }

    @Test
    fun `createPackage lanza error si peso es negativo o cero`() {
        val invalidPackage = testPackage.copy(weight = 0f)
        val ex = assertThrows<IllegalArgumentException> {
            service.createPackage(invalidPackage)
        }
        assertEquals("El peso debe ser mayor que cero", ex.message)
    }

    @Test
    fun `getPackageByTracking devuelve paquete existente`() {
        `when`(packageRepo.findByTrackingId("ABC123")).thenReturn(testPackage)
        val found = service.getPackageByTracking("ABC123")
        assertEquals(testPackage, found)
    }

    @Test
    fun `getPackageByTracking lanza error si no existe`() {
        `when`(packageRepo.findByTrackingId("ZZZ999")).thenReturn(null)
        val ex = assertThrows<IllegalArgumentException> {
            service.getPackageByTracking("ZZZ999")
        }
        assertEquals("Paquete con tracking ZZZ999 no encontrado", ex.message)
    }

    @Test
    fun `updateStatus lanza error si paquete no existe`() {
        `when`(packageRepo.findByTrackingId("NOEXIST")).thenReturn(null)
        val ex = assertThrows<IllegalArgumentException> {
            service.updateStatus("NOEXIST", Status.IN_TRANSIT, null)
        }
        assertEquals("Envío no encontrado", ex.message)
    }

    @Test
    fun `updateStatus actualiza estado si es transicion valida`() {
        val pkg = testPackage.copy(status = Status.PENDING)
        `when`(packageRepo.findByTrackingId(pkg.trackingId)).thenReturn(pkg)
        `when`(historyRepo.save(any())).thenReturn(mock(PackageHistory::class.java))
        service.updateStatus(pkg.trackingId, Status.IN_TRANSIT, "comentario")
        verify(packageRepo).save(pkg)
        assertEquals(Status.IN_TRANSIT, pkg.status)
    }

    @Test
    fun `updateStatus lanza error si estado es final`() {
        val pkg = testPackage.copy(status = Status.DELIVERED)
        `when`(packageRepo.findByTrackingId(pkg.trackingId)).thenReturn(pkg)
        val ex = assertThrows<IllegalStateException> {
            service.updateStatus(pkg.trackingId, Status.CANCELLED, null)
        }
        assertEquals("El estado DELIVERED es final y no puede cambiarse", ex.message)
    }

    @Test
    fun `updateStatus lanza error si transicion no permitida`() {
        val pkg = testPackage.copy(status = Status.PENDING)
        `when`(packageRepo.findByTrackingId(pkg.trackingId)).thenReturn(pkg)
        val ex = assertThrows<IllegalStateException> {
            service.updateStatus(pkg.trackingId, Status.CANCELLED, null)
        }
        assertEquals("No se puede pasar de PENDING a CANCELLED", ex.message)
    }

    @Test
    fun `updateStatus lanza error si pasa a DELIVERED sin IN_TRANSIT previo`() {
        val pkg = testPackage.copy(status = Status.PENDING)
        `when`(packageRepo.findByTrackingId(pkg.trackingId)).thenReturn(pkg)
        `when`(historyRepo.existsByShipmentAndStatus(pkg, Status.IN_TRANSIT)).thenReturn(false)
        val ex = assertThrows<IllegalStateException> {
            service.updateStatus(pkg.trackingId, Status.DELIVERED, null)
        }
        assertEquals("Solo se puede marcar como entregado si estuvo en tránsito", ex.message)
    }

    @Test
    fun `updateStatus permite pasar a DELIVERED si estuvo en transito`() {
        val pkg = testPackage.copy(status = Status.IN_TRANSIT)
        `when`(packageRepo.findByTrackingId(pkg.trackingId)).thenReturn(pkg)
        `when`(historyRepo.existsByShipmentAndStatus(pkg, Status.IN_TRANSIT)).thenReturn(true)
        service.updateStatus(pkg.trackingId, Status.DELIVERED, "Entregado")
        verify(packageRepo).save(pkg)
        assertEquals(Status.DELIVERED, pkg.status)
    }

    @Test
    fun `getHistory retorna eventos del paquete`() {
        val pkg = testPackage.copy(id = 100L)
        val mockEvents = listOf(
            PackageHistory(status = Status.IN_TRANSIT, shipment = pkg, comment = "salió"),
            PackageHistory(status = Status.ON_HOLD, shipment = pkg, comment = "retraso")
        )
        `when`(packageRepo.findByTrackingId(pkg.trackingId)).thenReturn(pkg)
        `when`(historyRepo.findAllByShipmentId(pkg.id)).thenReturn(mockEvents)
        val result = service.getHistory(pkg.trackingId)
        assertEquals(2, result.size)
        assertEquals("salió", result[0].comment)
    }
}