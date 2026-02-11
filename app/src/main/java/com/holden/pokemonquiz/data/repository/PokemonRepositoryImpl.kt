package com.holden.pokemonquiz.data.repository

import com.holden.pokemonquiz.architecture.mapList
import com.holden.pokemonquiz.data.api.PokemonGraphQLApi
import com.holden.pokemonquiz.data.mapper.PokemonDetailApiToDomainMapper
import com.holden.pokemonquiz.data.mapper.SpeciesApiToDomainMapper
import com.holden.pokemonquiz.data.model.DetailResponseWrapper
import com.holden.pokemonquiz.data.model.SearchResponseWrapper
import com.holden.pokemonquiz.domain.model.Pokemon
import com.holden.pokemonquiz.domain.model.SearchResult
import com.holden.pokemonquiz.domain.repository.PokemonRepository

/**
 * PokemonRepository 的实现类。
 * */
class PokemonRepositoryImpl(
        private val api: PokemonGraphQLApi,
        private val speciesMapper: SpeciesApiToDomainMapper = SpeciesApiToDomainMapper(),
        private val detailMapper: PokemonDetailApiToDomainMapper = PokemonDetailApiToDomainMapper()
) : PokemonRepository {

    override suspend fun searchSpecies(query: String, limit: Int, offset: Int): SearchResult {
        val rawJson = api.searchSpecies(query, limit, offset)
        val response = api.json.decodeFromString<SearchResponseWrapper>(rawJson)

        return SearchResult(
                species = speciesMapper.mapList(response.data.species),
                totalCount = response.data.aggregate.aggregate.count
        )
    }

    override suspend fun getPokemonDetail(pokemonId: Int): Pokemon {
        val rawJson = api.getPokemonDetail(pokemonId)
        val response = api.json.decodeFromString<DetailResponseWrapper>(rawJson)

        val pokemonApi =
                response.data.pokemons.firstOrNull()
                        ?: throw RuntimeException("Pokémon with id=$pokemonId not found")

        return detailMapper.map(pokemonApi)
    }
}
