package com.aplications.parcial2.domain.validation
data class TripInput(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)

data class PlaceInput(
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int?,
    val tentativeDate: String
)
data class ValidationResult(val errors: Map<String, String> = emptyMap()) {
    val isValid: Boolean get() = errors.isEmpty()
}

fun isValidDate(value: String): Boolean {
    if (!Regex("""\d{4}-\d{2}-\d{2}""").matches(value)) return false
    val year = value.substring(0, 4).toInt()
    val month = value.substring(5, 7).toInt()
    val day = value.substring(8, 10).toInt()
    if (month !in 1..12 || day < 1) return false
    val leap = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
    val maxDay = when (month) {
        2 -> if (leap) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
    return day <= maxDay
}

fun validateTrip(input: TripInput): ValidationResult {
    val errors = mutableMapOf<String, String>()

    if (input.name.isBlank()) errors["name"] = "El nombre es obligatorio"
    if (input.destination.isBlank()) errors["destination"] = "El destino es obligatorio"

    var startOk = false
    var endOk = false
    if (input.startDate.isBlank()) errors["startDate"] = "La fecha de inicio es obligatoria"
    else if (!isValidDate(input.startDate)) errors["startDate"] = "Formato inválido (yyyy-MM-dd)"
    else startOk = true

    if (input.endDate.isBlank()) errors["endDate"] = "La fecha de fin es obligatoria"
    else if (!isValidDate(input.endDate)) errors["endDate"] = "Formato inválido (yyyy-MM-dd)"
    else endOk = true

    if (startOk && endOk && input.startDate > input.endDate) {
        errors["endDate"] = "La fecha de inicio no puede ser posterior a la de fin"
    }
    return ValidationResult(errors)
}

fun validatePlace(input: PlaceInput, tripStart: String, tripEnd: String): ValidationResult {
    val errors = mutableMapOf<String, String>()

    if (input.name.isBlank()) errors["name"] = "El nombre es obligatorio"
    if (input.description.isBlank()) errors["description"] = "La descripción es obligatoria"
    if (input.city.isBlank()) errors["city"] = "La ciudad es obligatoria"
    if (input.categoryId == null) errors["categoryId"] = "Selecciona una categoría"

    if (input.tentativeDate.isBlank()) {
        errors["tentativeDate"] = "La fecha de visita es obligatoria"
    } else if (!isValidDate(input.tentativeDate)) {
        errors["tentativeDate"] = "Formato inválido (yyyy-MM-dd)"
    } else if (input.tentativeDate < tripStart) {
        errors["tentativeDate"] = "La fecha no puede ser anterior al inicio del viaje ($tripStart)"
    } else if (input.tentativeDate > tripEnd) {
        errors["tentativeDate"] = "La fecha no puede ser posterior al fin del viaje ($tripEnd)"
    }
    return ValidationResult(errors)
}
