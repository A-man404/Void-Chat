package com.example.pages.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.pages.mainTabs.ChatListScreen
import com.example.pages.mainTabs.ProfileScreen
import com.example.ui.theme.accentPastel
import com.example.ui.theme.backgroundColor
import com.example.ui.theme.softTextColor
import com.example.ui.theme.softWhite
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.MessageCircle
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.fill.PhoneCall
import compose.icons.evaicons.outline.Bell

object MainScreen : Screen {
    @Composable
    override fun Content() {
        var selectedTabIndex by remember { mutableStateOf(1) }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .systemBarsPadding(),
            topBar = { ChatTopAppBar() },
            bottomBar = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    BottomNavigationBar(
                        selectedIndex = selectedTabIndex,
                        onItemSelected = { selectedTabIndex = it },

                        )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
//                    .padding(innerPadding)
                    .background(backgroundColor)
            ) {
                when (selectedTabIndex) {
//                    0 -> CallsScreenContent()
                    1 -> ChatListScreen()
                    2 -> ProfileScreen()
                }
            }
        }
    }
}


@Composable
private fun ChatTopAppBar() {
    TopAppBar(
        modifier = Modifier.height(100.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Welcome Aman", fontSize = 16.sp, color = softTextColor)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Void Chat",
                        fontSize = 32.sp,
                        color = accentPastel,
                        fontWeight = FontWeight.Medium
                    )
                }
                Icon(
                    imageVector = EvaIcons.Outline.Bell,
                    contentDescription = "Notifications",
                    tint = softWhite,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        backgroundColor = backgroundColor,
        elevation = 0.dp
    )
}

@Composable
private fun BottomNavigationBar(
    selectedIndex: Int, onItemSelected: (Int) -> Unit
) {
    val items = listOf("Calls", "Messages", "Profile")
    val icons = listOf(EvaIcons.Fill.PhoneCall, EvaIcons.Fill.MessageCircle, EvaIcons.Fill.Person)

    Surface(
        modifier = Modifier.wrapContentWidth().padding(bottom = 16.dp),
        color = Color(0xFF121212),
        shape = RoundedCornerShape(48.dp),
        elevation = 16.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex
                val color = if (isSelected) Color(0xFFFFD6A5) else softWhite

                Column(
                    modifier = Modifier.clickable
                    {
                        onItemSelected(index)

                    }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = label,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(text = label, color = color, fontSize = 12.sp)
                }
            }
        }
    }
}