package com.holden.pokemonquiz.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SearchResponseWrapper(val data: SearchResponseData)

@Serializable
data class SearchResponseData(
    @SerialName("pokemon_v2_pokemonspecies") val species: List<SpeciesApiModel>,
    @SerialName("pokemon_v2_pokemonspecies_aggregate") val aggregate: AggregateWrapper
)

@Serializable
data class AggregateWrapper(val aggregate: AggregateCount)

@Serializable
data class AggregateCount(val count: Int)

@Serializable
data class SpeciesApiModel(
    val id: Int,
    val name: String,
    @SerialName("capture_rate") val captureRate: Int,
    @SerialName("pokemon_v2_pokemoncolor") val color: ColorApiModel,
    @SerialName("pokemon_v2_pokemons") val pokemons: List<PokemonListApiModel>
)

@Serializable
data class ColorApiModel(val name: String)

@Serializable
data class PokemonListApiModel(val id: Int, val name: String)


@Serializable
data class DetailResponseWrapper(val data: DetailResponseData)

@Serializable
data class DetailResponseData(
    @SerialName("pokemon_v2_pokemon") val pokemons: List<PokemonDetailApiModel>
)

@Serializable
data class PokemonDetailApiModel(
    val id: Int,
    val name: String,
    @SerialName("pokemon_v2_pokemonabilities") val abilities: List<AbilityWrapper>
)

@Serializable
data class AbilityWrapper(@SerialName("pokemon_v2_ability") val ability: AbilityApiModel)

@Serializable
data class AbilityApiModel(val name: String)
