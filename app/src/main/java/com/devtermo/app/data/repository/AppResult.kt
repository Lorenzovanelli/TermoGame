package com.devtermo.app.data.repository

import kotlin.coroutines.cancellation.CancellationException

/** Resultado padrao do Repository. O ViewModel so enxerga Success ou Error. */
sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val mensagem: String, val causa: Throwable? = null) : AppResult<Nothing>()
}

/** Executa o bloco, converte excecoes em AppResult.Error e preserva o cancelamento de coroutines. */
suspend inline fun <T> safeCall(mensagemErro: String, block: () -> T): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        AppResult.Error(mensagemErro, e)
    }
