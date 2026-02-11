package com.holden.pokemonquiz.redux.search

import com.toggl.komposable.architecture.Effect
import com.toggl.komposable.architecture.ReduceResult
import com.toggl.komposable.architecture.Reducer

class SearchReducer(private val searchEffect: SearchEffect) : Reducer<SearchState, SearchAction> {

    override fun reduce(
            state: SearchState,
            action: SearchAction
    ): ReduceResult<SearchState, SearchAction> {
        return when (action) {
            is SearchAction.QueryChanged -> {
                ReduceResult(state = state.copy(query = action.query), effect = Effect.none())
            }
            is SearchAction.PerformSearch -> {
                if (action.query.isBlank()) {
                    ReduceResult(
                            state =
                                    state.copy(
                                            results = emptyList(),
                                            isLoading = false,
                                            totalCount = 0,
                                            error = null
                                    ),
                            effect = Effect.none()
                    )
                } else {
                    ReduceResult(
                            state =
                                    state.copy(
                                            isLoading = true,
                                            results = emptyList(),
                                            error = null
                                    ),
                            effect = searchEffect.performSearch(action.query)
                    )
                }
            }
            is SearchAction.SearchResultsLoaded -> {
                ReduceResult(
                        state =
                                state.copy(
                                        results = action.species,
                                        totalCount = action.totalCount,
                                        hasMore = action.species.size < action.totalCount,
                                        isLoading = false,
                                        error = null
                                ),
                        effect = Effect.none()
                )
            }
            is SearchAction.LoadMore -> {
                ReduceResult(
                        state = state.copy(isLoading = true),
                        effect = searchEffect.loadMore(state.query, state.results.size)
                )
            }
            is SearchAction.MoreResultsLoaded -> {
                val allResults = state.results + action.species
                ReduceResult(
                        state =
                                state.copy(
                                        results = allResults,
                                        hasMore = allResults.size < state.totalCount,
                                        isLoading = false,
                                        error = null
                                ),
                        effect = Effect.none()
                )
            }
            is SearchAction.SearchFailed -> {
                ReduceResult(
                        state = state.copy(isLoading = false, error = action.error),
                        effect = Effect.none()
                )
            }
        }
    }
}
