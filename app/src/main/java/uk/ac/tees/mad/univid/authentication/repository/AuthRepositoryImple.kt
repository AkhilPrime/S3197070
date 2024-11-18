package uk.ac.tees.mad.univid.authentication.repository

import androidx.compose.ui.util.fastCbrt
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.univid.authentication.model.CurrentUser
import uk.ac.tees.mad.univid.authentication.utils.Response
import javax.inject.Inject

class AuthRepositoryImple @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthRepository {

    override fun LogInUser(email: String, password: String): Flow<Response<AuthResult>> {
        return flow {
            emit(Response.Loading())
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(Response.Success(result))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }
    }

    override fun SignOut() {
        auth.signOut()
    }

    override fun IsLoggedIn(): Boolean {
        val user = auth.currentUser
        if (user!=null)return true
        else return false
    }

    override fun RegisterUser(name: String, email: String, password: String): Flow<Response<AuthResult>> {
        return flow {
            try {
                emit(Response.Loading())

                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = auth.currentUser

                if (user != null) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user.updateProfile(profileUpdates).await()

                    val userData = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "profilepictureurl" to null
                    )

                    val userId = user.uid
                    firestore.collection("users").document(userId).set(userData).await()

                    emit(Response.Success(authResult))
                } else {
                    emit(Response.Error("User registration failed: currentUser is null"))
                }
            } catch (e: Exception) {
                emit(Response.Error(e.message.toString()))
            }
        }
    }

}