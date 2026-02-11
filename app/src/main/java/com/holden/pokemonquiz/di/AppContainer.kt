package com.holden.pokemonquiz.di

import com.holden.pokemonquiz.data.api.PokemonGraphQLApi
import com.holden.pokemonquiz.data.repository.PokemonRepositoryImpl
import com.holden.pokemonquiz.domain.repository.PokemonRepository
import com.holden.pokemonquiz.domain.usecase.GetPokemonDetailUseCase
import com.holden.pokemonquiz.domain.usecase.SearchPokemonUseCase
import com.holden.pokemonquiz.redux.PokemonQuizStoreManager
import com.holden.pokemonquiz.redux.detail.DetailEffect
import com.holden.pokemonquiz.redux.detail.DetailReducer
import com.holden.pokemonquiz.redux.search.SearchEffect
import com.holden.pokemonquiz.redux.search.SearchReducer
import com.toggl.komposable.scope.DispatcherProvider
import com.toggl.komposable.scope.StoreScopeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/** 精简的手动依赖注入 */
object AppContainer {

    private val api: PokemonGraphQLApi by lazy { PokemonGraphQLApi() }
    private val repository: PokemonRepository by lazy { PokemonRepositoryImpl(api) }

    private val searchPokemonUseCase: SearchPokemonUseCase by lazy {
        SearchPokemonUseCase(repository)
    }
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase by lazy {
        GetPokemonDetailUseCase(repository)
    }

    private val searchEffect: SearchEffect by lazy { SearchEffect(searchPokemonUseCase) }
    private val detailEffect: DetailEffect by lazy { DetailEffect(getPokemonDetailUseCase) }

    private val searchReducer: SearchReducer by lazy { SearchReducer(searchEffect) }
    private val detailReducer: DetailReducer by lazy { DetailReducer(detailEffect) }

    private val storeScopeProvider = StoreScopeProvider {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    private val dispatcherProvider =
            DispatcherProvider(
                    io = Dispatchers.IO,
                    computation = Dispatchers.Default,
                    main = Dispatchers.Main
            )

    val storeManager: PokemonQuizStoreManager by lazy {
        PokemonQuizStoreManager(
                searchReducer = searchReducer,
                detailReducer = detailReducer,
                storeScopeProvider = storeScopeProvider,
                dispatcherProvider = dispatcherProvider
        )
    }
}
