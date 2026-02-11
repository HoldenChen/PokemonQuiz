package com.holden.pokemonquiz.architecture

/**
 *mapper 基类，用于映射服务端数据和 UI 展示数据
 */
fun interface Mapper<In, Out> {
    fun map(input: In): Out
}


fun <In, Out> Mapper<In, Out>.mapList(input: List<In>): List<Out> =
    input.map { map(it) }
