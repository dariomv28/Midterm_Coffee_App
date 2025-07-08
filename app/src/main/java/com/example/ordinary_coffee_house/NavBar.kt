package com.example.ordinary_coffee_house

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NavBar(selection: Int, onTabSelected: (Int) -> Unit) {
    val items = listOf( R.drawable.ic_home,  R.drawable.ic_gift,  R.drawable.ic_order)

    val bgColor = Color(0xFFF7F7F7)
    val activeColor = Color(0xFF344054)
    val inactiveColor = Color(0xFF344054).copy(alpha = 0.2f)

    Box(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(64.dp)
            .shadow(12.dp, RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
            .background(bgColor)
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 26.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, icon ->
                IconButton(
                    onClick = {
                        onTabSelected(index)
                    }
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = "",
                        tint = if (index == selection) activeColor else inactiveColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun BottomNavBarPreview() {
    NavBar(selection = 1, onTabSelected = {})
}