package com.example.ordinary_coffee_house

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun OrderSuccess(nav_ctrl: NavController, onGoToOrder: ()->Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7F9))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(top = 80.dp, start = 18.dp, end = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.order_success_logo),
                contentDescription = "Order Success",
                modifier = Modifier.size(180.dp).padding(bottom = 16.dp),
                tint = Color(0xFF223140)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Order Success",
                color = Color(0xFF192C41),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Your order has been placed successfully.\nFor more details, go to my orders.",
                color = Color(0xFFA5AEBB),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onGoToOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A4F5C),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Track My Order",
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}


