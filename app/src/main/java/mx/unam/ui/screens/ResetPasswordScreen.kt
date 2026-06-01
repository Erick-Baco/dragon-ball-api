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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.unam.model.AuthViewModel
import mx.unam.ui.components.AppBackground
import mx.unam.ui.components.GlassSurface

@Composable
fun ResetPasswordScreen(
    authModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    onNavigateToLogin: () -> Unit,
) {
    val emailState = rememberTextFieldState()
    var resetDialog by rememberSaveable { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    fun dismissErrorDialog() {
        authModel.clearError()
    }

    fun handleEmailSent() {
        resetDialog = false
        onNavigateToLogin()
    }

    fun handleSendEmail() {
        keyboardController?.hide()

        authModel.resetPassword(
            emailState.text.toString().trim(),
            onSuccess = { resetDialog = true }
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
                            text = "Recuperar contraseña",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Text(
                            text = "Ingresa tu correo electrónico y te enviaremos las instrucciones para cambiar tu contraseña.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            state = emailState,
                            placeholder = { Text("Correo electrónico") },
                            lineLimits = TextFieldLineLimits.SingleLine,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                capitalization = KeyboardCapitalization.None,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape,
                            textStyle = LocalTextStyle.current.copy(color = Color.Black),
                            colors = inputColors,
                            onKeyboardAction = {
                                handleSendEmail()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(70.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { handleSendEmail() },
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
                                text = "Enviar correo",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = CircleShape,
                        colors = blackButtonColors
                    ) {
                        Text(
                            text = "Cancelar",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (authModel.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { dismissErrorDialog() },
            text = {
                Text(authModel.errorMessage ?: "")
            },
            confirmButton = {
                Button(
                    onClick = { dismissErrorDialog() },
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

    if (resetDialog) {
        AlertDialog(
            onDismissRequest = {
                handleEmailSent()
            },
            text = {
                Text("Se ha enviado un correo para cambio de contraseña.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        handleEmailSent()
                    },
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