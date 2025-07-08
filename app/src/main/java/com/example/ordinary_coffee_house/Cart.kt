package com.example.ordinary_coffee_house

import android.content.Context
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun CartItemUI(item: CartItem) {
    val cup_type = if (item.select == 1) "Iced" else "Hot"
    val size_label = listOf("Small", "Medium", "Large").getOrNull(item.size) ?: "Unknown"
    val ice_label = when (item.ice) {
        0 -> "Single Ice"
        1 -> "Half Ice"
        2 -> "Full Ice"
        else -> "No Ice"
    }

    Surface(
        shape = RoundedCornerShape(0.dp),
        color = Color(0xFFF8FAFF),
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(item.image_res),
                contentDescription = null,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF263047)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = buildString {
                        append(item.shot)
                        append(" | $cup_type | $size_label | $ice_label")
                    },
                    fontSize = 12.sp,
                    color = Color(0xFF6D7A89),
                    fontFamily = FontFamily.Serif
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Qty: ${item.quantity}",
                    fontSize = 12.sp,
                    color = Color(0xFFB2B9C4),
                    fontFamily = FontFamily.Serif
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 6.dp)
            ) {
                Text(
                    text = "$%.2f".format(item.price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF223140),
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Cart(nav_ctrl: NavController, current_route: String, onBackPressed: () -> Unit = {}, onShowNavBar: () -> Unit = {} ,onLoyaltyUpdated: () -> Unit = {}, onGoToOrderSuccess: () -> Unit) {
    val context = LocalContext.current
    var cart_items by remember { mutableStateOf(getCartItems(context)) }
    val total_price = cart_items.sumOf { it.price }
    var voucherCode by remember { mutableStateOf("") }
    var useVoucher by remember { mutableStateOf(false) }

    fun onCheckOutPressed() {
        val _dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM | HH:mm"))
        val _address = getUser(context).address
        val on_going = getOngoing(context).toMutableList()
        val rewards = getRewards(context).toMutableList()
        val new_items = cart_items.map { cartItem ->
            OrderItem(
                id = cartItem.id,
                time_stamp = _dateTime,
                name = cartItem.name,
                address = _address,
                price = cartItem.price,
                points = 100 * cartItem.quantity
            )
        }
        saveOngoing(context, resetIds(on_going + new_items))
        saveRewards(context, resetIds(rewards + new_items))
        emptyCart(context)
        cart_items = listOf()
        val loyalty = getLoyalty(context)
        val points = getMyPoints(context)
        saveLoyalty(context, loyalty + 1)
        saveMyPoints(context, points + new_items.sumOf { it.points })
        onLoyaltyUpdated()
        onGoToOrderSuccess()
    }

    BackHandler(enabled = true) {
        if (useVoucher) {
            cancelDiscount20(context)
            useVoucher = false
            cart_items = getCartItems(context)
        }
        nav_ctrl.popBackStack()
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7F9))
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 0.dp)
                    .weight(1f, fill = true)
            ) {
                TopAppBar(
                    title = {
                        Box(
                            Modifier.fillMaxWidth().padding(end = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "My Cart",
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                fontSize = 20.sp,
                                color = Color(0xFF132032)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackPressed()
                            if (current_route == "home") {
                                onShowNavBar()
                            }
                            if (useVoucher) {
                                cancelDiscount20(context)
                                useVoucher = false
                                cart_items = getCartItems(context)
                            }
                        }) {
                            Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Back", tint = Color(0xFF132032))
                        }
                    },
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = voucherCode,
                        onValueChange = { voucherCode = it },
                        enabled = !useVoucher,
                        placeholder = { Text("Enter voucher...", fontSize = 15.sp, fontFamily = FontFamily.Serif) },
                        modifier = Modifier.width(260.dp),
                        singleLine = true,
                        )
                    Spacer(Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (voucherCode == "codeCoffee20" && !useVoucher) {
                                discount20(context)
                                useVoucher = true
                                cart_items = getCartItems(context)
                            }
                        },
                        enabled = !useVoucher,
                        modifier = Modifier.height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF324A59),
                            contentColor = Color.White
                        )
                    ) {
                        Text(if (useVoucher) "Applied" else "Apply", fontFamily = FontFamily.Serif)
                    }
                }
                if (useVoucher) {
                    Text(
                        "Voucher applied: 20% discount",
                        color = Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }


                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(
                        items = cart_items,
                        key = { it.id }
                    ) { item ->
                        SwipeToRemoveBox<CartItem>(
                            value = item,
                            onRemove = { cartItem ->
                                removeCartItem(context, cartItem.id)
                                cart_items = getCartItems(context)
                            }
                        ) { cartItem ->
                            CartItemUI(cartItem)
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Price",
                            color = Color(0xFFD2D7DF),
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$%.2f".format(total_price),
                            color = Color(0xFF192C41),
                            fontSize = 26.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            if (!cart_items.isEmpty()) {
                                onCheckOutPressed()
                                if (current_route == "home") {
                                    onShowNavBar()
                                }
                            }
                        },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF374C5A),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(50.dp)
                            .defaultMinSize(minWidth = 140.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_shopping_cart),
                            tint = Color.White,
                            contentDescription = "Checkout",
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Checkout",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToRemoveBox(value: T, onRemove: (T) -> Unit, removeAnimDuration: Int = 100, content: @Composable (T) -> Unit) {
    var dismissed by remember { mutableStateOf(false) }
    val swipe_state = rememberSwipeToDismissBoxState(
        confirmValueChange = { direction ->
            if (direction == SwipeToDismissBoxValue.EndToStart) {
                dismissed = true
                true
            }
            else false
        }
    )
    LaunchedEffect(dismissed) {
        if (dismissed) {
            delay(removeAnimDuration.toLong())
            onRemove(value)
        }
    }
    AnimatedVisibility(
        visible = !dismissed,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = removeAnimDuration),
            shrinkTowards = Alignment.Bottom
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = swipe_state,
            backgroundContent = {
                TrashBackground(swipe_state)
            },
            enableDismissFromEndToStart = true,
        ) {
            content(value)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashBackground(swipeState: SwipeToDismissBoxState) {
    val is_delete = swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart
    val bg_color = if (is_delete) Color(0xFFE53935) else Color(0x00000000)
    Row(
        modifier = Modifier.fillMaxSize().background(bg_color).padding(end = 20.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Remove",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 412, heightDp = 869)
@Composable
fun CartPreview() {
    val nav_ctrl = rememberNavController()
    Cart(
        nav_ctrl = nav_ctrl,
        current_route = "",
        onBackPressed = {},
        onShowNavBar = {},
        onLoyaltyUpdated = {},
        onGoToOrderSuccess = {}
    )
}