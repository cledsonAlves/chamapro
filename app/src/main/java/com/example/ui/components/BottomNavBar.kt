package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MainTab
import com.example.ui.theme.Black
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun BottomNavBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp)
            .background(PureWhite)
            .border(width = 1.dp, color = BorderHairline)
            .navigationBarsPadding()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            title = "Explorar",
            selectedIcon = Icons.Filled.Explore,
            unselectedIcon = Icons.Outlined.Explore,
            isSelected = currentTab == MainTab.EXPLORAR,
            onClick = { onTabSelected(MainTab.EXPLORAR) },
            testTag = "tab_explorar"
        )

        BottomNavItem(
            title = "Pedidos",
            selectedIcon = Icons.Filled.Assignment,
            unselectedIcon = Icons.Outlined.Assignment,
            isSelected = currentTab == MainTab.PEDIDOS,
            onClick = { onTabSelected(MainTab.PEDIDOS) },
            testTag = "tab_pedidos"
        )

        BottomNavItem(
            title = "Mensagens",
            selectedIcon = Icons.Filled.Chat,
            unselectedIcon = Icons.Outlined.Chat,
            isSelected = currentTab == MainTab.MENSAGENS,
            onClick = { onTabSelected(MainTab.MENSAGENS) },
            showBadge = true,
            testTag = "tab_mensagens"
        )

        BottomNavItem(
            title = "Perfil",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            isSelected = currentTab == MainTab.PERFIL,
            onClick = { onTabSelected(MainTab.PERFIL) },
            testTag = "tab_perfil"
        )
    }
}

@Composable
private fun BottomNavItem(
    title: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    showBadge: Boolean = false,
    testTag: String
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = title,
                tint = if (isSelected) Black else TextMuted,
                modifier = Modifier.size(24.dp)
            )

            if (showBadge && !isSelected) {
                Box(
                    modifier = Modifier
                        .offset(x = 3.dp, y = (-2).dp)
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Black)
                )
            }
        }

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Black else TextMuted,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
