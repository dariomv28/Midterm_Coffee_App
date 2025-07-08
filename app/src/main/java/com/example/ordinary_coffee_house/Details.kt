package com.example.ordinary_coffee_house


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
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
fun Details(imageRes: Int, name: String, nav_ctrl: NavController, onLoyaltyUpdated: () -> Unit = {}, onBackToHome: () -> Unit, onGoToOrderSuccess: () -> Unit) {
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(1) }
    var shot by remember { mutableStateOf("Single") }
    var select by remember { mutableStateOf(0) }
    var size by remember { mutableStateOf(1) }
    var ice by remember { mutableStateOf(2) }
    var price by remember { mutableStateOf(3.0) }
    var cart_showed by remember{ mutableStateOf(false) }
    fun getPrice() {
        price = (3 + size * 2.0) * quantity
    }
    LaunchedEffect(quantity, shot, select, size, ice) {
        getPrice()
    }
    if(cart_showed) {
        Cart(nav_ctrl, current_route = "", onBackPressed = {cart_showed = false}, onShowNavBar = {}, onLoyaltyUpdated, onGoToOrderSuccess)
    }
    else{
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7F9))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
//                            nav_ctrl.navigate("home")
                            onBackToHome()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = Color(0xFF2D3C49)
                        )
                    }
                    Text(
                        text = "Details",
                        color = Color(0xFF2D3C49),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp
                    )
                    IconButton(onClick = {cart_showed = true}) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_shopping_cart),
                            contentDescription = "Cart",
                            tint = Color(0xFF2D3C49)
                        )
                    }
                }
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = "Coffee",
                    modifier = Modifier.fillMaxWidth().size(100.dp).padding(top = 12.dp, bottom = 8.dp).clip(RoundedCornerShape(18.dp)).background(Color.White).align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Fit
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(name, color = Color(0xFF2D3C49), fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(32.dp),
                            contentPadding = PaddingValues(0.dp),
                            border = BorderStroke(1.dp, Color(0xFFD6D6D6)),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("-", fontSize = 20.sp, color = Color(0xFF2D3C49))
                        }
                        Text(
                            text = "$quantity",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Color(0xFF2D3C49),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        OutlinedButton(
                            onClick = { quantity++ },
                            modifier = Modifier.size(32.dp),
                            contentPadding = PaddingValues(0.dp),
                            border = BorderStroke(1.dp, Color(0xFFD6D6D6)),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("+", fontSize = 20.sp, color = Color(0xFF2D3C49))
                        }
                    }
                }
                TextRowSelector(
                    label = "Shot",
                    options = listOf("Single", "Double"),
                    selectedIndex = if (shot == "Single") 0 else 1,
                    onSelected = { shot = if (it == 0) "Single" else "Double" }
                )
                IconRowSelector(
                    label = "Select",
                    options = listOf(R.drawable.ic_cup_hot, R.drawable.ic_cup_cold),
                    selectedIndex = select,
                    onSelected = { select = it }
                )
                IconRowSelector(
                    label = "Size",
                    options = listOf(R.drawable.ic_size_small, R.drawable.ic_size_medium, R.drawable.ic_size_large),
                    selectedIndex = size,
                    onSelected = { size = it }
                )
                IconRowSelector(
                    label = "Ice",
                    options = listOf(R.drawable.ic_ice_single, R.drawable.ic_ice_half, R.drawable.ic_ice_full),
                    selectedIndex = ice,
                    onSelected = { ice = it }
                )

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Amount",
                        color = Color(0xFF2D3C49),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "\$${"%.2f".format(price)}",
                        color = Color(0xFF2D3C49),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp
                    )
                }
                Button(
                    onClick = {
                        val id = setCartItemId(context)
                        val cart_item = CartItem(id, name, imageRes, quantity, shot, select, size, ice, price)
                        addToCart(context, cart_item)
                        cart_showed = true
                    },
                    modifier = Modifier.fillMaxWidth().height(70.dp).padding(bottom = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3A4F5C),
                        contentColor = Color.White
                    )
                ) {
                    Text("Add to cart", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, fontFamily = FontFamily.Serif)
                }
            }
        }
    }
}

@Composable
fun TextRowSelector(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF2D3C49), fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            options.forEachIndexed { i, text ->
                OutlinedButton(
                    onClick = { onSelected(i) },
                    border = BorderStroke(1.dp, if (i == selectedIndex) Color(0xFF3A4F5C) else Color(0xFFD6D6D6)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (i == selectedIndex) Color(0xFF3A4F5C) else Color.Transparent,
                        contentColor = if (i == selectedIndex) Color.White else Color(0xFF2D3C49)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(text, fontSize = 12.sp, fontFamily = FontFamily.Serif)
                }
            }
        }
    }
}

@Composable
fun IconRowSelector(
    label: String,
    options: List<Int>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF2D3C49), fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        Spacer(Modifier.height(2.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            options.forEachIndexed { i, iconRes ->
                IconButton(
                    onClick = { onSelected(i) },
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = if (i == selectedIndex) Color(0xFF2D3C49) else Color(0xFFD6D6D6),
                        modifier = Modifier.size((25+10*i).dp)
                    )
                }
            }
        }
    }
}