package com.aplications.parcial2.domain.validation


import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatorsTest {

    private val validTrip = TripInput("Europa 2026", "Italia", "2026-06-10", "2026-06-20")
    private val validPlace = PlaceInput("Coliseo", "Anfiteatro romano", "Roma", 1, "2026-06-12")
    private val start = "2026-06-10"
    private val end = "2026-06-20"
    //validateTrip
    @Test fun trip_valido_pasa() = assertTrue(validateTrip(validTrip).isValid)

    @Test fun trip_sin_nombre_falla() {
        val r = validateTrip(validTrip.copy(name = " "))
        assertFalse(r.isValid); assertTrue("name" in r.errors)
    }

    @Test fun trip_sin_destino_falla() =
        assertTrue("destination" in validateTrip(validTrip.copy(destination = "")).errors)

    @Test fun trip_sin_fechas_falla() {
        val r = validateTrip(validTrip.copy(startDate = "", endDate = ""))
        assertTrue("startDate" in r.errors && "endDate" in r.errors)
    }

    @Test fun trip_inicio_posterior_a_fin_falla() =
        assertTrue("endDate" in validateTrip(validTrip.copy(startDate = "2026-06-25")).errors)

    @Test fun trip_mismo_dia_es_valido() =
        assertTrue(validateTrip(validTrip.copy(endDate = "2026-06-10")).isValid)

    @Test fun trip_fecha_inexistente_falla() =
        assertTrue("startDate" in validateTrip(validTrip.copy(startDate = "2026-02-30")).errors)

    // ---- validatePlace ----
    @Test fun place_valido_pasa() = assertTrue(validatePlace(validPlace, start, end).isValid)

    @Test fun place_sin_campos_obligatorios_falla() {
        val r = validatePlace(PlaceInput("", "", "", null, ""), start, end)
        assertEquals(setOf("name", "description", "city", "categoryId", "tentativeDate"), r.errors.keys)
    }

    @Test fun place_fecha_anterior_al_viaje_falla() =
        assertTrue("tentativeDate" in validatePlace(validPlace.copy(tentativeDate = "2026-06-09"), start, end).errors)

    @Test fun place_fecha_posterior_al_viaje_falla() =
        assertTrue("tentativeDate" in validatePlace(validPlace.copy(tentativeDate = "2026-06-21"), start, end).errors)

    @Test fun place_fecha_en_los_limites_es_valida() {
        assertTrue(validatePlace(validPlace.copy(tentativeDate = start), start, end).isValid)
        assertTrue(validatePlace(validPlace.copy(tentativeDate = end), start, end).isValid)
    }
}
