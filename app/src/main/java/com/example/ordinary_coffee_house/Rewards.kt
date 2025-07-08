package com.example.ordinary_coffee_house

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rewards(nav_ctrl: NavController, model: Model, onGoToRedeem: ()->Unit) {
    val context = LocalContext.current
    val loyaltyPoints by model.loyalty_points.collectAsState()
    val totalPoints by model.my_points.collectAsState()
    val rewards_list by remember { mutableStateOf(getRewards(context)) }
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp).padding(bottom = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopAppBar(
                title = {
                    Box (
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Rewards",
                            color = Color(0xFF223140),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 22.sp,
                        )
                    }
                }
            )
            LoyaltyCard(loyaltyPoints = loyaltyPoints)
            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .background(Color(0xFF324A59), shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "My Points:",
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFFD2D7DF),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "$totalPoints",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 30.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            nav_ctrl.navigate("redeem")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF50616E),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            "Redeem drinks",
                            fontFamily = FontFamily.Serif,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "History Rewards",
                fontFamily = FontFamily.Serif,
                color = Color(0xFF192C41),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 12.dp),
            ) {
                items(rewards_list.sortedByDescending { it.time_stamp }) { order ->
                    RewardHistoryUI(order)
                }
            }
        }
        Spacer(Modifier.height(70.dp))
    }
}



@Composable
fun RewardHistoryUI(order: OrderItem) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.name,
                    color = Color(0xFF223140),
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp
                )
                Text(
                    text = order.time_stamp,
                    color = Color(0xFFA5AEBB),
                    fontFamily = FontFamily.Serif,
                    fontSize = 13.sp
                )
            }
            Text(
                text = "+ ${order.points} Pts",
                color = Color(0xFF223140),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.height(6.dp))
        HorizontalDivider(color = Color(0xFFF5F7F9), thickness = 1.dp)
    }
}