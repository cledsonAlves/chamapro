package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProviderItem
import com.example.ui.theme.Black
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SurfaceCanvas
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProviderCard(
    provider: ProviderItem,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onViewProfile: () -> Unit,
    onCallNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) Black else BorderSubtle
    val borderWidth = if (isSelected) 1.5.dp else 1.dp
    val bgColor = if (isSelected) Color(0xFFFAFAFA) else PureWhite

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(borderWidth, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onCardClick)
            .padding(14.dp)
            .testTag("provider_card_${provider.id}")
    ) {
        // Top section with Avatar, Name, Badge, Price, Description, Metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar with verified badge
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                if (provider.photoResId != null) {
                    Image(
                        painter = painterResource(id = provider.photoResId),
                        contentDescription = "Foto de ${provider.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(SurfaceContainer)
                            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = provider.name,
                            tint = TextSecondary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                if (provider.isVerified) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Black)
                            .border(1.dp, PureWhite, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Verificado",
                            tint = PureWhite,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info column
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = provider.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        provider.badge?.let { badgeText ->
                            val isPro = badgeText == "PRO"
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isPro) Black else SurfaceContainer)
                                    .border(
                                        width = if (isPro) 0.dp else 1.dp,
                                        color = BorderSubtle,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = badgeText,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPro) PureWhite else TextPrimary
                                )
                            }
                        }
                    }

                    // Price
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = provider.price,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = provider.priceUnit,
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 1.dp)
                        )
                    }
                }

                Text(
                    text = provider.description,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )

                // Metrics Row: Rating + Distance/ETA
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Rating Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainer)
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Avaliação",
                            tint = Black,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = String.format("%.1f", provider.rating),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "(${provider.ratingCount})",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    // Distance & ETA
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = "Distância",
                            tint = Black,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${provider.distanceFormatted} · ${provider.etaMinutes} min",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BorderHairline)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // CTAs Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onViewProfile,
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .testTag("btn_profile_${provider.id}"),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextPrimary,
                    containerColor = PureWhite
                )
            ) {
                Text(
                    text = "Ver Perfil",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            val isPlumber = provider.categoryCode == "ENCANADOR"
            val ctaText = if (isPlumber) "Chamar Agora" else if (provider.categoryCode == "ELETRICISTA") "Chamar" else "Agendar"

            Button(
                onClick = onCallNow,
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .testTag("btn_call_${provider.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPlumber || provider.categoryCode == "ELETRICISTA") Black else SurfaceContainer,
                    contentColor = if (isPlumber || provider.categoryCode == "ELETRICISTA") PureWhite else TextPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ElectricBolt,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = ctaText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
