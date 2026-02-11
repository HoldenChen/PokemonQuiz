package com.holden.pokemonquiz.redux.search

import com.holden.pokemonquiz.domain.usecase.SearchPokemonUseCase
import com.toggl.komposable.architecture.Effect
import kotlinx.coroutines.flow.flow


class SearchEffect(private val searchPokemonUseCase: SearchPokemonUseCase) {
    companion object {
        const val PAGE_SIZE = 10
    }

    fun performSearch(query: String): Effect<SearchAction> = Effect {
        flow {
            try {
                val params =
                    SearchPokemonUseCase.Params(query = query, pageSize = PAGE_SIZE, pageIndex = 0)
                val result = searchPokemonUseCase(params)
                emit(
                    SearchAction.SearchResultsLoaded(
                        species = result.species,
                        totalCount = result.totalCount
                    )
                )
            } catch (e: Exception) {
                emit(SearchAction.SearchFailed(e.message ?: "Unknown error"))
            }
        }
    }

    fun loadMore(query: String, currentOffset: Int): Effect<SearchAction> = Effect {
        flow {
            try {
                val params =
                    SearchPokemonUseCase.Params(
                        query = query,
                        pageSize = PAGE_SIZE,
                        pageIndex = currentOffset
                    )
                val result = searchPokemonUseCase(params)
                emit(SearchAction.MoreResultsLoaded(result.species))
            } catch (e: Exception) {
                emit(SearchAction.SearchFailed(e.message ?: "Unknown error"))
            }
        }
    }
}
