package com.panini.support.data.mock

import com.panini.support.data.model.Category
import com.panini.support.data.model.Priority
import com.panini.support.data.model.Status
import com.panini.support.data.model.Ticket
import java.time.LocalDateTime

/**
 * Realistic mock data representing actual support scenarios for
 * Panini Costa Rica's FIFA World Cup 2026 album distribution operation.
 *
 * These tickets cover: inventory shortages, provider delays,
 * logistics failures, and distribution errors across CR sales points.
 */
object MockTickets {

    val tickets: List<Ticket> = listOf(
        Ticket(
            id = "TKT-001",
            title = "Faltante de sobres en punto de venta San José Centro",
            description = "El distribuidor reporta que el punto de venta ubicado en Mall San Pedro " +
                    "tiene un faltante de 2,400 sobres correspondientes al lote #CR-2026-04. " +
                    "El pedido fue confirmado pero el envío no llegó en la fecha acordada.",
            priority = Priority.CRITICAL,
            status = Status.OPEN,
            provider = "Distribuidora Nacional Panini CR",
            category = Category.INVENTORY,
            createdAt = LocalDateTime.of(2026, 5, 28, 8, 30),
            assignedTo = "Carlos Méndez"
        ),
        Ticket(
            id = "TKT-002",
            title = "Proveedor de impresión no entrega stickers laminados",
            description = "El proveedor Grupo Gráfico Centroamérica no ha entregado el pedido " +
                    "de 50,000 stickers laminados especiales para la edición coleccionable. " +
                    "Llevan 6 días de retraso sin comunicación oficial.",
            priority = Priority.HIGH,
            status = Status.IN_PROGRESS,
            provider = "Grupo Gráfico Centroamérica",
            category = Category.PROVIDER,
            createdAt = LocalDateTime.of(2026, 5, 26, 14, 15),
            assignedTo = "Andrea Solís"
        ),
        Ticket(
            id = "TKT-003",
            title = "Error en facturación lote Liberia, Guanacaste",
            description = "La factura del lote #CR-2026-07 enviado a la zona de Liberia presenta " +
                    "discrepancias: se facturaron 1,200 unidades pero solo se enviaron 900. " +
                    "Se requiere nota de crédito y ajuste de inventario.",
            priority = Priority.HIGH,
            status = Status.OPEN,
            provider = "LogiPack Transportes CR",
            category = Category.LOGISTICS,
            createdAt = LocalDateTime.of(2026, 5, 27, 11, 0),
            assignedTo = null
        ),
        Ticket(
            id = "TKT-004",
            title = "Retraso en despacho para zona Caribe (Limón)",
            description = "El despacho programado para la zona Caribe fue cancelado por la " +
                    "empresa de transporte sin previo aviso. Los 8 puntos de venta en Limón " +
                    "llevan 4 días sin reabastecimiento durante la semana de mayor demanda.",
            priority = Priority.CRITICAL,
            status = Status.OPEN,
            provider = "Transportes Caribe Express",
            category = Category.DISTRIBUTION,
            createdAt = LocalDateTime.of(2026, 5, 29, 9, 45),
            assignedTo = "Marco Vargas"
        ),
        Ticket(
            id = "TKT-005",
            title = "Stickers duplicados en lote álbum edición especial",
            description = "Múltiples clientes reportan que el lote #CR-2026-05 de la edición " +
                    "especial contiene stickers duplicados del número 128 (Messi) y ningún " +
                    "sticker del número 315. Posible error de empaque en fábrica.",
            priority = Priority.MEDIUM,
            status = Status.IN_PROGRESS,
            provider = "Panini Group Italia (Fábrica)",
            category = Category.INVENTORY,
            createdAt = LocalDateTime.of(2026, 5, 25, 16, 20),
            assignedTo = "Andrea Solís"
        ),
        Ticket(
            id = "TKT-008",
            title = "Inventario no concilia entre sistema y físico en CEDI",
            description = "El Centro de Distribución (CEDI) en San Antonio de Belén reporta " +
                    "una diferencia de 3,800 sobres entre el inventario registrado en el " +
                    "sistema y el conteo físico realizado el 27 de mayo.",
            priority = Priority.HIGH,
            status = Status.OPEN,
            provider = "Distribuidora Nacional Panini CR",
            category = Category.INVENTORY,
            createdAt = LocalDateTime.of(2026, 5, 27, 7, 0),
            assignedTo = "Marco Vargas"
        ),
        Ticket(
            id = "TKT-009",
            title = "Retraso aduanero en importación lote premium FIFA",
            description = "El lote premium de álbumes edición FIFA 2026 está retenido en " +
                    "aduana desde hace 5 días por documentación incompleta del agente " +
                    "aduanero. Se estima impacto en 12 puntos de venta de alta rotación.",
            priority = Priority.CRITICAL,
            status = Status.IN_PROGRESS,
            provider = "Agencia Aduanera Centroamérica S.A.",
            category = Category.LOGISTICS,
            createdAt = LocalDateTime.of(2026, 5, 23, 15, 0),
            assignedTo = "Andrea Solís"
        )
    )
}
