// Plugins declarados aqui e aplicados nos modulos. Responsavel: Integrante 1.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.gms) apply false
}
