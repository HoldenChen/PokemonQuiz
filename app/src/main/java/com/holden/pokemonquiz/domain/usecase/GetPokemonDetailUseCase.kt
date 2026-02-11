package com.holden.pokemonquiz.domain.usecase

import com.holden.pokemonquiz.architecture.UseCase
import com.holden.pokemonquiz.domain.model.Pokemon
import com.holden.pokemonquiz.domain.repository.PokemonRepository

// 详情用例实现类
class GetPokemonDetailUseCase(
    private val repository: PokemonRepository
) : UseCase<Int, Pokemon>() {

    override suspend fun execute(input: Int): Pokemon =
        repository.getPokemonDetail(input)
}
