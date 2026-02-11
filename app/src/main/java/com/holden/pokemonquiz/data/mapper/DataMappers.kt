package com.holden.pokemonquiz.data.mapper

import com.holden.pokemonquiz.architecture.Mapper
import com.holden.pokemonquiz.data.model.PokemonDetailApiModel
import com.holden.pokemonquiz.data.model.PokemonListApiModel
import com.holden.pokemonquiz.data.model.SpeciesApiModel
import com.holden.pokemonquiz.domain.model.Pokemon
import com.holden.pokemonquiz.domain.model.PokemonSpecies


/**
 * 接口数据模型转页面数据模型，在接口改动的情况下，无需改动 UI 数据明星。
 * */

class SpeciesApiToDomainMapper : Mapper<SpeciesApiModel, PokemonSpecies> {
    override fun map(input: SpeciesApiModel): PokemonSpecies =
        PokemonSpecies(
            id = input.id,
            name = input.name,
            captureRate = input.captureRate,
            colorName = input.color.name,
            pokemons = input.pokemons.map { it.toDomain() }
        )

    private fun PokemonListApiModel.toDomain() =
        Pokemon(
            id = id,
            name = name,
            abilities = emptyList()
        )
}

class PokemonDetailApiToDomainMapper : Mapper<PokemonDetailApiModel, Pokemon> {
    override fun map(input: PokemonDetailApiModel): Pokemon =
        Pokemon(
            id = input.id,
            name = input.name,
            abilities = input.abilities.map { it.ability.name }
        )
}
