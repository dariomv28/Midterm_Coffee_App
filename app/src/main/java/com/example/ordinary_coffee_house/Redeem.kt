package com.example.ordinary_coffee_house

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class RedeemItem(val id: Int, val name: String, val image_res: Int, val points: Int, val valid_until: String, var time_stamp: String
)

@Composable
fun RedeemItemUI(item: RedeemItem) {
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
                    item.time_stamp,
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
                        item.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF192C41)
                    )
                }
            }
            Text(
                text = "${item.points} pts",
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


val redeemSampleList = listOf(
    RedeemItem(-1,"Americano", R.drawable.americano, 1340, "04.07.21", ""),
    RedeemItem(-1,"Flat White", R.drawable.flat_white, 1340, "04.07.21", ""),
    RedeemItem(-1,"Cappuccino", R.drawable.cappuccino, 1340, "04.07.21", ""),
    RedeemItem(-1,"Mocha", R.drawable.mocha, 1340, "04.07.21", "")
)

enum class RedeemTab(val label: String) {
    Available("Available"),
    RedeemHistory("Redeem History")
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Redeem(nav_ctrl: NavController, onUpdatePoints: () -> Unit = {},onBackToRewards:()->Unit) {
    val rewards = redeemSampleList
    val context = LocalContext.current
    val snack_bar = remember{ SnackbarHostState() }
    val tabs = RedeemTab.values().toList()
    var selectedTab by remember { mutableStateOf(RedeemTab.Available) }
    var redeem_history by remember { mutableStateOf(getRedeemHistory(context)) }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7F9))
    ){
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Box(
                        Modifier.fillMaxWidth().padding(end = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Redeem",
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF223140),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {onBackToRewards()}) {
                        Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Back", tint = Color(0xFF132032))
                    }
                },
            )
            TabRowTemplate(
                tabs = tabs,
                selectedTab = selectedTab,
                tabLabel = { it.label },
                onTabSelected = { selectedTab = it }
            )
            if (selectedTab == RedeemTab.Available) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(rewards) { reward ->
                        AvailableRedeem(
                            reward,
                            redeem_history,
                            context,
                            snack_bar,
                            onUpdatePoints,
                            onRedeemHistoryUpdated = { (a) ->
                                redeem_history = reIndexed_redeem_history(redeem_history + a)
                                saveRedeemHistory(context, redeem_history)
                            }
                        )
                    }
                }
            }
            else {
                if (!redeem_history.isEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(
                            items = redeem_history.sortedByDescending { it.time_stamp },
                            key = { it.id }
                        ) { i -> RedeemItemUI(i) }
                    }
                } else {
                    EmptyOrderMessage()
                }
            }
        }
        SnackbarHost(
            hostState = snack_bar,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AvailableRedeem(reward: RedeemItem, redeem_history: List<RedeemItem>, context: Context, snack_bar: SnackbarHostState, onUpdatePoints: () -> Unit = {}, onRedeemHistoryUpdated: (List<RedeemItem>) -> Unit) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = reward.image_res),
            contentDescription = reward.name,
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F7F9), CircleShape)
        )
        Spacer(Modifier.width(18.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                reward.name,
                color = Color(0xFF223140),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Valid until ${reward.valid_until}",
                color = Color(0xFFA5AEBB),
                fontSize = 13.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium
            )
        }

        Button(
            onClick = {
                var p = getMyPoints(context)
                if (p >= reward.points) {
                    p -= reward.points
                    saveMyPoints(context, p)
                    onUpdatePoints()
                    val _dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM | HH:mm"))
                    val _address = getUser(context).address
                    val on_going = getOngoing(context).toMutableList()
                    val new_redeemed_item = OrderItem(
                        id = -1,
                        time_stamp = _dateTime,
                        name = reward.name,
                        address = _address,
                        price = 0.0,
                        points = 0,
                    )
                    val new_redeem_history_item = RedeemItem(
                        id = -1,
                        name = reward.name,
                        image_res = reward.image_res,
                        points = reward.points,
                        valid_until = "",
                        time_stamp = _dateTime
                    )
                    on_going.add(new_redeemed_item)
                    saveOngoing(context, resetIds(on_going))
                    val new_redeem_list = listOf(new_redeem_history_item)
                    onRedeemHistoryUpdated(new_redeem_list)
                    scope.launch {
                        snack_bar.showSnackbar(
                            message = "Congratulation",
                            duration = SnackbarDuration.Short
                        )
                    }
                } else {
                    scope.launch {
                        snack_bar.showSnackbar(
                            message = "You do not have enough points",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            },
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF21C169),
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 7.dp),
            modifier = Modifier
        ) {
            Text(
                "${reward.points} pts",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp
            )
        }
    }
}

fun reIndexed_redeem_history(list: List<RedeemItem>): List<RedeemItem> {
    return list.mapIndexed { index, item ->
        item.copy(id = index)
    }
}
