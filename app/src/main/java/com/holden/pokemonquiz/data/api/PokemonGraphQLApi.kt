package com.holden.pokemonquiz.data.api

import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 使用 GraphQL API 查询 Pokemon 数据。
 * */
class PokemonGraphQLApi(
    private val client: OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
) {
    companion object {
        private const val GRAPHQL_URL = "https://beta.pokeapi.co/graphql/v1beta"
        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    }

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * 搜索 Pokémon 物种。支持模糊搜索。
     *
     */
    suspend fun searchSpecies(query: String, limit: Int, offset: Int): String {
        val graphqlQuery =
            """
        {
            "query": "query SearchSpecies(${'$'}name: String!, ${'$'}limit: Int!, ${'$'}offset: Int!) { pokemon_v2_pokemonspecies(where: {name: {_ilike: ${'$'}name}}, limit: ${'$'}limit, offset: ${'$'}offset, order_by: {id: asc}) { id name capture_rate pokemon_v2_pokemoncolor { name } pokemon_v2_pokemons { id name } } pokemon_v2_pokemonspecies_aggregate(where: {name: {_ilike: ${'$'}name}}) { aggregate { count } } }",
            "variables": {
                "name": "%${query.replace("\"", "")}%",
                "limit": $limit,
                "offset": $offset
            }
        }
        """.trimIndent()

        return executeQuery(graphqlQuery)
    }

    suspend fun getPokemonDetail(pokemonId: Int): String {
        val graphqlQuery =
            """
        {
            "query": "query GetPokemonDetail(${'$'}id: Int!) { pokemon_v2_pokemon(where: {id: {_eq: ${'$'}id}}) { id name pokemon_v2_pokemonabilities { pokemon_v2_ability { name } } } }",
            "variables": {
                "id": $pokemonId
            }
        }
        """.trimIndent()

        return executeQuery(graphqlQuery)
    }

    private suspend fun executeQuery(body: String): String =
        withContext(Dispatchers.IO) {
            val request =
                Request.Builder()
                    .url(GRAPHQL_URL)
                    .post(body.toRequestBody(JSON_MEDIA_TYPE))
                    .header("Content-Type", "application/json")
                    .build()

            val response = client.newCall(request).execute()
            response.body?.string() ?: throw RuntimeException("Empty response body")
        }
}
