package com.example.ordinary_coffee_house

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class OrderTab(val label: String) {
    OnGoing("On going"),
    History("History")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrder(nav_ctrl: NavController, onBackToRewards: ()->Unit, onBackToHome: ()->Unit) {
    val context = LocalContext.current
    var on_going by remember { mutableStateOf(getOngoing(context)) }
    var history by remember { mutableStateOf(getHistory(context)) }
    val tabs = OrderTab.values().toList()
    var selectedTab by remember { mutableStateOf(OrderTab.OnGoing) }

    Box (
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7F9))
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "My Order",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            color = Color(0xFF192C41)
                        )
                    }
                }
            )
            TabRowTemplate(
                tabs = tabs,
                selectedTab = selectedTab,
                tabLabel = { it.label },
                onTabSelected = { selectedTab = it }
            )
            if (selectedTab == OrderTab.OnGoing) {
                if (on_going.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(
                            items = on_going,
                            key = { it.id }
                        ) { order ->
                            SwipeToRemoveBox(
                                value = order,
                                onRemove = { order_item ->
                                    on_going = on_going.filter { it.id != order_item.id }
                                    saveOngoing(context, on_going)
                                    history = resetIds(history + order_item)
                                    saveHistory(context, history)
                                }
                            ) { order_item ->
                                OrderItemUI(order_item)
                            }
                        }
                    }
                } else {
                    EmptyOrderMessage()
                }
            } else {
                if (!history.isEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(
                            items = history.sortedByDescending { it.time_stamp },
                            key = { it.id }
                        ) { order -> OrderItemUI(order) }
                    }
                } else {
                    EmptyOrderMessage()
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun <T> TabRowTemplate(
    tabs: List<T>,
    selectedTab: T,
    tabLabel: (T) -> String,
    onTabSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFF192C41),
    inactiveColor: Color = Color(0xFFD2D7DF),
    underlineWidth: Dp = 160.dp,
    fontFamily: FontFamily = FontFamily.Serif,
    fontSize: TextUnit = 18.sp
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 28.dp)
    ) {
        tabs.forEach { tab ->
            val isSelected = selectedTab == tab
            val tabColor by animateColorAsState(if (isSelected) activeColor else inactiveColor, label = "")
            val underlineColor = if (isSelected) activeColor else Color.Transparent
            val underlineWidthAnim by animateDpAsState(if (isSelected) underlineWidth else 0.dp, label = "")
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(tab) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    tabLabel(tab),
                    color = tabColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = fontSize,
                    fontFamily = fontFamily,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    Modifier
                        .height(3.dp)
                        .width(underlineWidthAnim)
                        .clip(RoundedCornerShape(2.dp))
                        .background(underlineColor)
                )
            }
        }
    }
}


@Composable
fun OrderItemUI(order: OrderItem) {
    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(start = 10.dp, end = 10.dp, top = 12.dp, bottom = 0.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    order.time_stamp,
                    color = Color(0xFFD2D7DF),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalCafe,
                        contentDescription = "",
                        tint = Color(0xFF192C41),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        order.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF192C41)
                    )
                }
                Spacer(Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "",
                        tint = Color(0xFFD2D7DF),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        order.address,
                        color = Color(0xFFD2D7DF),
                        fontSize = 13.sp,
                        maxLines = 1,
                        fontFamily = FontFamily.Serif,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                "$%.2f".format(order.price),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF192C41),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFF0F0F0))
    }
}


@Composable
fun EmptyOrderMessage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Text(
            "No items found",
            fontFamily = FontFamily.Serif,
            color = Color(0xFFD2D7DF),
            fontSize = 16.sp
        )
        Spacer(Modifier.weight(1f))
    }
}




