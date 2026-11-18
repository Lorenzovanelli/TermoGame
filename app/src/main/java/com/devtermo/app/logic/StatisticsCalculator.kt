package com.devtermo.app.logic

import com.devtermo.app.model.Partida
import com.devtermo.app.model.Resultados

data class EstatisticasJogador(
    val totalPartidas: Int = 0,
    val vitorias: Int = 0,
    val derrotas: Int = 0,
    val taxaDeAcerto: Float = 0f,        
    val pontuacaoTotal: Int = 0,
    val mediaTentativasVitoria: Float = 0f
)

object StatisticsCalculator {

    fun calcular(partidas: List<Partida>): EstatisticasJogador {
        if (partidas.isEmpty()) return EstatisticasJogador()
        val vitorias = partidas.count { it.resultado == Resultados.VITORIA }
        val tentativasNasVitorias = partidas
            .filter { it.resultado == Resultados.VITORIA }
            .map { it.tentativas }
        return EstatisticasJogador(
            totalPartidas = partidas.size,
            vitorias = vitorias,
            derrotas = partidas.size - vitorias,
            taxaDeAcerto = vitorias.toFloat() / partidas.size,
            pontuacaoTotal = partidas.sumOf { it.pontuacao },
            mediaTentativasVitoria = if (tentativasNasVitorias.isEmpty()) 0f
            else tentativasNasVitorias.average().toFloat()
        )
    }
}
