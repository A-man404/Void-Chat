package com.example.pages.mainTabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.accentPastel
import com.example.ui.theme.backgroundColor
import com.example.ui.theme.lightCardColor
import com.example.ui.theme.softTextColor
import com.example.ui.theme.softWhite
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.Plus

@Composable
fun ChatListScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(backgroundColor)
    ) {
        StoriesRow()
        ChatCard()
    }
}

@Composable
private fun StoriesRow() {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .height(150.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        item { AddStory() }
        items(10) { StoryItem() }
    }
}

@Composable
private fun StoryItem() {
    val pastelColors = listOf(
        Color(0xFFFFD6A5),
        Color(0xFFA5D8FF),
        Color(0xFFB59EFF),
        Color(0xFFB5F8D4)
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier.size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF2C2B3A))
                .border(2.dp, pastelColors.random(), CircleShape)
                .shadow(4.dp, CircleShape)
        ) {
            Image(
                imageVector = EvaIcons.Outline.Person, colorFilter = ColorFilter.tint(softWhite),
                contentDescription = "Story profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "HELLO",
            style = TextStyle(color = softWhite, fontSize = 12.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AddStory() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier.size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF252535))
                .border(2.dp, Color(0xFFFBE7A1), CircleShape)
                .shadow(4.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = EvaIcons.Outline.Plus,
                contentDescription = "Add Story",
                tint = softWhite,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Add",
            style = TextStyle(color = softWhite, fontSize = 12.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ChatCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxSize(),
        backgroundColor = lightCardColor,
        shape = RoundedCornerShape(topStart = 56.dp, topEnd = 56.dp)
    ) {

        Column(modifier = Modifier.fillMaxSize().padding(top = 48.dp, start = 20.dp, end = 20.dp)) {
            Text(
                "Recent Chat",
                fontSize = 24.sp,
                color = softWhite,
                fontWeight = FontWeight.SemiBold

            )
            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
                items(10) {
                    ChatItem()
                    Divider(color = backgroundColor, thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
private fun ChatItem() {
    Row(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth()
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFF2D2C3C))
                .border(2.dp, Color(0xFFFBE7A1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = EvaIcons.Outline.Person,
                contentDescription = "User",
                modifier = Modifier.size(28.dp),
                tint = Color(0xFFE0E0E0)
            )
        }

        Column(modifier = Modifier.weight(3f)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Aman", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = softWhite)
                Text("00:21", color = accentPastel)
            }
            Text(
                "Last message preview here...",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = softTextColor
            )
        }
    }
}

