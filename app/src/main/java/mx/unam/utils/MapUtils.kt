package mx.unam.utils

object CharacterDisplayMapper {

    fun race(value: String): String {
        return when (value.trim()) {
            "Saiyan" -> "Saiyajin"
            "Namekian" -> "Namekiano"
            "Human" -> "Humano"
            "Majin" -> "Majin"
            "Frieza Race" -> "Raza de Freezer"
            "Jiren Race" -> "Raza de Jiren"
            "Android" -> "Androide"
            "God" -> "Dios"
            "Angel" -> "Ángel"
            "Evil" -> "Maligno"
            "Unknown" -> "Desconocido"
            "Nucleico benigno" -> "Nucleico benigno"
            "Nucleico" -> "Nucleico"
            else -> value
        }
    }

    fun gender(value: String): String {
        return when (value.trim()) {
            "Male" -> "Masculino"
            "Female" -> "Femenino"
            "Other" -> "Otro"
            "Unknown" -> "Desconocido"
            else -> value
        }
    }

    fun affiliation(value: String): String {
        return when (value.trim()) {
            "Z Fighter" -> "Guerrero Z"
            "Red Ribbon Army" -> "Ejército Red Ribbon"
            "Namekian Warrior" -> "Guerrero Namekiano"
            "Freelancer" -> "Independiente"
            "Army of Frieza" -> "Ejército de Freezer"
            "Other" -> "Otro"
            "Pride Troopers" -> "Tropas del Orgullo"
            "Assistant of Vermoud" -> "Asistente de Vermoud"
            "Assistant of Beerus" -> "Asistente de Bills"
            "Villain" -> "Villano"
            else -> value
        }
    }
}