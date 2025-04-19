package repository

import com.example.database.MongoDatabaseFactory
import com.example.plugins.JWTConfig
import com.example.plugins.generateToken
import com.mongodb.client.model.Filters
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import model.LoginRequest
import model.RepositoryResponse
import model.User
import security.PasswordHasher

object UserRepository {

    private val database = MongoDatabaseFactory.client
    private val collection = database.getCollection<User>("users")

    suspend fun registerUser(user: User): RepositoryResponse<Boolean> {
        return try {
            val doesUserExist = collection.find<User>(Filters.eq("email", user.email)).firstOrNull()
            if (doesUserExist != null) {
                RepositoryResponse(
                    data = false,
                    message = "User Already Created",
                    statusCode = HttpStatusCode.Conflict.value
                )
            } else {

                val hashedPassword = PasswordHasher.createHashPassword(password = user.password)

                val userWithHashedPassword = user.copy(password = hashedPassword)
                println(hashedPassword)


                collection.insertOne(userWithHashedPassword)

                return RepositoryResponse(
                    data = true,
                    message = "User Created Successfully",
                    statusCode = HttpStatusCode.Created.value
                )
            }
        } catch (e: Exception) {
            return RepositoryResponse(
                data = false,
                message = "An Error has occurred ${e.localizedMessage}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }

    }

    suspend fun loginUser(user: LoginRequest, jwtConfig: JWTConfig): RepositoryResponse<String> {

        val userExists = collection.find<User>(Filters.eq("email", user.email)).firstOrNull()

        return try {
            if (userExists != null) {
                val checkPassword = PasswordHasher.checkPassword(user.password, userExists.password)

                if (checkPassword) {
                    val token = generateToken(jwtConfig, email = user.email)
                    RepositoryResponse(data = token, message = "User Logged In", statusCode = HttpStatusCode.OK.value)
                } else {
                    RepositoryResponse(
                        data = null,
                        message = "Password is Incorrect",
                        statusCode = HttpStatusCode.BadRequest.value
                    )

                }

            } else {
                RepositoryResponse(data = null, message = "User not found", statusCode = HttpStatusCode.NotFound.value)

            }
        } catch (e: Exception) {
            RepositoryResponse(
                data = e.localizedMessage,
                message = "Error Found",
                statusCode = HttpStatusCode.NotFound.value
            )
        }
    }
}