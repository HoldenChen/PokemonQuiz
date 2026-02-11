package com.holden.pokemonquiz.domain.usecase

import com.holden.pokemonquiz.architecture.UseCase
import com.holden.pokemonquiz.domain.model.SearchResult
import com.holden.pokemonquiz.domain.repository.PokemonRepository


//搜索用例实现类
class SearchPokemonUseCase(
    private val repository: PokemonRepository
) : UseCase<SearchPokemonUseCase.Params, SearchResult>() {

    data class Params(
        val query: String,
        val pageSize: Int = 10,
        val pageIndex: Int = 0
    )

    override suspend fun execute(input: Params): SearchResult =
        repository.searchSpecies(
            query = input.query,
            limit = input.pageSize,
            offset = input.pageIndex
        )
}
