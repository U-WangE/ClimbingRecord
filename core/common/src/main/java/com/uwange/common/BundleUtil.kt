package com.uwange.common

import android.os.Bundle

fun Bundle.getSafeValue(key: String): String {
    return try {
        getString(key)
            ?: getInt(key).toString()
    } catch (e: ClassCastException) {
        try {
            getLong(key).toString()
        } catch (e: ClassCastException) {
            try {
                getDouble(key).toString()
            } catch (e: ClassCastException) {
                try {
                    getBoolean(key).toString()
                } catch (e: ClassCastException) {
                    "unknown"
                }
            }
        }
    }
}