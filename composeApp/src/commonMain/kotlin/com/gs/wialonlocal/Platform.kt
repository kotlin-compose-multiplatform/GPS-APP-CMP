package com.gs.wialonlocal

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform