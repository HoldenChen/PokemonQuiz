package com.holden.pokemonquiz.redux

import com.holden.pokemonquiz.redux.detail.DetailAction
import com.holden.pokemonquiz.redux.detail.DetailReducer
import com.holden.pokemonquiz.redux.detail.DetailState
import com.holden.pokemonquiz.redux.search.SearchAction
import com.holden.pokemonquiz.redux.search.SearchReducer
import com.holden.pokemonquiz.redux.search.SearchState
import com.toggl.komposable.architecture.Reducer
import com.toggl.komposable.architecture.Store
import com.toggl.komposable.extensions.combine
import com.toggl.komposable.extensions.createStore
import com.toggl.komposable.extensions.pullback
import com.toggl.komposable.scope.DispatcherProvider
import com.toggl.komposable.scope.StoreScopeProvider


class PokemonQuizStoreManager(
    searchReducer: SearchReducer,
    detailReducer: DetailReducer,
    storeScopeProvider: StoreScopeProvider,
    dispatcherProvider: DispatcherProvider
) {
    private val searchPullback: Reducer<PokemonQuizAppState, PokemonQuizAppAction> =
        searchReducer.pullback(
            mapToLocalState = { globalState: PokemonQuizAppState ->
                globalState.searchState
            },
            mapToLocalAction = { globalAction: PokemonQuizAppAction ->
                (globalAction as? PokemonQuizAppAction.SearchActions)?.action
            },
            mapToGlobalState = { globalState: PokemonQuizAppState,
                                 localState: SearchState ->
                globalState.copy(searchState = localState)
            },
            mapToGlobalAction = { localAction: SearchAction ->
                PokemonQuizAppAction.SearchActions(localAction)
            }
        )

    private val detailPullback: Reducer<PokemonQuizAppState, PokemonQuizAppAction> =
        detailReducer.pullback(
            mapToLocalState = { globalState: PokemonQuizAppState ->
                globalState.detailState
            },
            mapToLocalAction = { globalAction: PokemonQuizAppAction ->
                (globalAction as? PokemonQuizAppAction.DetailActions)?.action
            },
            mapToGlobalState = { globalState: PokemonQuizAppState,
                                 localState: DetailState ->
                globalState.copy(detailState = localState)
            },
            mapToGlobalAction = { localAction: DetailAction ->
                PokemonQuizAppAction.DetailActions(localAction)
            }
        )

    private val appReducer = combine(searchPullback, detailPullback)

    val appStore: Store<PokemonQuizAppState, PokemonQuizAppAction> by lazy {
        createStore(
            initialState = PokemonQuizAppState(),
            reducer = appReducer,
            storeScopeProvider = storeScopeProvider,
            dispatcherProvider = dispatcherProvider
        )
    }

    val searchStore: Store<SearchState, SearchAction> by lazy {
        appStore.view(
            mapToLocalState = { it.searchState },
            mapToGlobalAction = { PokemonQuizAppAction.SearchActions(it) }
        )
    }

    val detailStore: Store<DetailState, DetailAction> by lazy {
        appStore.view(
            mapToLocalState = { it.detailState },
            mapToGlobalAction = { PokemonQuizAppAction.DetailActions(it) }
        )
    }
}
