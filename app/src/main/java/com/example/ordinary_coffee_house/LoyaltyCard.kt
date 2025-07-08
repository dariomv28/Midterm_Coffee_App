package com.example.ordinary_coffee_house

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoyaltyCard(loyaltyPoints: Int) {
    Box(
        modifier = Modifier.background(Color(0xFF324A59), shape = RoundedCornerShape(12.dp)).padding(16.dp).width(325.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loyalty card",
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFD8D8D8),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$loyaltyPoints / 8",
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFD8D8D8),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(61.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(8) { index ->
                        val iconTint = if (index < loyaltyPoints)
                            Color(0xFFD77C2B)
                        else
                            Color(0xFFD8D8D8)
                        Image(
                            painter = painterResource(id = R.drawable.loyalty_cup),
                            contentDescription = "Coffee cup",
                            modifier = Modifier.size(35.dp),
                            colorFilter = ColorFilter.tint(iconTint)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun PreviewLoyaltyCard() {
    LoyaltyCard(loyaltyPoints = 4)
}
