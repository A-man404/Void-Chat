package com.example.pages.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.accentPastel
import com.example.ui.theme.backgroundColor
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.MessageCircle
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.fill.PhoneCall
import compose.icons.evaicons.outline.Bell
import compose.icons.evaicons.outline.Person

@Composable
fun ChatListScreen() {

    var selectedTabIndex by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            ChatTopAppBar()
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BottomNavigationBar(
                    selectedIndex = selectedTabIndex,
                    onItemSelected = { selectedTabIndex = it }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                StoriesRow()
                ChatCard()
            }
        }
    }
}

@Composable
fun StoriesRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .height(150.dp)

    ) {
        Text(
            text = "Stories",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ChatTopAppBar() {
    TopAppBar(
        modifier = Modifier.height(100.dp),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Welcome Aman",
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Text(
                        "Void Chat",
                        fontWeight = FontWeight.Normal,
                        fontSize = 28.sp,
                        color = accentPastel
                    )
                }
                Icon(
                    imageVector = EvaIcons.Outline.Bell,
                    contentDescription = "Bell Icon",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        backgroundColor = backgroundColor,
        elevation = 0.dp
    )
}

@Composable
fun ChatCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxSize(),
        backgroundColor = Color.Green,
        shape = RoundedCornerShape(topStart = 56.dp, topEnd = 56.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 48.dp, start = 20.dp, end = 20.dp)) {

            Text(
                "Recent Chat",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 4.dp)
            ) {
                items(100) {
                    ChatItem()
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 2.dp,
                        color = Color.Black
                    )
                }
            }
        }


    }
}


@Composable
fun ChatItem() {
    Row(
        modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.Yellow),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = EvaIcons.Outline.Person,
            contentDescription = "null",
            modifier = Modifier.weight(1f).wrapContentWidth().size(40.dp)
        )
        Column(modifier = Modifier.fillMaxSize().background(Color.Magenta).weight(3f)) { }
    }

}

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf("Calls", "Messages", "Profile")
    val icons = listOf(EvaIcons.Fill.PhoneCall, EvaIcons.Fill.MessageCircle, EvaIcons.Fill.Person)

    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .padding(bottom = 16.dp),
        color = Color.Black,
        shape = RoundedCornerShape(48.dp),
        elevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex
                val iconColor = if (isSelected) Color(0xFFB59EFF) else Color.White
                val textColor = if (isSelected) Color(0xFFB59EFF) else Color.White

                Column(
                    modifier = Modifier
                        .clickable { onItemSelected(index) }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = label,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = label,
                        color = textColor,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
