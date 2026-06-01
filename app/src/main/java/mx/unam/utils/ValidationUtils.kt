package mx.unam.utils

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(pass: String): Boolean {
    val passwordPattern = "^(?=.*[A-Z])(?=.*[0-9]).{6,}\$"
    return pass.matches(Regex(passwordPattern))
}