package com.example

import com.example.data.model.SortOption
import com.example.data.repository.ProviderRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SosCasaUnitTest {

    private lateinit var repository: ProviderRepository

    @Before
    fun setUp() {
        repository = ProviderRepository()
    }

    @Test
    fun testCategoriesAvailable() {
        val categories = repository.categories
        assertTrue("Deve haver categorias disponíveis", categories.isNotEmpty())
        assertEquals("Primeira categoria deve ser TODOS", "TODOS", categories.first().code)
        assertTrue("Deve conter Encanador", categories.any { it.code == "ENCANADOR" })
        assertTrue("Deve conter Eletricista", categories.any { it.code == "ELETRICISTA" })
    }

    @Test
    fun testFilterProvidersByCategory() {
        val all = repository.getProviders("TODOS", "", SortOption.FASTEST)
        assertTrue("Deve retornar todos os prestadores", all.size >= 5)

        val plumbers = repository.getProviders("ENCANADOR", "", SortOption.FASTEST)
        assertTrue("Deve retornar encanadores", plumbers.isNotEmpty())
        assertTrue("Todos devem ser encanadores", plumbers.all { it.categoryCode == "ENCANADOR" })
    }

    @Test
    fun testSortProvidersByFastest() {
        val fastest = repository.getProviders("TODOS", "", SortOption.FASTEST)
        for (i in 0 until fastest.size - 1) {
            assertTrue(
                "Ordem por menor ETA",
                fastest[i].etaMinutes <= fastest[i + 1].etaMinutes
            )
        }
    }

    @Test
    fun testSortProvidersByRating() {
        val rated = repository.getProviders("TODOS", "", SortOption.HIGHEST_RATED)
        for (i in 0 until rated.size - 1) {
            assertTrue(
                "Ordem por maior avaliação",
                rated[i].rating >= rated[i + 1].rating
            )
        }
    }

    @Test
    fun testCreateOrder() {
        val provider = repository.getProviderById("prov-joao")
        assertNotNull("Prestador João deve existir", provider)

        val initialCount = repository.orders.value.size
        val order = repository.createOrder(provider!!, "Av. Paulista, 1000")

        assertEquals("Prestador correto no pedido", "prov-joao", order.providerId)
        assertEquals("Quantidade de pedidos incrementada", initialCount + 1, repository.orders.value.size)
    }

    @Test
    fun testSendMessage() {
        val providerId = "prov-joao"
        repository.sendMessage(providerId, "Olá João, teste de mensagem!")
        val msgs = repository.messages.value[providerId]
        assertNotNull("Deve haver mensagens para João", msgs)
        assertTrue("Deve conter a nova mensagem", msgs!!.any { it.text.contains("teste de mensagem") })
    }
}
