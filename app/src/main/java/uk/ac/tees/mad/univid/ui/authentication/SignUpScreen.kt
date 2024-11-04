package uk.ac.tees.mad.univid.ui.authentication

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.univid.authentication.state.AuthState
import uk.ac.tees.mad.univid.authentication.viewmodel.AuthViewModel
import uk.ac.tees.mad.univid.ui.theme.poppinsFam
import uk.ac.tees.mad.univid.ui.theme.sansFam
import uk.ac.tees.mad.univid.ui.theme.skyblue


@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()

    when(authState){
        is AuthState.success->{
            navController.navigate("home_graph") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        is AuthState.failure->{
            Log.i("The error: ", "Authentication can't be completed")
            Toast.makeText(context, "Unable to Sign Up! Check the details", Toast.LENGTH_LONG).show()
            name = ""
            email = ""
            password = ""
        }
        else->{

        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.fillMaxWidth(0.9f),
                text = "Create an account",
                fontSize = 30.sp,
                fontFamily = sansFam,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                modifier = Modifier.fillMaxWidth(0.9f),
                text = "Welcome! Please Enter your details",
                fontSize = 15.sp,
                fontFamily = sansFam,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                modifier = Modifier.fillMaxWidth(0.88f),
                text = "Name",
                fontFamily = poppinsFam
            )
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                modifier = Modifier.fillMaxWidth(0.88f),
                shape = RoundedCornerShape(10.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email"
                    )
                },
                placeholder = {
                    Text(
                        text = "Enter your name",
                        fontSize = 14.sp,
                        fontFamily = poppinsFam
                    )
                }
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                modifier = Modifier.fillMaxWidth(0.88f),
                text = "Email",
                fontFamily = poppinsFam
            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                modifier = Modifier.fillMaxWidth(0.88f),
                shape = RoundedCornerShape(10.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email"
                    )
                },
                placeholder = {
                    Text(
                        text = "Enter your email",
                        fontSize = 14.sp,
                        fontFamily = poppinsFam
                    )
                }
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                text = "Password",
                modifier = Modifier.fillMaxWidth(0.88f),
                fontFamily = poppinsFam
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password=it
                },
                modifier = Modifier.fillMaxWidth(0.88f),
                shape = RoundedCornerShape(10.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "password"
                    )
                },
                placeholder = {
                    Text(
                        text = "Enter your password",
                        fontSize = 14.sp,
                        fontFamily = poppinsFam,
                    )
                },
                trailingIcon = {
                    val showPassword = if (passwordVisibility)
                        Icons.Outlined.Visibility
                    else{
                        Icons.Outlined.VisibilityOff
                    }

                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = showPassword,
                            contentDescription = "Password Visibility"
                        )
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.weight(0.2f))

            Row {
                Spacer(modifier = Modifier.weight(0.3f))
                Text(
                    text = "Already have an account?",
                    fontSize = 14.sp,
                    fontFamily = poppinsFam
                )
                Spacer(modifier = Modifier.weight(0.05f))
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate("login_screen")
                    },
                    text = "LogIn here",
                    fontSize = 14.sp,
                    fontFamily = poppinsFam,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(0.3f))
            }

            Spacer(modifier = Modifier.weight(0.2f))
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(45.dp),
                onClick = {
                    authViewModel.SignUp(name, email, password)
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = skyblue
                )
            ) {
                Text(
                    text = "Sign Up",
                    fontFamily = poppinsFam,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(modifier = Modifier.weight(1f))
        }
    }
}