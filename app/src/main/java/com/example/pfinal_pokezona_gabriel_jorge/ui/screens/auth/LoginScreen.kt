// Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.auth

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    // Configuración del inicio de sesión de Google
    val tokenGoogle = "238612024369-afhpdvg213vdef4rsser4hb7m3sarenm.apps.googleusercontent.com"

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(tokenGoogle)
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    viewModel.signInWithGoogle(idToken)
                }
            } catch (e: ApiException) {
                Log.e("LoginScreen", "Google sign in failed", e)
            }
        }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
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
                                                    MaterialTheme.colorScheme.primary,
                                                    MaterialTheme.colorScheme.primary
                                                            .copy(alpha = 0.6f),
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
                                translateFirebaseError(
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

            // Tarjeta principal de login
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
                        text = stringResource(R.string.login),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.access_account),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

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
                                        .primary,
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
                                        .primary,
                                unfocusedBorderColor =
                                    MaterialTheme.colorScheme
                                        .onSurface.copy(
                                            alpha = 0.15f
                                        ),
                                focusedContainerColor =
                                    MaterialTheme.colorScheme
                                        .primary.copy(
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
                                        .primary,
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
                                        .primary,
                                unfocusedBorderColor =
                                    MaterialTheme.colorScheme
                                        .onSurface.copy(
                                            alpha = 0.15f
                                        ),
                                focusedContainerColor =
                                    MaterialTheme.colorScheme
                                        .primary.copy(
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
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Button(
                            onClick = {
                                viewModel.login(email, password)
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
                                            .primary
                                ),
                            elevation =
                                ButtonDefaults.buttonElevation(
                                    defaultElevation = 6.dp,
                                    pressedElevation = 2.dp
                                )
                        ) {
                            Text(
                                stringResource(R.string.enter),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color =
                                    MaterialTheme.colorScheme
                                        .onSurface.copy(
                                            alpha = 0.12f
                                        )
                            )
                            Text(
                                text = stringResource(R.string.or),
                                modifier =
                                    Modifier.padding(
                                        horizontal = 16.dp
                                    ),
                                color =
                                    MaterialTheme.colorScheme
                                        .onSurfaceVariant,
                                fontSize = 13.sp
                            )
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color =
                                    MaterialTheme.colorScheme
                                        .onSurface.copy(
                                            alpha = 0.12f
                                        )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón de Google
                        OutlinedButton(
                            onClick = {
                                launcher.launch(
                                    googleSignInClient
                                        .signInIntent
                                )
                            },
                            modifier =
                                Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            border =
                                androidx.compose.foundation
                                    .BorderStroke(
                                        1.dp,
                                        MaterialTheme
                                            .colorScheme
                                            .onSurface
                                            .copy(
                                                alpha =
                                                    0.15f
                                            )
                                    ),
                            colors =
                                ButtonDefaults.outlinedButtonColors(
                                    contentColor =
                                        MaterialTheme
                                            .colorScheme
                                            .onSurface
                                )
                        ) {
                            Text(
                                "G",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color =
                                    MaterialTheme.colorScheme
                                        .primary,
                                modifier =
                                    Modifier.padding(end = 8.dp)
                            )
                            Text(
                                stringResource(
                                    R.string
                                        .continue_with_google
                                ),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Link a registro
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.no_account),
                    color =
                        MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.6f
                        ),
                    fontSize = 14.sp
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        stringResource(R.string.register),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// Errores de FireBase traducidos
private fun translateFirebaseError(message: String, context: android.content.Context): String {
    val lower = message.lowercase()
    return when {
        "empty" in lower || "null" in lower || "blank" in lower ->
            context.getString(R.string.error_fill_fields)

        "password" in lower && "invalid" in lower || "wrong password" in lower ->
            context.getString(R.string.error_wrong_password)

        "no user record" in lower || "user not found" in lower || "no account" in lower ->
            context.getString(R.string.error_account_not_found)

        "email address is badly formatted" in lower || "invalid email" in lower ->
            context.getString(R.string.error_invalid_email)

        "email already in use" in lower || "email-already-in-use" in lower ->
            context.getString(R.string.error_email_in_use)

        "password is invalid" in lower ||
                "weak password" in lower ||
                "at least 6 characters" in lower ->
            context.getString(R.string.error_weak_password)

        "too many" in lower || "blocked" in lower ->
            context.getString(R.string.error_too_many_attempts)

        "network" in lower || "connection" in lower ->
            context.getString(R.string.error_network)

        "credential" in lower || "invalid" in lower ->
            context.getString(R.string.error_invalid_credentials)

        "internal error" in lower -> context.getString(R.string.error_internal)
        "disabled" in lower -> context.getString(R.string.error_account_disabled)
        "expired" in lower -> context.getString(R.string.error_session_expired)
        else -> message
    }
}
