package org.purboyndradev.rt_rw.helper

object Validators {
    fun required(value: String, label: String): String? =
        if (value.isBlank()) "$label is required" else null
    
    private val EMAIL_REGEX =
        Regex("^[A-Za-z0-9+._%\\-]{1,256}@[A-Za-z0-9][A-Za-z0-9\\-]{0,64}(\\.[A-Za-z0-9][A-Za-z0-9\\-]{0,25})+$")
    
    fun email(value: String): String? =
        if (value.isBlank()) "Email is required"
        else if (!EMAIL_REGEX.matches(value)) "Enter a valid email"
        else null
    
    fun minLength(value: String, min: Int, label: String): String? =
        if (value.length < min) "$label must be at least $min characters" else null
    
    fun phoneNumber(value: String): String? =
        if (value.isBlank()) "Phone Number is required" else if (value.length > 13 || !value.startsWith(
                "08"
            )
        ) "Enter a valid phone number" else null
    
}

data class FieldState(
    val value: String = "",
    val error: String? = null,
    val touched: Boolean = false
)