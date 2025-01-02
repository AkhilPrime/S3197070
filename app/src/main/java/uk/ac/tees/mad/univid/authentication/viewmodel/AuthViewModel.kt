package uk.ac.tees.mad.univid.authentication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.univid.authentication.model.CurrentUser
import uk.ac.tees.mad.univid.authentication.repository.AuthRepository
import uk.ac.tees.mad.univid.authentication.state.AuthState
import uk.ac.tees.mad.univid.authentication.utils.Response
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {

    val storage = Firebase.storage.reference

    private val _authstate = MutableStateFlow<AuthState>(AuthState.idle)
    val authState = _authstate.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<CurrentUser?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        checkIfUserLoggedIn()
    }

    private fun checkIfUserLoggedIn(){
        if (repository.IsLoggedIn()){
            _isLoggedIn.value = true
            fetchCurrentUser()
        }else{
            _isLoggedIn.value = false
            fetchCurrentUser()
        }
    }


    fun LogIn(email: String, password: String) = viewModelScope.launch{
        repository.LogInUser(email, password).collect{res->
            when(res){
                is Response.Success->{
                    _authstate.value = AuthState.success
                    Log.i("The response:", "The user is authenticated")
                }

                is Response.Loading->{
                    _authstate.value = AuthState.loading
                    Log.i("The response: ", "Loading user")
                }

                is Response.Error->{
                    _authstate.value = AuthState.failure("${res.message}")
                }
            }

        }
    }

    fun SignUp(name: String, email: String, password: String) = viewModelScope.launch{
        repository.RegisterUser(name, email, password).collect{res->
            when(res){
                is Response.Success->{
                    _authstate.value = AuthState.success
                    Log.i("The registration: ", "The user is registered successfully")
                }

                is Response.Loading->{
                    _authstate.value = AuthState.loading
                    Log.i("The registration: ", "The registration is loading")
                }

                is Response.Error->{
                    _authstate.value = AuthState.failure(res.message.toString())
                    Log.i("The registration: ", "The user is not registered ${res.message}")
                }
            }
        }
    }

    fun signOut(){
        repository.SignOut()
        _authstate.value = AuthState.idle
        _isLoggedIn.value = false
    }

    fun fetchCurrentUser(){
        val user = auth.currentUser
        _authstate.value = AuthState.loading
        if (user != null){
            val userId = user.uid
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val name = it.getString("name") ?: ""
                        val email = it.getString("email") ?: ""
                        val imgUrl = it.getString("profilepictureurl") ?: ""
                        val fetchedUser = CurrentUser(name, email, imgUrl)
                        _currentUser.value = fetchedUser
                        _authstate.value = AuthState.success
                    }else{
                        signOut()
                    }
                }
                .addOnFailureListener {
                    signOut()
                    _authstate.value = AuthState.failure(it.message.toString())
                    Log.i("The error: ", "Cannot fetch the user")
                }
        }
    }

    fun updateCurrentUser(name: String, email: String, password: String){
        val currUser = auth.currentUser
        if (currUser!=null){
            val userId = currUser.uid
            val userData = hashMapOf(
                "name" to name,
                "email" to email
            )
            val crediential = EmailAuthProvider.getCredential(currUser.email!!, password)
            currUser.reauthenticate(crediential)
                .addOnCompleteListener {
                    firestore.collection("users")
                        .document(userId)
                        .update(userData as Map<String, Any>)
                }
                .addOnSuccessListener {
                    currUser.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                Log.i("The Email: ", email)
                                currUser.updateEmail(email).addOnCompleteListener {
                                    fetchCurrentUser()
                                }
                            }
                        }
                }
                .addOnFailureListener {
                    _authstate.value = AuthState.failure("Unable to re-authenticate the user")
                }
        }else{
            _authstate.value = AuthState.failure("Unable to authenticate")
        }
    }

    fun updateProfileImage(uri: Uri){
        val currentUser = auth.currentUser
        if (currentUser!=null){
            val userId = currentUser.uid
            val imageRef = storage.child("users/${userId}/profile.jpg")

            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener {
                        val imageLink = it.toString()

                        val userData = hashMapOf(
                            "profilepictureurl" to imageLink)
                        firestore.collection("users")
                            .document(userId)
                            .update(userData as Map<String, Any>)
                            .addOnSuccessListener {
                                fetchCurrentUser()
                            }
                    }
                }
                .addOnFailureListener{
                    Log.i("Error Encountered: ", "Unable to update the profile picture.")
                }
        }else{
            Log.i("Error update:", "Current User is null")
        }
    }
}