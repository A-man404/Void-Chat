package com.example.pages.mainTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.ui.theme.*
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosForward
import compose.icons.evaicons.outline.LogOut
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.Settings

@Composable
fun ProfileScreen() {
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        TopAppBar(
            title = { Text("Profile", color = softWhite, fontSize = 22.sp) },
            backgroundColor = backgroundColor,
            elevation = 0.dp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Profile Header
        Card(
            backgroundColor = lightCardColor,
            shape = RoundedCornerShape(24.dp),
            elevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(softWhite)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Your Name", color = softWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("youremail@example.com", color = softTextColor, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* TODO: Change Profile */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = accentPastel),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Change Profile", color = Color.Black, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Action Items
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileActionItem(
                label = "Profile Info & Settings",
                icon = EvaIcons.Outline.Settings,
                onClick = { /* TODO: Navigate to Profile Info */ }
            )

            ProfileActionItem(
                label = "Add Friend",
                icon = EvaIcons.Outline.Person,
                onClick = { /* TODO: Navigate to Add Friend */ }
            )

            ProfileActionItem(
                label = "Friends List",
                icon = EvaIcons.Outline.Person,
                onClick = { /* TODO: Navigate to Friends List */ }
            )

            ProfileActionItem(
                label = "Logout",
                icon = EvaIcons.Outline.LogOut,
                onClick = { /* TODO: Logout */ }
            )
        }
    }
}

@Composable
fun ProfileActionItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        backgroundColor = cardColor,
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 18.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
    2                   tint = accentPastel,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(label, color = softWhite, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Icon(
                imageVector = EvaIcons.Outline.ArrowIosForward,
                contentDescription = null,
                tint = softTextColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
