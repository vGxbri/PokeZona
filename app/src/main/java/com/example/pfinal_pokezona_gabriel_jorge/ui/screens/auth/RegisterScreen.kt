// Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pfinal_pokezona_gabriel_jorge.R

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onRegisterSuccess()
        }
    }

    Box(
        modifier =
            Modifier
                    .fillMaxSize()
                    .background(
                            Brush.verticalGradient(
                                    colors =
                                            listOf(
                                                    MaterialTheme.colorScheme.secondary,
                                                    MaterialTheme.colorScheme.secondary
                                                            .copy(alpha = 0.5f),
                                                    MaterialTheme.colorScheme.background
                                            )
                            )
                    )
    ) {
        // Notificación de error flotante arriba
        AnimatedVisibility(
            visible = authState is AuthState.Error,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier =
                Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .zIndex(1f)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text =
                            if (authState is AuthState.Error)
                                translateRegisterFirebaseError(
                                    (authState as
                                            AuthState.Error)
                                        .message,
                                    context
                                )
                            else "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Column(
            modifier =
                Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .statusBarsPadding()
                        .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo de la app
            Image(
                painter = painterResource(id = R.drawable.pokezona),
                contentDescription = stringResource(R.string.pokezona_logo),
                modifier = Modifier.size(180.dp),
                contentScale = ContentScale.Fit
            )

            // Tarjeta principal de registro
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                elevation =
                    CardDefaults.elevatedCardElevation(
                        defaultElevation = 12.dp
                    ),
                colors =
                    CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
            ) {
                Column(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.create_account),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.complete_data),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo Nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(R.string.username)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint =
                                    MaterialTheme.colorScheme
                                        .secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedBorderColor =
                                    MaterialTheme.colorScheme
                                        .secondary,
                                unfocusedBorderColor =
                                    MaterialTheme.colorScheme
                                        .onSurface.copy(
                                            alpha = 0.15f
                                        ),
                                focusedContainerColor =
                                    MaterialTheme.colorScheme
                                        .secondary.copy(
                                            alpha = 0.04f
                                        ),
                                unfocusedContainerColor =
                                    Color.Transparent
                            )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(R.string.email)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint =
                                    MaterialTheme.colorScheme
                                        .secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedBorderColor =
                                    MaterialTheme.colorScheme
                                        .secondary,
                                unfocusedBorderColor =
                                    MaterialTheme.colorScheme
                                        .onSurface.copy(
                                            alpha = 0.15f
                                        ),
                                focusedContainerColor =
                                    MaterialTheme.colorScheme
                                        .secondary.copy(
                                            alpha = 0.04f
                                        ),
                                unfocusedContainerColor =
                                    Color.Transparent
                            )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint =
                                    MaterialTheme.colorScheme
                                        .secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisible =
                                        !passwordVisible
                                }
                            ) {
                                Icon(
                                    if (passwordVisible)
                                        Icons.Default
                                            .VisibilityOff
                                    else
                                        Icons.Default
                                            .Visibility,
                                    contentDescription =
                                        if (passwordVisible)
                                            stringResource(
                                                R.string
                                                    .hide
                                            )
                                        else
                                            stringResource(
                                                R.string
                                                    .show
                                            ),
                                    tint =
                                        MaterialTheme
                                            .colorScheme
                                            .onSurfaceVariant,
                                    modifier =
                                        Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation =
                            if (passwordVisible)
                                VisualTransformation.None
                            else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedBorderColor =
                                    MaterialTheme.colorScheme
                                        .secondary,
                                unfocusedBorderColor =
                                    MaterialTheme.colorScheme
                                        .onSurface.copy(
                                            alpha = 0.15f
                                        ),
                                focusedContainerColor =
                                    MaterialTheme.colorScheme
                                        .secondary.copy(
                                            alpha = 0.04f
                                        ),
                                unfocusedContainerColor =
                                    Color.Transparent
                            )
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Botón principal
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(40.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Button(
                            onClick = {
                                viewModel.register(
                                    email,
                                    password,
                                    name
                                )
                            },
                            modifier =
                                Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        MaterialTheme
                                            .colorScheme
                                            .secondary
                                ),
                            elevation =
                                ButtonDefaults.buttonElevation(
                                    defaultElevation = 6.dp,
                                    pressedElevation = 2.dp
                                )
                        ) {
                            Text(
                                stringResource(
                                    R.string.create_account
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Link a login
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.already_have_account),
                    color =
                        MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.6f
                        ),
                    fontSize = 14.sp
                )
                TextButton(onClick = onNavigateBack) {
                    Text(
                        stringResource(R.string.login_action),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// Errores de FireBase traducidos
private fun translateRegisterFirebaseError(
    message: String,
    context: android.content.Context
): String {
    val lower = message.lowercase()
    return when {
        "empty" in lower || "null" in lower || "blank" in lower ->
            context.getString(R.string.error_fill_fields)

        "email already in use" in lower || "email-already-in-use" in lower ->
            context.getString(R.string.error_email_in_use)

        "email address is badly formatted" in lower || "invalid email" in lower ->
            context.getString(R.string.error_invalid_email)

        "password is invalid" in lower ||
                "weak password" in lower ||
                "at least 6 characters" in lower ->
            context.getString(R.string.error_weak_password)

        "network" in lower || "connection" in lower ->
            context.getString(R.string.error_network)

        "too many" in lower || "blocked" in lower ->
            context.getString(R.string.error_too_many_attempts)

        "internal error" in lower -> context.getString(R.string.error_internal)
        "disabled" in lower -> context.getString(R.string.error_account_disabled)
        else -> message
    }
}
