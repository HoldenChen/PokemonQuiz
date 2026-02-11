package com.holden.pokemonquiz.redux.detail

import com.holden.pokemonquiz.domain.model.Pokemon

// 详情页的页面状态
data class DetailState(
        val pokemon: Pokemon? = null,
        val isLoading: Boolean = false,
        val error: String? = null
)
