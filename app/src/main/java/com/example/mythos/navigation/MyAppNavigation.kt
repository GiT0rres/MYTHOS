package com.example.mythos.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mythos.pages.ComparePage
import com.example.mythos.pages.DetailPage
import com.example.mythos.pages.FavoritesPage
import com.example.mythos.pages.GalleryPage
import com.example.mythos.pages.HomePage
import com.example.mythos.pages.LoginPage
import com.example.mythos.components.MythosBottomBar
import com.example.mythos.pages.ProfilePage
import com.example.mythos.pages.SignupPage
import com.example.mythos.components.observeAsStateCompat
import com.example.mythos.viewmodel.AuthState
import com.example.mythos.viewmodel.AuthViewModel
import com.example.mythos.viewmodel.MythologyViewModel

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Signup : Screen("signup")
    data object Home : Screen("home")
    data object Gallery : Screen("gallery?filter={filter}")
    data object Detail : Screen("detail/{id}")
    data object Compare : Screen("compare/{id1}/{id2}")
    data object Favorites : Screen("favorites")
    data object Profile : Screen("profile")
}

private val BOTTOM_ROUTES = setOf("home", "gallery", "favorites", "profile")

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    mythologyViewModel: MythologyViewModel = viewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.observeAsStateCompat()
    val uiState by mythologyViewModel.uiState.collectAsStateWithLifecycle()

    // Mantém o usuário na área autenticada ou nas telas de login conforme o Firebase Auth.
    LaunchedEffect(authState) {

        when (authState) {

            is AuthState.Authenticated -> {

                navController.navigate(Screen.Home.route) {
                    popUpTo(0)
                }
            }

            is AuthState.Unauthenticated -> {

                navController.navigate(Screen.Login.route) {
                    popUpTo(0)
                }
            }

            else -> Unit
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route?.substringBefore("?")?.substringBefore("/")

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route
            ) {
                composable(Screen.Login.route) {
                    LoginPage(
                        authViewModel = authViewModel,
                        onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
                    )
                }

                composable(Screen.Signup.route) {
                    SignupPage(
                        authViewModel = authViewModel,
                        onNavigateToLogin = { navController.popBackStack() }
                    )
                }

                composable(Screen.Home.route) {
                    HomePage(
                        userName = authViewModel.userName,
                        onExplore = { navController.navigate("gallery?filter=Todas") },
                        onCategory = { filter -> navController.navigate("gallery?filter=$filter") }
                    )
                }

                composable(Screen.Gallery.route) { entry ->
                    val filter = entry.arguments?.getString("filter") ?: "Todas"
                    GalleryPage(
                        deities = uiState.deities,
                        loading = uiState.loading,
                        favorites = uiState.favorites,
                        initialFilter = filter,
                        onDeityClick = { id -> navController.navigate("detail/$id") },
                        onToggleFavorite = { id -> mythologyViewModel.toggleFavorite(id) }
                    )
                }

                composable("gallery") {
                    GalleryPage(
                        deities = uiState.deities,
                        loading = uiState.loading,
                        favorites = uiState.favorites,
                        onDeityClick = { id -> navController.navigate("detail/$id") },
                        onToggleFavorite = { id -> mythologyViewModel.toggleFavorite(id) }
                    )
                }

                composable(Screen.Detail.route) { entry ->
                    val id = entry.arguments?.getString("id")
                    val deity = mythologyViewModel.deityById(id)
                    if (deity != null) {
                        DetailPage(
                            deity = deity,
                            isFavorite = uiState.favorites.contains(deity.id),
                            onBack = { navController.popBackStack() },
                            onToggleFavorite = { mythologyViewModel.toggleFavorite(deity.id) },
                            onCompare = {
                                val other = uiState.deities.firstOrNull { it.id != deity.id }
                                if (other != null) {
                                    navController.navigate("compare/${deity.id}/${other.id}")
                                }
                            }
                        )
                    }
                }

                composable(Screen.Compare.route) { entry ->
                    val id1 = entry.arguments?.getString("id1")
                    val id2 = entry.arguments?.getString("id2")
                    val first = mythologyViewModel.deityById(id1)
                    val second = mythologyViewModel.deityById(id2)
                    val curiosity by mythologyViewModel.curiosity.collectAsStateWithLifecycle()

                    LaunchedEffect(id1, id2) {
                        if (id1 != null && id2 != null) {
                            mythologyViewModel.loadCuriosity(id1, id2)
                        }
                    }

                    if (first != null && second != null) {
                        ComparePage(
                            first = first,
                            second = second,
                            others = uiState.deities,
                            curiosity = curiosity,
                            onBack = { navController.popBackStack() },
                            onSelectSecond = { newId ->
                                navController.navigate("compare/${first.id}/$newId") {
                                    popUpTo(Screen.Compare.route) { inclusive = true }
                                }
                            }
                        )
                    }
                }

                composable(Screen.Favorites.route) {
                    FavoritesPage(
                        deities = uiState.deities,
                        favorites = uiState.favorites,
                        onDeityClick = { id -> navController.navigate("detail/$id") },
                        onToggleFavorite = { id -> mythologyViewModel.toggleFavorite(id) }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfilePage(
                        userName = authViewModel.userName,
                        userEmail = authViewModel.userEmail,
                        favoritesCount = uiState.favorites.size,
                        artworksCount = uiState.deities.size,
                        onSignout = { authViewModel.signout() }
                    )
                }
            }
        }

        if (currentRoute in BOTTOM_ROUTES) {
            MythosBottomBar(currentRoute = currentRoute) { route ->
                navController.navigate(route) { launchSingleTop = true }
            }
        }
    }
}

