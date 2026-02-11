package com.holden.pokemonquiz.domain.model


data class PokemonSpecies(
    val id: Int,
    val name: String,
    val captureRate: Int,
    val colorName: String,
    val pokemons: List<Pokemon>
)


data class Pokemon(
    val id: Int,
    val name: String,
    val abilities: List<String>
)


data class SearchResult(
    val species: List<PokemonSpecies>,
    val totalCount: Int
)
