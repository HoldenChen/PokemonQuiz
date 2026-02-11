package com.holden.pokemonquiz.domain.repository

import com.holden.pokemonquiz.domain.model.Pokemon
import com.holden.pokemonquiz.domain.model.SearchResult


interface PokemonRepository {

    /**
     * @param query search string (use _ilike with wildcards)
     * @param limit page size
     * @param offset pagination offset
     */
    suspend fun searchSpecies(query: String, limit: Int, offset: Int): SearchResult


    suspend fun getPokemonDetail(pokemonId: Int): Pokemon
}
