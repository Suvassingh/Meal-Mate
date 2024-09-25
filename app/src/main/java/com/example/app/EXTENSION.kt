package com.example.app

import java.security.MessageDigest

fun String.validatePassword():Boolean{
    return this.isNotEmpty() && this.length>=6
}
fun String.toSHA256():String{
    val HEX_CHARS = "0123456789ABCDE"
    val digest = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return digest.joinToString  (
        separator = "",
        transform = {a->
            String(
                charArrayOf(
                    HEX_CHARS[a.toInt() shr 4 and 0x0f],
                    HEX_CHARS[a.toInt() and 0x0f]
                )
            )
        }
    )
}
