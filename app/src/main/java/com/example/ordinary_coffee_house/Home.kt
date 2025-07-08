package com.example.ordinary_coffee_house

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(nav_ctrl: NavController, current_route: String, model: Model, onHideNavBar:() -> Unit = {}, onShowNavBar:() -> Unit = {}, onGoToDetails: (imageRes: Int, name: String) -> Unit, onGoToOrderSuccess: () -> Unit) {
    val user by model.user.collectAsState()
    val loyalty_points by model.loyalty_points.collectAsState()
    var profile_showed by remember { mutableStateOf(false) }
    var cart_showed by remember { mutableStateOf(false) }
    val coffee_options = listOf(
        Option("Americano", R.drawable.americano),
        Option("Cappuccino", R.drawable.cappuccino),
        Option("Mocha", R.drawable.mocha),
        Option("Flat White", R.drawable.flat_white),
    )
    if (profile_showed) {
        Profile(onBackPressed = {profile_showed = false}, onShowNavBar, onUserSaved = {model.update()})
    }
    else if (cart_showed) {
        Cart(nav_ctrl, current_route, onBackPressed = {cart_showed = false}, onShowNavBar, onLoyaltyUpdated = {model.update()}, onGoToOrderSuccess)
    }
    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7F9))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp, bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Good morning",
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFFA5AEBB),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = user.full_name,
                            color = Color(0xFF273640),
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 0.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                onHideNavBar()
                                cart_showed = true
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_shopping_cart),
                                contentDescription = "Cart",
                                modifier = Modifier.size(26.dp),
                                tint = Color(0xFF273640)
                            )
                        }
                        IconButton(
                            onClick = {
                                profile_showed = true
                                onHideNavBar()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_profile),
                                contentDescription = "Profile",
                                modifier = Modifier.size(26.dp),
                                tint = Color(0xFF273640)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                LoyaltyCard(loyalty_points)

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .background(Color(0xFF324A59), shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 22.dp, start = 16.dp, end = 16.dp, bottom = 68.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Choose your coffee",
                            color = Color(0xFFD8D8D8),
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            userScrollEnabled = true,
                            content = {
                                items(coffee_options) { coffee ->
                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .background(Color.White, RoundedCornerShape(20.dp))
                                            .padding(8.dp)
                                            .clickable {
//                                                nav_ctrl.navigate("details/${coffee.imageRes}/${coffee.name}")
                                                onGoToDetails(coffee.imageRes, coffee.name)
                                            }
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Image(
                                                painter = painterResource(coffee.imageRes),
                                                contentDescription = coffee.name,
                                                modifier = Modifier.size(80.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = coffee.name,
                                                fontFamily = FontFamily.Serif,
                                                color = Color(0xFF2D3C49),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        )
                        Spacer(Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}
