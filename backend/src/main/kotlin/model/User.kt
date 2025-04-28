package model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class User(
    val name: String,
    val email: String,
    val password: String,
    val createdAt: String = LocalDateTime.now().toString(),
    val profileImage: String,
    val lastLoginAt: String? = LocalDateTime.now().toString(),
    val role: UserRole = UserRole.USER,
    val isActive: Boolean = true,
    val status: String = "OFFLINE",
    val friends: List<String> = emptyList(),
    val friendRequests: List<String> = emptyList(),
    val blockedUser: List<String> = emptyList(),
)


@Serializable
data class FriendUser(
    val name: String,
    val email: String,
    val isActive: Boolean,
    val status: String,
    val profileImage: String,
    val friends: List<String>
) {
    companion object {
        fun fromUser(user: User): FriendUser {
            return FriendUser(
                name = user.name,
                email = user.email,
                isActive = user.isActive,
                status = user.status,
                profileImage = user.profileImage,
                friends = user.friends
            )
        }
    }
}

@Serializable
data class LoginRequest(
    val email: String, val password: String
)

@Serializable
data class RegisterRequest(
    val email: String, val password: String, val name: String
)

@Serializable
data class ChangePasswordRequest(
    val currentPassword: String, val newPassword: String
)


@Serializable
data class ChangeRoleRequest(
    val targetEmail: String, val newRole: String
)


enum class UserRole {
    USER, ADMIN
}