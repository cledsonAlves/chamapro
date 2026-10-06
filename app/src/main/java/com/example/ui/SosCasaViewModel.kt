package com.example.ui

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatMessage
import com.example.data.model.MainTab
import com.example.data.model.MapLayerMode
import com.example.data.model.OrderStatus
import com.example.data.model.ProviderItem
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceOrder
import com.example.data.model.SortOption
import com.example.data.repository.ProviderRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SosCasaViewModel(
    private val repository: ProviderRepository = ProviderRepository()
) : ViewModel() {

    val categories: List<ServiceCategory> = repository.categories

    private val _selectedCategory = MutableStateFlow(categories.first())
    val selectedCategory: StateFlow<ServiceCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.FASTEST)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _maxDistanceKm = MutableStateFlow(10f)
    val maxDistanceKm: StateFlow<Float> = _maxDistanceKm.asStateFlow()

    private val _onlyVerified = MutableStateFlow(false)
    val onlyVerified: StateFlow<Boolean> = _onlyVerified.asStateFlow()

    private val _selectedProviderId = MutableStateFlow<String?>("prov-joao")
    val selectedProviderId: StateFlow<String?> = _selectedProviderId.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.EXPLORAR)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _profileProvider = MutableStateFlow<ProviderItem?>(null)
    val profileProvider: StateFlow<ProviderItem?> = _profileProvider.asStateFlow()

    private val _bookingProvider = MutableStateFlow<ProviderItem?>(null)
    val bookingProvider: StateFlow<ProviderItem?> = _bookingProvider.asStateFlow()

    private val _chatProvider = MutableStateFlow<ProviderItem?>(null)
    val chatProvider: StateFlow<ProviderItem?> = _chatProvider.asStateFlow()

    private val _isFilterSheetOpen = MutableStateFlow(false)
    val isFilterSheetOpen: StateFlow<Boolean> = _isFilterSheetOpen.asStateFlow()

    private val _mapZoom = MutableStateFlow(1.0f)
    val mapZoom: StateFlow<Float> = _mapZoom.asStateFlow()

    private val _mapOffset = MutableStateFlow(Offset.Zero)
    val mapOffset: StateFlow<Offset> = _mapOffset.asStateFlow()

    private val _mapLayerMode = MutableStateFlow(MapLayerMode.LIGHT)
    val mapLayerMode: StateFlow<MapLayerMode> = _mapLayerMode.asStateFlow()

    val orders: StateFlow<List<ServiceOrder>> = repository.orders
    val messages: StateFlow<Map<String, List<ChatMessage>>> = repository.messages

    val filteredProviders: StateFlow<List<ProviderItem>> = combine(
        _selectedCategory,
        _searchQuery,
        _sortOption,
        _maxDistanceKm,
        _onlyVerified
    ) { category, query, sort, maxDist, verified ->
        repository.getProviders(
            categoryCode = category.code,
            searchQuery = query,
            sortOption = sort,
            maxDistanceKm = maxDist,
            onlyVerified = verified
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        repository.getProviders("TODOS", "", SortOption.FASTEST)
    )

    fun selectCategory(category: ServiceCategory) {
        _selectedCategory.value = category
        val currentList = repository.getProviders(
            categoryCode = category.code,
            searchQuery = _searchQuery.value,
            sortOption = _sortOption.value
        )
        if (currentList.isNotEmpty() && currentList.none { it.id == _selectedProviderId.value }) {
            _selectedProviderId.value = currentList.first().id
        }
    }

    fun selectProvider(providerId: String?) {
        _selectedProviderId.value = providerId
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setMaxDistanceKm(km: Float) {
        _maxDistanceKm.value = km
    }

    fun setOnlyVerified(verified: Boolean) {
        _onlyVerified.value = verified
    }

    fun setFilterSheetOpen(open: Boolean) {
        _isFilterSheetOpen.value = open
    }

    fun openProviderProfile(provider: ProviderItem) {
        _profileProvider.value = provider
    }

    fun closeProviderProfile() {
        _profileProvider.value = null
    }

    fun openBooking(provider: ProviderItem) {
        _bookingProvider.value = provider
    }

    fun closeBooking() {
        _bookingProvider.value = null
    }

    fun openChat(provider: ProviderItem) {
        _chatProvider.value = provider
    }

    fun closeChat() {
        _chatProvider.value = null
    }

    fun sendChatMessage(providerId: String, text: String) {
        if (text.isNotBlank()) {
            repository.sendMessage(providerId, text)
            // Auto response simulation for high fidelity
            viewModelScope.launch {
                delay(1200)
                val replies = listOf(
                    "Entendido! Já estou preparando as ferramentas.",
                    "Perfeito, chegarei pontualmente no endereço informado.",
                    "Combinado! Qualquer dúvida pode me chamar aqui."
                )
                val reply = replies.random()
                val current = repository.messages.value[providerId]?.toMutableList() ?: mutableListOf()
                current.add(
                    ChatMessage(
                        id = System.currentTimeMillis().toString(),
                        providerId = providerId,
                        sender = "provider",
                        text = reply,
                        time = "Agora"
                    )
                )
                val updated = repository.messages.value.toMutableMap()
                updated[providerId] = current
            }
        }
    }

    fun confirmOrder(provider: ProviderItem, address: String): ServiceOrder {
        val order = repository.createOrder(provider, address)
        _bookingProvider.value = null
        _currentTab.value = MainTab.PEDIDOS
        return order
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun zoomIn() {
        _mapZoom.value = (_mapZoom.value + 0.25f).coerceAtMost(2.5f)
    }

    fun zoomOut() {
        _mapZoom.value = (_mapZoom.value - 0.25f).coerceAtLeast(0.75f)
    }

    fun recenterMap() {
        _mapOffset.value = Offset.Zero
        _mapZoom.value = 1.0f
        // Also ensure user or closest provider is selected
        _selectedProviderId.value = "prov-joao"
    }

    fun cycleMapLayer() {
        _mapLayerMode.value = when (_mapLayerMode.value) {
            MapLayerMode.LIGHT -> MapLayerMode.OBSIDIAN
            MapLayerMode.OBSIDIAN -> MapLayerMode.DETAILED
            MapLayerMode.DETAILED -> MapLayerMode.LIGHT
        }
    }

    fun updateMapPan(pan: Offset) {
        _mapOffset.value = Offset(
            x = (_mapOffset.value.x + pan.x).coerceIn(-400f, 400f),
            y = (_mapOffset.value.y + pan.y).coerceIn(-400f, 400f)
        )
    }
}
