package mx.unam.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.unam.R
import mx.unam.model.AuthViewModel
import mx.unam.ui.components.AppBackground
import mx.unam.ui.components.GlassSurface

@Composable
fun LoginScreen(
    authModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    onNavigateToRegister: () -> Unit,
    onNavigateToResetPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    val emailState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    val keyboardController = LocalSoftwareKeyboardController.current

    fun dismissDialog() {
        authModel.clearError()
    }

    fun handleOnClickLogin() {
        keyboardController?.hide()

        authModel.loginManual(
            emailState.text.toString().trim(),
            passwordState.text.toString().trim(),
            onLoginSuccess,
        )
    }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        unfocusedContainerColor = Color.White.copy(alpha = 0.65f),
        focusedContainerColor = Color.White.copy(alpha = 0.85f),
        unfocusedBorderColor = Color.White.copy(alpha = 0.9f),
        focusedBorderColor = Color.White,
        cursorColor = Color.Black,
        focusedPlaceholderColor = Color.DarkGray,
        unfocusedPlaceholderColor = Color.DarkGray
    )

    val blackButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Black,
        contentColor = Color.White,
        disabledContainerColor = Color.Black.copy(alpha = 0.55f),
        disabledContentColor = Color.White.copy(alpha = 0.75f)
    )

    AppBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GlassSurface(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Bienvenido",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(50.dp))

                        OutlinedTextField(
                            state = emailState,
                            placeholder = { Text("Correo electrónico") },
                            lineLimits = TextFieldLineLimits.SingleLine,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                capitalization = KeyboardCapitalization.None,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape,
                            textStyle = LocalTextStyle.current.copy(color = Color.Black),
                            colors = inputColors,
                        )

                        OutlinedSecureTextField(
                            state = passwordState,
                            placeholder = { Text("Contraseña") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                autoCorrectEnabled = false,
                                capitalization = KeyboardCapitalization.None,
                                imeAction = ImeAction.Done
                            ),
                            textObfuscationMode =
                                if (passwordHidden) TextObfuscationMode.RevealLastTyped
                                else TextObfuscationMode.Visible,
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape,
                            textStyle = LocalTextStyle.current.copy(color = Color.Black),
                            colors = inputColors,
                            trailingIcon = {
                                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                                    Icon(
                                        painterResource(
                                            id = if (passwordHidden) {
                                                R.drawable.visibility_off
                                            } else {
                                                R.drawable.visibility
                                            }
                                        ),
                                        contentDescription = if (passwordHidden) {
                                            "Mostrar contraseña"
                                        } else {
                                            "Ocultar contraseña"
                                        },
                                        tint = Color.Black
                                    )
                                }
                            },
                            onKeyboardAction = {
                                handleOnClickLogin()
                            }
                        )

                        TextButton(
                            onClick = onNavigateToResetPassword,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Olvidé mi contraseña",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentHeight(Alignment.CenterVertically)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(70.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = { handleOnClickLogin() },
                        enabled = !authModel.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = CircleShape,
                        colors = blackButtonColors
                    ) {
                        if (authModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Entrar",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToRegister,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = CircleShape,
                        colors = blackButtonColors
                    ) {
                        Text(
                            text = "Registrarme",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (authModel.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { dismissDialog() },
            text = {
                Text(authModel.errorMessage ?: "")
            },
            confirmButton = {
                Button(
                    onClick = { dismissDialog() },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}