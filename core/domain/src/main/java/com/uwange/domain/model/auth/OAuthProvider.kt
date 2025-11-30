package com.uwange.domain.model.auth

enum class OAuthProvider(val apiValue: String, val displayName: String) {
    KAKAO("kakao", "카카오");

    companion object {
        fun create(code: String?) : OAuthProvider? {
            return OAuthProvider.entries.firstOrNull { it.apiValue == code }
        }
    }
}