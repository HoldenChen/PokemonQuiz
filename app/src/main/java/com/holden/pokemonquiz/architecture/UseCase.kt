package com.holden.pokemonquiz.architecture

/**
 * 遵循 Clean Architecture 的 base用例.
 */
abstract class UseCase<in Input, out Output> {
    suspend operator fun invoke(input: Input): Output = execute(input)
    protected abstract suspend fun execute(input: Input): Output
}


