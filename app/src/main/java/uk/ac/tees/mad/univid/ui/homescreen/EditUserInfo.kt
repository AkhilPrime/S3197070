package uk.ac.tees.mad.univid.ui.homescreen

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.authentication.viewmodel.AuthViewModel
import uk.ac.tees.mad.univid.mainscreen.viewmodel.HomeViewModel
import uk.ac.tees.mad.univid.ui.theme.poppinsFam
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EditUserInformation(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    homeViewModel: HomeViewModel
){

    val width = LocalConfiguration.current.screenWidthDp.dp * 0.6f

    val context = LocalContext.current

    val currentUser by authViewModel.currentUser.collectAsState()
    var updatedName by remember{ mutableStateOf(currentUser?.name ?: "") }
    var updatedEmail by remember { mutableStateOf(currentUser?.email ?: "") }
    var password by remember { mutableStateOf("") }
    var passwordVisi by remember { mutableStateOf(false) }
    var showImageDialog by remember { mutableStateOf(false) }

    val cameraPermissionGranted = remember {
        mutableStateOf(false)
    }
    val galleryPermissionGranted = remember {
        mutableStateOf(false)
    }

    //Syncing the updated current user.
    LaunchedEffect(currentUser) {
        authViewModel.fetchCurrentUser()
        updatedName = currentUser?.name ?: ""
        updatedEmail = currentUser?.email ?: ""
    }

    //Selecting image from the gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri: Uri? ->
        uri?.let {
            authViewModel.updateProfileImage(uri)
        }
    }

    //Launching camera for clicking the photo
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {bitmap ->
        bitmap?.let {
            val cameraUri = bitmapToUri(context, bitmap)
            authViewModel.updateProfileImage(cameraUri)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        cameraPermissionGranted.value = permissions[Manifest.permission.CAMERA] == true
        galleryPermissionGranted.value = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true ||
                permissions[Manifest.permission.READ_MEDIA_IMAGES] == true

        if (cameraPermissionGranted.value) {
            cameraLauncher.launch(null)
        } else if (galleryPermissionGranted.value) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // ImageSourceDialog - launch permission requests
    if (showImageDialog) {
        ImageSourceDialog(
            onDismiss = { showImageDialog = false },
            onCameraClick = {
                permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
            },
            onGalleryClick = {
                val galleryPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
                permissionLauncher.launch(arrayOf(galleryPermission))
            }
        )
    }



    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.weight(0.3f))

        Card (
            modifier = Modifier
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(20.dp)
        ){
            GlideImage(
                model = currentUser?.profileImgUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(width)
                    .fillMaxWidth(),
                failure = placeholder(R.drawable.avatar)
            )
        }
        Spacer(modifier = Modifier.weight(0.2f))

        Card(
            elevation = CardDefaults.cardElevation(5.dp),
            onClick = {
                showImageDialog = true
            }
        ) {
            Text(
                text = "Add new/Update picture",
                fontFamily = poppinsFam,
                fontSize = 15.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            modifier = Modifier.fillMaxWidth(0.88f),
            text = "Name",
            fontFamily = poppinsFam
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.88f),
            value = updatedName,
            onValueChange = {
                updatedName = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Name"
                )
            },
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            modifier = Modifier.fillMaxWidth(0.88f),
            text = "Email",
            fontFamily = poppinsFam
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.88f),
            value = updatedEmail,
            onValueChange = {
                updatedEmail = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Name"
                )
            },
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            modifier = Modifier.fillMaxWidth(0.88f),
            text = "Password ",
            fontFamily = poppinsFam
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.88f),
            value = password,
            onValueChange = {
                password = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Key,
                    contentDescription = "Name"
                )
            },
            trailingIcon = {
                val showPassword = if(passwordVisi){
                    Icons.Outlined.Visibility
                }else{
                    Icons.Outlined.VisibilityOff
                }

                IconButton(onClick = {
                    passwordVisi = !passwordVisi
                }){
                    Icon(
                        imageVector = showPassword,
                        contentDescription = "Password Visibility"
                    )
                }
            },
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(0.88f),
                    text = "Required to for update",
                    fontFamily = poppinsFam,
                    fontSize = 14.sp
                )
            },
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Button(
            modifier = Modifier.fillMaxWidth(0.77f),
            onClick = {
                authViewModel.updateCurrentUser(updatedName, updatedEmail, password)
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Gray
            ),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text(
                text = "Update User Details",
                fontFamily = poppinsFam,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
fun ImageSourceDialog(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Choose Image Source")
        },
        text = {
            Column {
                Button(
                    onClick = {
                        onCameraClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Take a Photo",
                        fontSize = 13.sp,
                        fontFamily = poppinsFam
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onGalleryClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Choose from Gallery",
                        fontFamily = poppinsFam,
                        fontSize = 13.sp
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}


//From Bitmap to uri
fun bitmapToUri(context: Context, bt: Bitmap): Uri{
    val image = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
    val outStream = FileOutputStream(image)
    bt.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
    outStream.flush()
    outStream.close()
    return Uri.fromFile(image)
}
