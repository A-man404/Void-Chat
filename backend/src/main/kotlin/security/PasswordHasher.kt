package security

import org.mindrot.jbcrypt.BCrypt

object PasswordHasher {

    fun createHashPassword(password: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(password, salt)
    }

    fun checkPassword(password: String, hashPassword: String): Boolean {
        return BCrypt.checkpw(password, hashPassword)
    }

}