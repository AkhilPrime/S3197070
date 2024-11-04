package uk.ac.tees.mad.univid.authentication.state

sealed class AuthState{
    object idle: AuthState()
    object loading: AuthState()
    object success: AuthState()
    data class failure(val error: String): AuthState()
}