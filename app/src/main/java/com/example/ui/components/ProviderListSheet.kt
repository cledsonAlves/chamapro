package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProviderItem
import com.example.data.model.SortOption
import com.example.ui.theme.Black
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SurfaceCanvas
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProviderListSheet(
    providers: List<ProviderItem>,
    selectedProviderId: String?,
    onProviderSelected: (String) -> Unit,
    onViewProfile: (ProviderItem) -> Unit,
    onCallNow: (ProviderItem) -> Unit,
    sortOption: SortOption,
    onSortOptionChanged: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSortMenuOpen by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Scroll to selected item when selection changes from map
    LaunchedEffect(selectedProviderId) {
        if (selectedProviderId != null) {
            val index = providers.indexOfFirst { it.id == selectedProviderId }
            if (index >= 0) {
                listState.animateScrollToItem(index)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(PureWhite)
            .border(
                width = 1.dp,
                color = BorderSubtle,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
    ) {
        // Drag Handle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFFE0E0E0))
            )
        }

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Prestadores próximos",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Profissionais avaliados com chegada rápida",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            // Sort Pill Dropdown
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(SurfaceContainer)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(999.dp))
                        .clickable { isSortMenuOpen = true }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("sort_filter_pill"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sortOption.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Alterar ordenação",
                        tint = Black,
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = isSortMenuOpen,
                    onDismissRequest = { isSortMenuOpen = false },
                    modifier = Modifier.background(PureWhite)
                ) {
                    SortOption.values().forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.label,
                                    fontWeight = if (option == sortOption) FontWeight.Bold else FontWeight.Normal,
                                    color = if (option == sortOption) Black else TextSecondary
                                )
                            },
                            onClick = {
                                onSortOptionChanged(option)
                                isSortMenuOpen = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Providers Cards Stack
        if (providers.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SearchOff,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Nenhum prestador encontrado",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 15.sp
                )
                Text(
                    text = "Tente alterar o filtro de categoria ou aumentar o raio de distância.",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(providers, key = { it.id }) { provider ->
                    ProviderCard(
                        provider = provider,
                        isSelected = provider.id == selectedProviderId,
                        onCardClick = { onProviderSelected(provider.id) },
                        onViewProfile = { onViewProfile(provider) },
                        onCallNow = { onCallNow(provider) }
                    )
                }
            }
        }
    }
}
