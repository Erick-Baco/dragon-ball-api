package mx.unam.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import mx.unam.R
import mx.unam.model.HomeViewModel
import mx.unam.ui.components.AppBackground
import mx.unam.ui.components.GlassSurface
import mx.unam.utils.CharacterDisplayMapper

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(),
    onLogOut: () -> Unit
) {
    val searchState = rememberTextFieldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    fun handleSearch() {
        keyboardController?.hide()
        focusManager.clearFocus()
        homeViewModel.getCharacter(searchState.text.toString().trim())
    }

    AppBackground {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp)
                    .padding(bottom = 112.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassSurface(shape = CircleShape) {
                        Text(
                            text = "DragonBall",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }

                    GlassSurface(shape = CircleShape) {
                        IconButton(
                            onClick = { onLogOut() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.logout),
                                contentDescription = "Cerrar sesión",
                                tint = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (homeViewModel.errorMessage != null && searchState.text.isNotEmpty()) {
                    Text(
                        text = homeViewModel.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (homeViewModel.character != null) {
                    val character = homeViewModel.character!!

                    GlassSurface(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = character.image,
                                contentDescription = "Imagen de ${character.name}",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .weight(0.55f)
                                    .fillMaxHeight()
                            )

                            Column(
                                modifier = Modifier
                                    .weight(0.45f)
                                    .fillMaxHeight()
                                    .padding(start = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = character.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CharacterDetailItem(label = "Raza", value = CharacterDisplayMapper.race(character.race))
                                    CharacterDetailItem(label = "Género", value = CharacterDisplayMapper.gender(character.gender))
                                    CharacterDetailItem(label = "Ki Base", value = character.ki)
                                    CharacterDetailItem(label = "Ki Máx", value = character.maxKi)
                                    CharacterDetailItem(label = "Afiliación", value = CharacterDisplayMapper.affiliation(character.affiliation))
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .imePadding()
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {
                OutlinedTextField(
                    state = searchState,
                    placeholder = {
                        Text("Buscar personaje...", color = Color.DarkGray)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    shape = CircleShape,
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White.copy(alpha = 0.65f),
                        focusedContainerColor = Color.White.copy(alpha = 0.85f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.9f),
                        focusedBorderColor = Color.White,
                        cursorColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        autoCorrect = false
                    ),
                    onKeyboardAction = { handleSearch() },
                    trailingIcon = {
                        Box(
                            modifier = Modifier.size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (homeViewModel.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.Black
                                )
                            } else if (searchState.text.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        searchState.edit { replace(0, length, "") }
                                        focusRequester.requestFocus()
                                        keyboardController?.show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Limpiar",
                                        tint = Color.Black
                                    )
                                }
                            } else {
                                IconButton(onClick = { handleSearch() }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Buscar personaje",
                                        tint = Color.Black
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CharacterDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall,
            color = Color.DarkGray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}