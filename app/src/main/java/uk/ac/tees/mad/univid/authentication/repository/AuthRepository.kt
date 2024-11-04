package uk.ac.tees.mad.univid.authentication.repository

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.univid.authentication.utils.Response

interface AuthRepository {

    fun LogInUser(email: String, password: String): Flow<Response<AuthResult>>

    fun SignOut()

    fun IsLoggedIn():Boolean

    fun RegisterUser(name: String, email: String, password: String): Flow<Response<AuthResult>>
}