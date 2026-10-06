package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MapLayerMode
import com.example.data.model.ProviderItem
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.Black
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import kotlin.math.roundToInt

@Composable
fun InteractiveMapView(
    modifier: Modifier = Modifier,
    providers: List<ProviderItem>,
    selectedProviderId: String?,
    onProviderSelected: (String) -> Unit,
    zoomLevel: Float,
    mapOffset: Offset,
    onPan: (Offset) -> Unit,
    mapLayerMode: MapLayerMode,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onRecenter: () -> Unit,
    onCycleLayer: () -> Unit
) {
    // Pulse animation for active pins and user location
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 2.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    BoxWithConstraints(
        modifier = modifier
            .background(
                if (mapLayerMode == MapLayerMode.OBSIDIAN) Color(0xFF1E1E1E) else Color(0xFFF4F4F4)
            )
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onPan(dragAmount)
                }
            }
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        // 1. Vector Map Canvas Drawing Streets, Roads, Parks & Water
        Canvas(modifier = Modifier.fillMaxSize()) {
            val isDark = mapLayerMode == MapLayerMode.OBSIDIAN
            val roadBaseColor = if (isDark) Color(0xFF2A2A2A) else Color(0xFFDEDEDE)
            val roadSurfaceColor = if (isDark) Color(0xFF383838) else Color(0xFFFFFFFF)
            val parkFillColor = if (isDark) Color(0xFF252B24) else Color(0xFFEAEAEA)
            val parkBorderColor = if (isDark) Color(0xFF30362F) else Color(0xFFDFDFDF)
            val waterColor = if (isDark) Color(0xFF1F2833) else Color(0xFFE0E5EA)
            val gridColor = if (isDark) Color(0xFF242424) else Color(0xFFE9E9E9)

            // Grid background pattern
            val gridSpacing = 48f * zoomLevel
            val startX = (mapOffset.x % gridSpacing)
            val startY = (mapOffset.y % gridSpacing)

            var gx = startX - gridSpacing
            while (gx < size.width + gridSpacing) {
                drawLine(
                    color = gridColor,
                    start = Offset(gx, 0f),
                    end = Offset(gx, size.height),
                    strokeWidth = 1f
                )
                gx += gridSpacing
            }
            var gy = startY - gridSpacing
            while (gy < size.height + gridSpacing) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, gy),
                    end = Offset(size.width, gy),
                    strokeWidth = 1f
                )
                gy += gridSpacing
            }

            // Park Polygon (Parque Trianon)
            val parkPath = Path().apply {
                val p1 = Offset(widthPx * 0.62f + mapOffset.x, heightPx * 0.32f + mapOffset.y)
                val p2 = Offset(widthPx * 0.82f + mapOffset.x, heightPx * 0.28f + mapOffset.y)
                val p3 = Offset(widthPx * 0.88f + mapOffset.x, heightPx * 0.48f + mapOffset.y)
                val p4 = Offset(widthPx * 0.70f + mapOffset.x, heightPx * 0.54f + mapOffset.y)
                moveTo(p1.x, p1.y)
                lineTo(p2.x, p2.y)
                lineTo(p3.x, p3.y)
                lineTo(p4.x, p4.y)
                close()
            }
            drawPath(path = parkPath, color = parkFillColor)
            drawPath(
                path = parkPath,
                color = parkBorderColor,
                style = Stroke(width = 1.5f)
            )

            // Water stream / Canal (subtle curved stream)
            val waterPath = Path().apply {
                moveTo(-50f + mapOffset.x, heightPx * 0.85f + mapOffset.y)
                cubicTo(
                    widthPx * 0.25f + mapOffset.x, heightPx * 0.88f + mapOffset.y,
                    widthPx * 0.45f + mapOffset.x, heightPx * 0.75f + mapOffset.y,
                    widthPx * 1.1f + mapOffset.x, heightPx * 0.95f + mapOffset.y
                )
            }
            drawPath(
                path = waterPath,
                color = waterColor,
                style = Stroke(width = 14f * zoomLevel, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Arterial Highway 1 (Diagonal - Avenida Paulista)
            val paulistaPath = Path().apply {
                moveTo(-40f + mapOffset.x, heightPx * 0.20f + mapOffset.y)
                quadraticTo(
                    widthPx * 0.35f + mapOffset.x, heightPx * 0.36f + mapOffset.y,
                    widthPx * 0.55f + mapOffset.x, heightPx * 0.62f + mapOffset.y
                )
                lineTo(widthPx * 1.15f + mapOffset.x, heightPx * 0.76f + mapOffset.y)
            }
            // Road base outline
            drawPath(
                path = paulistaPath,
                color = roadBaseColor,
                style = Stroke(width = 24f * zoomLevel, cap = StrokeCap.Round)
            )
            // Road inner surface
            drawPath(
                path = paulistaPath,
                color = roadSurfaceColor,
                style = Stroke(width = 18f * zoomLevel, cap = StrokeCap.Round)
            )

            // Arterial Road 2 (Vertical - Rua da Consolação)
            val consolacaoPath = Path().apply {
                moveTo(widthPx * 0.32f + mapOffset.x, -30f + mapOffset.y)
                lineTo(widthPx * 0.38f + mapOffset.x, heightPx + 30f + mapOffset.y)
            }
            drawPath(
                path = consolacaoPath,
                color = roadBaseColor,
                style = Stroke(width = 20f * zoomLevel)
            )
            drawPath(
                path = consolacaoPath,
                color = roadSurfaceColor,
                style = Stroke(width = 14f * zoomLevel)
            )

            // Arterial Road 3 (Rua Augusta / Alameda Santos)
            val augustaPath = Path().apply {
                moveTo(widthPx * 0.78f + mapOffset.x, 20f + mapOffset.y)
                lineTo(widthPx * 0.48f + mapOffset.x, heightPx + 40f + mapOffset.y)
            }
            drawPath(
                path = augustaPath,
                color = roadBaseColor,
                style = Stroke(width = 18f * zoomLevel)
            )
            drawPath(
                path = augustaPath,
                color = roadSurfaceColor,
                style = Stroke(width = 12f * zoomLevel)
            )

            // Residential secondary cross streets
            val street1 = Path().apply {
                moveTo(30f + mapOffset.x, heightPx * 0.55f + mapOffset.y)
                lineTo(widthPx * 0.7f + mapOffset.x, heightPx * 0.42f + mapOffset.y)
            }
            drawPath(
                path = street1,
                color = roadSurfaceColor,
                style = Stroke(width = 9f * zoomLevel)
            )

            val street2 = Path().apply {
                moveTo(widthPx * 0.2f + mapOffset.x, heightPx * 0.25f + mapOffset.y)
                lineTo(widthPx * 0.95f + mapOffset.x, heightPx * 0.22f + mapOffset.y)
            }
            drawPath(
                path = street2,
                color = roadSurfaceColor,
                style = Stroke(width = 8f * zoomLevel)
            )

            val street3 = Path().apply {
                moveTo(widthPx * 0.42f + mapOffset.x, heightPx * 0.65f + mapOffset.y)
                lineTo(widthPx * 0.95f + mapOffset.x, heightPx * 0.85f + mapOffset.y)
            }
            drawPath(
                path = street3,
                color = roadSurfaceColor,
                style = Stroke(width = 9f * zoomLevel)
            )
        }

        // 2. Street Typography Overlays on Map
        Text(
            text = "AV. PAULISTA",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 1.sp,
            modifier = Modifier
                .offset {
                    IntOffset(
                        (widthPx * 0.24f + mapOffset.x).roundToInt(),
                        (heightPx * 0.20f + mapOffset.y).roundToInt()
                    )
                }
        )

        Text(
            text = "PQ. TRIANON",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 1.sp,
            modifier = Modifier
                .offset {
                    IntOffset(
                        (widthPx * 0.64f + mapOffset.x).roundToInt(),
                        (heightPx * 0.42f + mapOffset.y).roundToInt()
                    )
                }
        )

        // 3. User Current Location Dot (Uber Black Dot with animated pulse)
        val userX = widthPx * 0.50f + mapOffset.x
        val userY = heightPx * 0.52f + mapOffset.y

        Box(
            modifier = Modifier
                .offset {
                    IntOffset((userX - 16.dp.toPx()).roundToInt(), (userY - 16.dp.toPx()).roundToInt())
                }
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Pulse wave
            Box(
                modifier = Modifier
                    .size(28.dp * pulseScale)
                    .clip(CircleShape)
                    .background(Black.copy(alpha = pulseAlpha))
            )
            // Solid center marker
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Black)
                    .border(2.dp, PureWhite, CircleShape)
            )
        }

        // 4. Provider Geolocation Markers
        providers.forEach { provider ->
            val isSelected = provider.id == selectedProviderId
            val markerX = widthPx * provider.mapX + mapOffset.x
            val markerY = heightPx * provider.mapY + mapOffset.y

            ProviderMapMarker(
                provider = provider,
                isSelected = isSelected,
                pulseScale = pulseScale,
                pulseAlpha = pulseAlpha,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (markerX - 32.dp.toPx()).roundToInt(),
                            (markerY - 32.dp.toPx()).roundToInt()
                        )
                    }
                    .clickable { onProviderSelected(provider.id) }
                    .testTag("marker_${provider.id}")
            )
        }

        // 5. Floating Map Control Buttons (Layers, Zoom In, Zoom Out, Recenter)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MapFloatingButton(
                icon = Icons.Default.Layers,
                contentDescription = "Camadas do mapa",
                onClick = onCycleLayer,
                testTag = "map_layer_button"
            )
            MapFloatingButton(
                icon = Icons.Default.Add,
                contentDescription = "Aumentar zoom",
                onClick = onZoomIn,
                testTag = "map_zoom_in_button"
            )
            MapFloatingButton(
                icon = Icons.Default.Remove,
                contentDescription = "Diminuir zoom",
                onClick = onZoomOut,
                testTag = "map_zoom_out_button"
            )
            MapFloatingButton(
                icon = Icons.Default.MyLocation,
                contentDescription = "Recentralizar localização",
                onClick = onRecenter,
                testTag = "map_recenter_button"
            )
        }

        // 6. Map Availability Pill (Bottom Left)
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 18.dp)
                .shadow(4.dp, RoundedCornerShape(999.dp))
                .clip(RoundedCornerShape(999.dp))
                .background(PureWhite.copy(alpha = 0.95f))
                .border(1.dp, BorderSubtle, RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(Black)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "${providers.size} disponíveis no raio de 2 km",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun ProviderMapMarker(
    provider: ProviderItem,
    isSelected: Boolean,
    pulseScale: Float,
    pulseAlpha: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(if (isSelected) 46.dp else 36.dp)
        ) {
            if (isSelected) {
                // Outer ping circle
                Box(
                    modifier = Modifier
                        .size(38.dp * pulseScale)
                        .clip(CircleShape)
                        .background(Black.copy(alpha = pulseAlpha))
                )
            }

            // Central pin circle
            Box(
                modifier = Modifier
                    .size(if (isSelected) 38.dp else 32.dp)
                    .shadow(if (isSelected) 8.dp else 4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(if (isSelected) Black else Color(0xFF1E1E1E))
                    .border(2.dp, PureWhite, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val icon = getCategoryIcon(provider.categoryCode)
                Icon(
                    imageVector = icon,
                    contentDescription = provider.name,
                    tint = PureWhite,
                    modifier = Modifier.size(if (isSelected) 19.dp else 16.dp)
                )
            }
        }

        // Label Pill below pin
        if (isSelected) {
            Row(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .shadow(6.dp, RoundedCornerShape(999.dp))
                    .clip(RoundedCornerShape(999.dp))
                    .background(Black)
                    .border(1.dp, PureWhite.copy(alpha = 0.25f), RoundedCornerShape(999.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(AccentGreen)
                )
                Spacer(modifier = Modifier.width(4.dp))
                val firstName = provider.name.split(" ").firstOrNull() ?: provider.name
                Text(
                    text = "$firstName · ${provider.etaMinutes} min",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            }
        } else {
            val firstName = provider.name.split(" ").firstOrNull() ?: provider.name
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .shadow(2.dp, RoundedCornerShape(999.dp))
                    .clip(RoundedCornerShape(999.dp))
                    .background(PureWhite)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(999.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = firstName,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )
            }
        }
    }
}

@Composable
private fun MapFloatingButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    testTag: String
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = PureWhite.copy(alpha = 0.95f),
            contentColor = Black
        ),
        modifier = Modifier
            .size(38.dp)
            .shadow(2.dp, RoundedCornerShape(10.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(19.dp)
        )
    }
}

fun getCategoryIcon(code: String): ImageVector {
    return when (code) {
        "ENCANADOR" -> Icons.Default.Plumbing
        "ELETRICISTA" -> Icons.Default.ElectricBolt
        "PINTOR" -> Icons.Default.FormatPaint
        "CHAVEIRO" -> Icons.Default.Key
        "AR_CONDICIONADO" -> Icons.Default.AcUnit
        "REPAROS" -> Icons.Default.Handyman
        else -> Icons.Default.Build
    }
}
