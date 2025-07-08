package com.example.ordinary_coffee_house

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ordinary_coffee_house.ui.theme.Ordinary_coffee_houseTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App() {
    val nav_ctrl = rememberNavController()
    var selection_id = remember { mutableStateOf(0) }
    var is_cart by remember { mutableStateOf(false) }
    val this_route = nav_ctrl.currentBackStackEntryAsState().value?.destination?.route
    val model: Model = viewModel()
    var hide_nav_bar by remember { mutableStateOf(false) }
    var transitionDirection by remember { mutableStateOf("left") }

    fun onHideNavBar() { hide_nav_bar = true }
    fun onShowNavBar() { hide_nav_bar = false }

    Box(Modifier.fillMaxSize()) {
        NavHost(
            navController = nav_ctrl,
            startDestination = "home"
        ) {
            // HOME
            composable(
                "home",
                enterTransition = {
                    if (transitionDirection == "left")
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(600))
                    else
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                },
                exitTransition = {
                    if (transitionDirection == "left")
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(600))
                    else
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                },
                popEnterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                },
                popExitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                }
            ) {
                if (this_route != null) {
                    Home(
                        nav_ctrl = nav_ctrl,
                        current_route = this_route,
                        model = model,
                        onHideNavBar = { onHideNavBar() },
                        onShowNavBar = { onShowNavBar() },
                        onGoToDetails = { imageRes, name ->
                            transitionDirection = "left"
                            nav_ctrl.navigate("details/$imageRes/$name")
                        },
                        onGoToOrderSuccess = {
                            transitionDirection = "left"
                            nav_ctrl.navigate("order_success")
                        }
                    )
                }
                if (!hide_nav_bar) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        NavBar(
                            selection = selection_id.value,
                            onTabSelected = { idx ->
                                if (selection_id.value == idx) return@NavBar
                                transitionDirection = if (idx > selection_id.value) "left" else "right"
                                selection_id.value = idx
                                when (idx) {
                                    0 -> {
                                    }
                                    1 -> nav_ctrl.navigate("rewards")
                                    2 -> nav_ctrl.navigate("orders")
                                }
                            }
                        )
                    }
                }
            }

            // DETAILS
            composable(
                "details/{imageRes}/{name}",
                arguments = listOf(
                    navArgument("imageRes") { type = NavType.IntType },
                    navArgument("name") { type = NavType.StringType }
                ),
                enterTransition = {
                    if (transitionDirection == "left")
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(600))
                    else
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                },
                exitTransition = {
                    if (transitionDirection == "left")
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(600))
                    else
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                },
                popEnterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                },
                popExitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                }
            ) { backStackEntry ->
                val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: R.drawable.americano
                val name = backStackEntry.arguments?.getString("name") ?: "Unknown"
                Details(
                    imageRes = imageRes,
                    name = name,
                    nav_ctrl = nav_ctrl,
                    onLoyaltyUpdated = {model.update()},
                    onBackToHome = {
                        transitionDirection = "right"
                        nav_ctrl.popBackStack("home", false)
                    },
                    onGoToOrderSuccess = {
                        transitionDirection = "left"
                        nav_ctrl.navigate("order_success")
                    }
                )
            }

            // ORDERS
            composable(
                "orders",
                enterTransition = {
                    if (transitionDirection == "left")
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(600))
                    else
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                },
                popExitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                }
            ) {
                MyOrder(
                    nav_ctrl = nav_ctrl,
                    onBackToRewards = {
                        transitionDirection = "right"
                        nav_ctrl.popBackStack("rewards", false)
                    },
                    onBackToHome = {
                        transitionDirection = "right"
                        nav_ctrl.popBackStack("home", false)
                    }
                )
                if (!hide_nav_bar) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        NavBar(
                            selection = selection_id.value,
                            onTabSelected = { idx ->
                                if (selection_id.value == idx) return@NavBar
                                transitionDirection = if (idx > selection_id.value) "left" else "right"
                                selection_id.value = idx
                                when (idx) {
                                    0 -> nav_ctrl.navigate("home")
                                    1 -> nav_ctrl.navigate("rewards")
                                    2 -> { }
                                }
                            }
                        )
                    }
                }
            }

            // REWARDS
            composable(
                "rewards",
                enterTransition = {
                    if (transitionDirection == "left")
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(600))
                    else
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                }
            ) {
                Rewards(
                    nav_ctrl = nav_ctrl,
                    model = model,
                    onGoToRedeem = {
                        transitionDirection = "left"
                        nav_ctrl.navigate("redeem")
                    },
                )
                if (!hide_nav_bar) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        NavBar(
                            selection = selection_id.value,
                            onTabSelected = { idx ->
                                if (selection_id.value == idx) return@NavBar
                                transitionDirection = if (idx > selection_id.value) "left" else "right"
                                selection_id.value = idx
                                when (idx) {
                                    0 -> nav_ctrl.navigate("home")
                                    1 -> {  }
                                    2 -> nav_ctrl.navigate("orders")
                                }
                            }
                        )
                    }
                }
            }

            // REDEEM
            composable(
                "redeem",
                enterTransition = {
                    if (transitionDirection == "left")
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(600))
                    else
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(600))
                }
            ) {
                Redeem(
                    nav_ctrl = nav_ctrl,
                    onUpdatePoints = {
                        model.update()
                    },
                    onBackToRewards = {
                        transitionDirection = "right"
                        nav_ctrl.popBackStack("rewards", false)
                    }
                )
            }

            // ORDER SUCCESS
            composable(
                "order_success",
                enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(600))
                }
            ) {
                OrderSuccess(
                    nav_ctrl = nav_ctrl,
                    onGoToOrder = {
                        selection_id.value = 2
                        nav_ctrl.navigate("orders")
                    }
                )
            }
        }
    }
}
