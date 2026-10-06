package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.data.model.MapLayerMode
import com.example.data.model.ProviderItem
import com.example.data.model.ServiceCategory
import com.example.data.model.SortOption
import com.example.ui.components.CategoryFilterBar
import com.example.ui.components.InteractiveMapView
import com.example.ui.components.ProviderListSheet
import com.example.ui.components.TopHeaderBar
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SurfaceCanvas

@Composable
fun ExploreScreen(
    categories: List<ServiceCategory>,
    selectedCategory: ServiceCategory,
    onCategorySelected: (ServiceCategory) -> Unit,
    providers: List<ProviderItem>,
    selectedProviderId: String?,
    onProviderSelected: (String) -> Unit,
    onViewProfile: (ProviderItem) -> Unit,
    onCallNow: (ProviderItem) -> Unit,
    sortOption: SortOption,
    onSortOptionChanged: (SortOption) -> Unit,
    zoomLevel: Float,
    mapOffset: androidx.compose.ui.geometry.Offset,
    onPan: (androidx.compose.ui.geometry.Offset) -> Unit,
    mapLayerMode: MapLayerMode,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onRecenter: () -> Unit,
    onCycleLayer: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        // 1. Top Header Bar
        TopHeaderBar(
            onLocationClick = onRecenter,
            onFilterClick = onFilterClick
        )

        // 2. Horizontal Category Filter Bar
        CategoryFilterBar(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected
        )

        // 3. Main Area: Map View at Top + Overlapping Provider List Sheet at Bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Interactive Vector Map
            InteractiveMapView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                providers = providers,
                selectedProviderId = selectedProviderId,
                onProviderSelected = onProviderSelected,
                zoomLevel = zoomLevel,
                mapOffset = mapOffset,
                onPan = onPan,
                mapLayerMode = mapLayerMode,
                onZoomIn = onZoomIn,
                onZoomOut = onZoomOut,
                onRecenter = onRecenter,
                onCycleLayer = onCycleLayer
            )

            // Bottom Provider List Sheet overlapping the bottom of the map by 14dp
            ProviderListSheet(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 306.dp),
                providers = providers,
                selectedProviderId = selectedProviderId,
                onProviderSelected = onProviderSelected,
                onViewProfile = onViewProfile,
                onCallNow = onCallNow,
                sortOption = sortOption,
                onSortOptionChanged = onSortOptionChanged
            )
        }
    }
}
