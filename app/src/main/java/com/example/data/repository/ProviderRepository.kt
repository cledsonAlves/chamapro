package com.example.data.repository

import com.example.R
import com.example.data.model.ChatMessage
import com.example.data.model.OrderStatus
import com.example.data.model.ProviderItem
import com.example.data.model.ProviderReview
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceOrder
import com.example.data.model.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProviderRepository {

    val categories = listOf(
        ServiceCategory("TODOS", "Todos", "apps"),
        ServiceCategory("ENCANADOR", "Encanador", "plumbing"),
        ServiceCategory("ELETRICISTA", "Eletricista", "bolt"),
        ServiceCategory("PINTOR", "Pintor", "format_paint"),
        ServiceCategory("CHAVEIRO", "Chaveiro", "vpn_key"),
        ServiceCategory("AR_CONDICIONADO", "Ar-condicionado", "ac_unit"),
        ServiceCategory("JARDINEIRO", "Jardineiro", "yard"),
        ServiceCategory("DIARISTA", "Diarista", "cleaning_services")
    )

    private val allProviders = listOf(
        ProviderItem(
            id = "prov-joao",
            name = "João Silva",
            categoryCode = "ENCANADOR",
            categoryName = "Encanador",
            specialties = listOf("Desentupimentos", "Caça Vazamentos", "Tubulação PPR/PVC", "Torneiras e Registros"),
            badge = "PRO",
            price = "R$ 90",
            priceUnit = "/visita",
            description = "Encanador Profissional & Desentupimentos",
            rating = 4.9,
            ratingCount = 120,
            distanceMeters = 500,
            distanceFormatted = "500 m",
            etaMinutes = 5,
            photoResId = R.drawable.avatar_joao,
            isVerified = true,
            vehicleType = "MOTO",
            mapX = 0.38f,
            mapY = 0.35f,
            phone = "(11) 98123-4567",
            bio = "Especialista em reparos hidráulicos residenciais e comerciais na região central de SP. Equipamento eletrônico de detecção de vazamentos.",
            completedJobs = 184,
            warrantyDays = 90,
            reviews = listOf(
                ProviderReview("Renata Salgado", 5.0, "Chegou em 7 minutos, resolveu o vazamento na pia super rápido e deixou tudo limpo!", "Hoje"),
                ProviderReview("Marcos B.", 4.8, "Muito atencioso, explicou todo o problema no encanamento e cobrou o preço justo.", "Há 2 dias"),
                ProviderReview("Helena V.", 5.0, "Profissional nota 10. Recomendo muito para emergências.", "Há 1 semana")
            )
        ),
        ProviderItem(
            id = "prov-maria",
            name = "Maria Fernandes",
            categoryCode = "ELETRICISTA",
            categoryName = "Eletricista",
            specialties = listOf("Quadro de Disjuntores", "Curto-Circuito", "Instalação 220V", "Iluminação LED"),
            badge = "CREA",
            price = "R$ 110",
            priceUnit = "/visita",
            description = "Eletricista Residencial & Padrão de Luz",
            rating = 5.0,
            ratingCount = 89,
            distanceMeters = 1200,
            distanceFormatted = "1,2 km",
            etaMinutes = 7,
            photoResId = R.drawable.avatar_maria,
            isVerified = true,
            vehicleType = "CARRO",
            mapX = 0.72f,
            mapY = 0.20f,
            phone = "(11) 97654-3210",
            bio = "Engenheira Eletricista com registro ativo no CREA-SP. Certificação NR-10 para serviços elétricos em baixa e média tensão com laudo técnico.",
            completedJobs = 142,
            warrantyDays = 120,
            reviews = listOf(
                ProviderReview("Claudia Martins", 5.0, "Perfeita. Encontrou um curto no painel principal que outros dois não conseguiram.", "Ontem"),
                ProviderReview("Luciano Trindade", 5.0, "Instalou chuveiro Lorenzetti e balanceou as fases do apartamento com segurança.", "Há 3 dias")
            )
        ),
        ProviderItem(
            id = "prov-carlos",
            name = "Carlos Mendes",
            categoryCode = "REPAROS",
            categoryName = "Reformas e Reparos",
            specialties = listOf("Fixação de TV", "Cortinas e Persianas", "Pequenos Reparos", "Montagem de Móveis"),
            badge = null,
            price = "R$ 80",
            priceUnit = "/h",
            description = "Reparos e Reformas em Geral",
            rating = 4.8,
            ratingCount = 64,
            distanceMeters = 2000,
            distanceFormatted = "2,0 km",
            etaMinutes = 12,
            photoResId = R.drawable.avatar_carlos,
            isVerified = true,
            vehicleType = "MOTO",
            mapX = 0.22f,
            mapY = 0.68f,
            phone = "(11) 99876-5432",
            bio = "Marido de aluguel completo, pontual e muito organizado. Ferramentas a laser para alinhamento perfeito de quadros e armários.",
            completedJobs = 98,
            warrantyDays = 60,
            reviews = listOf(
                ProviderReview("Ana Paula", 5.0, "Excelente profissional, montou 3 prateleiras e trocou uma fechadura.", "Há 4 dias"),
                ProviderReview("Bruno Lima", 4.6, "Serviço muito bem feito e rápido.", "Há 1 semana")
            )
        ),
        ProviderItem(
            id = "prov-marcos",
            name = "Marcos Oliveira",
            categoryCode = "PINTOR",
            categoryName = "Pintor",
            specialties = listOf("Pintura Látex e Acrílica", "Massa Corrida", "Efeito Cimento Queimado", "Pintura de Portas"),
            badge = "PRO",
            price = "R$ 95",
            priceUnit = "/h",
            description = "Pintor Profissional Fino & Acabamentos",
            rating = 4.9,
            ratingCount = 78,
            distanceMeters = 2300,
            distanceFormatted = "2,3 km",
            etaMinutes = 15,
            photoResId = R.drawable.avatar_joao,
            isVerified = true,
            vehicleType = "MOTO",
            mapX = 0.82f,
            mapY = 0.62f,
            phone = "(11) 98234-5678",
            bio = "Pintura residencial sem sujeira. Proteção completa de pisos e móveis com lona e fita especial.",
            completedJobs = 115,
            warrantyDays = 90,
            reviews = listOf(
                ProviderReview("Carla S.", 5.0, "Pintou a sala em um dia, acabamento impecável!", "Há 5 dias")
            )
        ),
        ProviderItem(
            id = "prov-roberto",
            name = "Roberto Chaves",
            categoryCode = "CHAVEIRO",
            categoryName = "Chaveiro",
            specialties = listOf("Abertura Emergencial", "Troca de Segredo", "Fechaduras Digitais", "Chaves Automotivas"),
            badge = "24H",
            price = "R$ 75",
            priceUnit = "/visita",
            description = "Chaveiro Residencial & Aberturas Rápidas",
            rating = 4.7,
            ratingCount = 112,
            distanceMeters = 1600,
            distanceFormatted = "1,6 km",
            etaMinutes = 9,
            photoResId = R.drawable.avatar_carlos,
            isVerified = true,
            vehicleType = "MOTO",
            mapX = 0.50f,
            mapY = 0.78f,
            phone = "(11) 97123-8899",
            bio = "Plantão 24 horas para abertura de portas residenciais e cofres sem danificar a estrutura.",
            completedJobs = 210,
            warrantyDays = 30,
            reviews = listOf(
                ProviderReview("Gabriel P.", 5.0, "Abriu a porta em 5 minutos quando fiquei trancado para fora à noite.", "Ontem")
            )
        ),
        ProviderItem(
            id = "prov-amanda",
            name = "Amanda Climatização",
            categoryCode = "AR_CONDICIONADO",
            categoryName = "Ar-condicionado",
            specialties = listOf("Higienização Completa", "Carga de Gás", "Manutenção Preventiva", "Instalação Split"),
            badge = "PRO",
            price = "R$ 130",
            priceUnit = "/visita",
            description = "Especialista em Ar-condicionado & Climatização",
            rating = 4.9,
            ratingCount = 95,
            distanceMeters = 2800,
            distanceFormatted = "2,8 km",
            etaMinutes = 18,
            photoResId = R.drawable.avatar_maria,
            isVerified = true,
            vehicleType = "CARRO",
            mapX = 0.16f,
            mapY = 0.38f,
            phone = "(11) 96543-2198",
            bio = "Técnica certificada pelas principais marcas (Daikin, LG, Samsung). Higienização com produtos antibactericidas hospitalares.",
            completedJobs = 153,
            warrantyDays = 90,
            reviews = listOf(
                ProviderReview("Tiago Ramos", 5.0, "O ar-condicionado ficou novo e sem cheiro algum. Muito cuidadosa.", "Há 3 dias")
            )
        )
    )

    private val _orders = MutableStateFlow<List<ServiceOrder>>(
        listOf(
            ServiceOrder(
                id = "ORD-8492",
                providerId = "prov-joao",
                providerName = "João Silva",
                categoryName = "Encanador",
                price = "R$ 90",
                etaMinutes = 5,
                address = "Av. Paulista, 1578, Apto 82",
                status = OrderStatus.CONFIRMED,
                requestedAt = "Hoje às 14:32",
                providerPhotoRes = R.drawable.avatar_joao
            ),
            ServiceOrder(
                id = "ORD-7311",
                providerId = "prov-maria",
                providerName = "Maria Fernandes",
                categoryName = "Eletricista",
                price = "R$ 110",
                etaMinutes = 0,
                address = "Rua Augusta, 1200",
                status = OrderStatus.COMPLETED,
                requestedAt = "04/10/2026",
                providerPhotoRes = R.drawable.avatar_maria
            )
        )
    )
    val orders: StateFlow<List<ServiceOrder>> = _orders.asStateFlow()

    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "prov-joao" to listOf(
                ChatMessage("1", "prov-joao", "user", "Olá João, você tem disponibilidade para verificar um vazamento na cozinha agora?", "14:30"),
                ChatMessage("2", "prov-joao", "provider", "Olá! Sim, acabei de finalizar um atendimento aqui na Alameda Santos. Em 5 minutos chego aí!", "14:31"),
                ChatMessage("3", "prov-joao", "user", "Perfeito! Apto 82, interfone liberado.", "14:32")
            ),
            "prov-maria" to listOf(
                ChatMessage("4", "prov-maria", "user", "Oi Maria, obrigado pelo atendimento no quadro de luz na semana passada. Ficou ótimo!", "10:15"),
                ChatMessage("5", "prov-maria", "provider", "Disponha sempre! Se precisar de qualquer ajuste na fiação é só chamar.", "10:20")
            )
        )
    )
    val messages: StateFlow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

    fun getProviders(
        categoryCode: String,
        searchQuery: String,
        sortOption: SortOption,
        maxDistanceKm: Float = 10f,
        onlyVerified: Boolean = false
    ): List<ProviderItem> {
        var list = allProviders

        if (categoryCode != "TODOS") {
            list = list.filter { it.categoryCode == categoryCode }
        }

        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                    it.categoryName.lowercase().contains(q) ||
                    it.description.lowercase().contains(q) ||
                    it.specialties.any { spec -> spec.lowercase().contains(q) }
            }
        }

        if (onlyVerified) {
            list = list.filter { it.isVerified && it.badge != null }
        }

        list = list.filter { (it.distanceMeters / 1000f) <= maxDistanceKm }

        return when (sortOption) {
            SortOption.FASTEST -> list.sortedBy { it.etaMinutes }
            SortOption.HIGHEST_RATED -> list.sortedByDescending { it.rating }
            SortOption.LOWEST_PRICE -> list.sortedBy {
                it.price.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
            }
        }
    }

    fun getProviderById(id: String): ProviderItem? {
        return allProviders.find { it.id == id }
    }

    fun createOrder(provider: ProviderItem, address: String): ServiceOrder {
        val newOrder = ServiceOrder(
            id = "ORD-" + (1000..9999).random(),
            providerId = provider.id,
            providerName = provider.name,
            categoryName = provider.categoryName,
            price = provider.price,
            etaMinutes = provider.etaMinutes,
            address = address,
            status = OrderStatus.CONFIRMED,
            requestedAt = "Hoje às 15:00",
            providerPhotoRes = provider.photoResId
        )
        _orders.value = listOf(newOrder) + _orders.value
        return newOrder
    }

    fun sendMessage(providerId: String, text: String) {
        val current = _messages.value[providerId]?.toMutableList() ?: mutableListOf()
        val userMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            providerId = providerId,
            sender = "user",
            text = text,
            time = "Agora"
        )
        current.add(userMsg)

        val updated = _messages.value.toMutableMap()
        updated[providerId] = current
        _messages.value = updated
    }
}
