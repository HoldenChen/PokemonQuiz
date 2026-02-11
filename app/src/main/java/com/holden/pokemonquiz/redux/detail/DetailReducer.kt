package com.holden.pokemonquiz.redux.detail

import com.toggl.komposable.architecture.Effect
import com.toggl.komposable.architecture.ReduceResult
import com.toggl.komposable.architecture.Reducer

/**
 * 详情的 reducer
 */
class DetailReducer(private val detailEffect: DetailEffect) : Reducer<DetailState, DetailAction> {

    override fun reduce(
        state: DetailState,
        action: DetailAction
    ): ReduceResult<DetailState, DetailAction> {
        return when (action) {
            is DetailAction.LoadPokemon -> {
                ReduceResult(
                    state = state.copy(isLoading = true, error = null),
                    effect = detailEffect.loadPokemon(action.pokemonId)
                )
            }

            is DetailAction.PokemonLoaded -> {
                ReduceResult(
                    state =
                        state.copy(
                            pokemon = action.pokemon,
                            isLoading = false,
                            error = null
                        ),
                    effect = Effect.none()
                )
            }

            is DetailAction.PokemonLoadFailed -> {
                ReduceResult(
                    state = state.copy(isLoading = false, error = action.error),
                    effect = Effect.none()
                )
            }
        }
    }
}
