package repository

import com.example.database.MongoDatabaseFactory
import com.example.plugins.JWTConfig
import com.example.plugins.generateToken
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import model.*
import security.PasswordHasher
import java.time.LocalDateTime

object UserRepository {

    private val database = MongoDatabaseFactory.client
    private val userCollection = database.getCollection<User>("users")

    suspend fun registerUser(user: RegisterRequest): RepositoryResponse<Boolean> {
        return try {
            val doesUserExist = userCollection.find<User>(Filters.eq("email", user.email)).firstOrNull()
            if (doesUserExist != null) {
                RepositoryResponse(
                    data = false,
                    message = "User Already Created",
                    statusCode = HttpStatusCode.Conflict.value
                )
            } else {

                val hashedPassword = PasswordHasher.createHashPassword(password = user.password)

                val newUser = User(
                    name = user.name,
                    email = user.email,
                    password = hashedPassword,
                    profileImage = "https://avatar.iran.liara.run/public/boy?username=${user.name}",
                )

                userCollection.insertOne(newUser)

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
        val userExists = userCollection.find<User>(Filters.eq("email", user.email)).firstOrNull()
        return try {
            if (userExists != null) {
                val checkPassword = PasswordHasher.checkPassword(user.password, userExists.password)

                if (!userExists.isActive) {
                    return RepositoryResponse(
                        data = null,
                        message = "Your account has been deactivated, please contact support",
                        statusCode = HttpStatusCode.Unauthorized.value
                    )

                }

                if (checkPassword) {
                    val token = generateToken(jwtConfig, email = user.email, userExists.role)

                    userCollection.updateOne(
                        Filters.eq("email", user.email),
                        Updates.set("lastLoginAt", LocalDateTime.now().toString())
                    )

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

    suspend fun changePassword(email: String, req: ChangePasswordRequest): RepositoryResponse<Boolean> {

        try {
            val existingUser = userCollection.find(Filters.eq("email", email)).firstOrNull()
                ?: return RepositoryResponse(false, "User doesn't exist", HttpStatusCode.NotFound.value)


            val isPasswordCorrect = PasswordHasher.checkPassword(req.currentPassword, existingUser.password)

            if (!isPasswordCorrect) {
                return RepositoryResponse(false, "Current password is incorrect", HttpStatusCode.Unauthorized.value)
            }

            val newHashed = PasswordHasher.createHashPassword(req.newPassword)
            userCollection.updateOne(Filters.eq("email", email), Updates.set("password", newHashed))

            return RepositoryResponse(true, "Password changed successfully", HttpStatusCode.OK.value)
        } catch (e: Exception) {
            return RepositoryResponse(false, "An error occurred ${e.localizedMessage}", HttpStatusCode.OK.value)

        }
    }


}

