package com.example.data.model

import androidx.annotation.DrawableRes

enum class MainTab {
    EXPLORAR,
    PEDIDOS,
    MENSAGENS,
    PERFIL
}

enum class SortOption(val label: String) {
    FASTEST("Mais rápidos"),
    HIGHEST_RATED("Melhor avaliados"),
    LOWEST_PRICE("Menor preço")
}

enum class MapLayerMode(val label: String) {
    LIGHT("Minimalista Claro"),
    OBSIDIAN("Obsidian Noturno"),
    DETAILED("Grade Detalhada")
}

data class ServiceCategory(
    val code: String,
    val name: String,
    val iconName: String
)

data class ProviderReview(
    val author: String,
    val rating: Double,
    val comment: String,
    val date: String
)

data class ProviderItem(
    val id: String,
    val name: String,
    val categoryCode: String,
    val categoryName: String,
    val specialties: List<String>,
    val badge: String? = null, // "PRO", "CREA", etc.
    val price: String, // "R$ 90"
    val priceUnit: String = "/visita", // "/visita", "/h"
    val description: String,
    val rating: Double,
    val ratingCount: Int,
    val distanceMeters: Int,
    val distanceFormatted: String, // "500 m", "1,2 km"
    val etaMinutes: Int, // 5
    @DrawableRes val photoResId: Int? = null,
    val isVerified: Boolean = true,
    val vehicleType: String? = "MOTO", // "MOTO", "CARRO", "BICICLETA"
    val mapX: Float, // 0.0f .. 1.0f relative coordinate on vector map
    val mapY: Float,
    val phone: String = "(11) 98765-4321",
    val bio: String = "Profissional qualificado com mais de 8 anos de experiência no mercado paulistano. Atendimento rápido com garantia e nota fiscal.",
    val completedJobs: Int = 145,
    val warrantyDays: Int = 90,
    val reviews: List<ProviderReview> = emptyList()
)

enum class OrderStatus(val label: String, val colorHex: Long) {
    SEARCHING("Localizando", 0xFF656464),
    CONFIRMED("Confirmado", 0xFF10B981),
    EN_ROUTE("A caminho", 0xFF000000),
    IN_PROGRESS("Em atendimento", 0xFF2563EB),
    COMPLETED("Finalizado", 0xFF10B981),
    CANCELLED("Cancelado", 0xFFBA1A1A)
}

data class ServiceOrder(
    val id: String,
    val providerId: String,
    val providerName: String,
    val categoryName: String,
    val price: String,
    val etaMinutes: Int,
    val address: String,
    val status: OrderStatus,
    val requestedAt: String,
    @DrawableRes val providerPhotoRes: Int? = null
)

data class ChatMessage(
    val id: String,
    val providerId: String,
    val sender: String, // "user" or "provider"
    val text: String,
    val time: String
)
