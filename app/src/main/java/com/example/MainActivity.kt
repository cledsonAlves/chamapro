package com.example

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.MainTab
import com.example.ui.SosCasaViewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.BookingDispatchDialog
import com.example.ui.components.ChatDialog
import com.example.ui.components.FilterDialog
import com.example.ui.components.ProviderProfileDialog
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SurfaceCanvas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SosCasaApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SosCasaApp(viewModel: SosCasaViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedProviderId by viewModel.selectedProviderId.collectAsState()
    val providers by viewModel.filteredProviders.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val maxDistKm by viewModel.maxDistanceKm.collectAsState()
    val onlyVerified by viewModel.onlyVerified.collectAsState()
    val zoomLevel by viewModel.mapZoom.collectAsState()
    val mapOffset by viewModel.mapOffset.collectAsState()
    val mapLayerMode by viewModel.mapLayerMode.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val messages by viewModel.messages.collectAsState()

    val profileProvider by viewModel.profileProvider.collectAsState()
    val bookingProvider by viewModel.bookingProvider.collectAsState()
    val chatProvider by viewModel.chatProvider.collectAsState()
    val isFilterOpen by viewModel.isFilterSheetOpen.collectAsState()

    // Location permission request launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        viewModel.recenterMap()
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Hardware back navigation handler
    BackHandler(enabled = currentTab != MainTab.EXPLORAR) {
        viewModel.setTab(MainTab.EXPLORAR)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .windowInsetsPadding(WindowInsets.statusBars),
        bottomBar = {
            BottomNavBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.setTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.EXPLORAR -> {
                    ExploreScreen(
                        categories = viewModel.categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        providers = providers,
                        selectedProviderId = selectedProviderId,
                        onProviderSelected = { viewModel.selectProvider(it) },
                        onViewProfile = { viewModel.openProviderProfile(it) },
                        onCallNow = { viewModel.openBooking(it) },
                        sortOption = sortOption,
                        onSortOptionChanged = { viewModel.setSortOption(it) },
                        zoomLevel = zoomLevel,
                        mapOffset = mapOffset,
                        onPan = { viewModel.updateMapPan(it) },
                        mapLayerMode = mapLayerMode,
                        onZoomIn = { viewModel.zoomIn() },
                        onZoomOut = { viewModel.zoomOut() },
                        onRecenter = { viewModel.recenterMap() },
                        onCycleLayer = { viewModel.cycleMapLayer() },
                        onFilterClick = { viewModel.setFilterSheetOpen(true) }
                    )
                }

                MainTab.PEDIDOS -> {
                    OrdersScreen(
                        orders = orders,
                        onBackToMap = { viewModel.setTab(MainTab.EXPLORAR) },
                        onContactProvider = { provId ->
                            val prov = providers.find { it.id == provId }
                            if (prov != null) {
                                viewModel.openChat(prov)
                            }
                        }
                    )
                }

                MainTab.MENSAGENS -> {
                    MessagesScreen(
                        providers = providers,
                        messages = messages,
                        onOpenChat = { viewModel.openChat(it) }
                    )
                }

                MainTab.PERFIL -> {
                    ProfileScreen()
                }
            }
        }
    }

    // Modals & BottomSheets
    profileProvider?.let { provider ->
        val profileSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ProviderProfileDialog(
            provider = provider,
            onDismiss = { viewModel.closeProviderProfile() },
            onCallNow = {
                viewModel.closeProviderProfile()
                viewModel.openBooking(provider)
            },
            onOpenChat = {
                viewModel.closeProviderProfile()
                viewModel.openChat(provider)
            },
            sheetState = profileSheetState
        )
    }

    bookingProvider?.let { provider ->
        val bookingSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        BookingDispatchDialog(
            provider = provider,
            onDismiss = { viewModel.closeBooking() },
            onOrderConfirmed = { prov, address ->
                viewModel.confirmOrder(prov, address)
            },
            sheetState = bookingSheetState
        )
    }

    chatProvider?.let { provider ->
        val chatSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ChatDialog(
            provider = provider,
            messages = messages[provider.id] ?: emptyList(),
            onSendMessage = { text ->
                viewModel.sendChatMessage(provider.id, text)
            },
            onDismiss = { viewModel.closeChat() },
            sheetState = chatSheetState
        )
    }

    if (isFilterOpen) {
        val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        FilterDialog(
            currentSort = sortOption,
            currentMaxDistKm = maxDistKm,
            currentOnlyVerified = onlyVerified,
            onApply = { newSort, newDist, newVerified ->
                viewModel.setSortOption(newSort)
                viewModel.setMaxDistanceKm(newDist)
                viewModel.setOnlyVerified(newVerified)
            },
            onDismiss = { viewModel.setFilterSheetOpen(false) },
            sheetState = filterSheetState
        )
    }
}
