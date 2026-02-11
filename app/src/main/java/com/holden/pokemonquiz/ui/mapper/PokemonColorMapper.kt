package com.holden.pokemonquiz.ui.mapper

import androidx.compose.ui.graphics.Color


// 颜色映射
object PokemonColorMapper {

    private val colorMap =
            mapOf(
                    "black" to Color(0xFF303030),
                    "blue" to Color(0xFF6890F0),
                    "brown" to Color(0xFFB8A038),
                    "gray" to Color(0xFFA8A8A8),
                    "green" to Color(0xFF78C850),
                    "pink" to Color(0xFFF85888),
                    "purple" to Color(0xFF7038F8),
                    "red" to Color(0xFFF08030),
                    "white" to Color(0xFFE8E8E8),
                    "yellow" to Color(0xFFF8D030)
            )

    fun getColor(colorName: String): Color = colorMap[colorName.lowercase()] ?: Color(0xFFB0B0B0)


    //根据不同的背景色设置下字体颜色，保证字体可见
    fun getTextColor(colorName: String): Color {
        return when (colorName.lowercase()) {
            "black", "blue", "purple", "brown" -> Color.White
            else -> Color(0xFF1A1A1A)
        }
    }
}
