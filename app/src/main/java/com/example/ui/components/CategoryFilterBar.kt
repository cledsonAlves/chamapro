package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceCategory
import com.example.ui.theme.Black
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.TextPrimary

@Composable
fun CategoryFilterBar(
    categories: List<ServiceCategory>,
    selectedCategory: ServiceCategory,
    onCategorySelected: (ServiceCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(PureWhite)
            .border(width = 1.dp, color = BorderSubtle)
            .horizontalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            val isSelected = category.code == selectedCategory.code
            CategoryPill(
                category = category,
                isSelected = isSelected,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun CategoryPill(
    category: ServiceCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Black else SurfaceContainer,
        animationSpec = tween(180),
        label = "pillBg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) PureWhite else TextPrimary,
        animationSpec = tween(180),
        label = "pillText"
    )

    val iconVector: ImageVector = when (category.code) {
        "TODOS" -> Icons.Default.GridView
        "ENCANADOR" -> Icons.Default.Plumbing
        "ELETRICISTA" -> Icons.Default.ElectricBolt
        "PINTOR" -> Icons.Default.FormatPaint
        "CHAVEIRO" -> Icons.Default.Key
        "AR_CONDICIONADO" -> Icons.Default.AcUnit
        "JARDINEIRO" -> Icons.Default.Yard
        "DIARISTA" -> Icons.Default.CleaningServices
        else -> Icons.Default.GridView
    }

    Row(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = category.name,
            tint = contentColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = category.name,
            color = contentColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
