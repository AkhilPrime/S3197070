package uk.ac.tees.mad.univid.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import uk.ac.tees.mad.univid.authentication.repository.AuthRepository
import uk.ac.tees.mad.univid.authentication.state.AuthState
import uk.ac.tees.mad.univid.authentication.utils.Response
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _authstate = MutableStateFlow<AuthState>(AuthState.idle)
    val authState = _authstate.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        checkIfUserLoggedIn()
    }

    private fun checkIfUserLoggedIn(){
        Log.i("the user: ", "The user is logged in already.")
        if (repository.IsLoggedIn()){
            _isLoggedIn.value = true
        }else{
            _isLoggedIn.value = false
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
}