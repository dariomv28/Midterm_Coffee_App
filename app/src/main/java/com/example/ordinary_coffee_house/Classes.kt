package com.example.ordinary_coffee_house

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


data class Option(val name: String, val imageRes: Int)

private const val CART_PREFS = "items"
private const val CART_KEY = "cart"

@Serializable
data class CartItem (val id: Int, val name: String, val image_res: Int, val quantity: Int, val shot: String, val select: Int, val size: Int, val ice: Int, val price: Double)

fun setCartItemId(context: Context): Int {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val list_of_items = prefs.getStringSet(CART_KEY, mutableSetOf()) ?: mutableSetOf()
    return list_of_items.size
}

fun toJson(item: CartItem): String {
    return Json.encodeToString(item)
}
fun addToCart(context: Context, cartItem: CartItem) {
    val json = toJson(cartItem)
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val cart_items = prefs.getStringSet(CART_KEY, setOf())?.toMutableSet() ?: mutableSetOf()
    cart_items.add(json)
    prefs.edit().putStringSet(CART_KEY, HashSet(cart_items)).apply()
    Log.d("CartItems", "Added item: $json")
}
fun emptyCart(context: Context) {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    prefs.edit().putStringSet(CART_KEY, mutableSetOf()).apply()
}
fun getCartItems(context: Context): List<CartItem> {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val jsonSet = prefs.getStringSet(CART_KEY, setOf()) ?: setOf()
    return jsonSet.mapNotNull {
        try {
            val item = Json.decodeFromString<CartItem>(it)
            Log.d("CartItems", "Decoded item: $item")
            item
        } catch (e: Exception) {
            Log.e("CartItems", "Failed to decode: $it", e)
            null
        }
    }
}
fun removeCartItem(context: Context, id: Int) {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val jsonSet = prefs.getStringSet(CART_KEY, setOf()) ?: setOf()
    val updatedSet = jsonSet.filter { json ->
        try {
            val item = Json.decodeFromString<CartItem>(json)
            item.id != id
        } catch (e: Exception) {
            true
        }
    }.toSet()
    prefs.edit().putStringSet("cart", updatedSet).apply()
}




@Serializable
data class OrderItem(val id: Int, val time_stamp: String, val name: String, val address: String, val price: Double, val points: Int)

private const val ORDER_ONGOING = "ongoing"
private const val ORDER_HISTORY = "history"
private const val REWARDS_HISTORY = "rewards"
private const val REDEEM_HISTORY = "redeems"
fun saveOngoing(context: Context, orders: List<OrderItem>) {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val jsonSet = orders.map { Json.encodeToString(OrderItem.serializer(), it) }.toSet()
    prefs.edit().putStringSet(ORDER_ONGOING, jsonSet).apply()
}
fun getOngoing(context: Context) : List<OrderItem> {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val jsonSet = prefs.getStringSet(ORDER_ONGOING, setOf()) ?: setOf()
    return jsonSet.mapNotNull {
        try {
            Json.decodeFromString<OrderItem>(it)
        } catch(e: Exception) {
            null
        }
    }
}
fun saveHistory(context: Context, orders: List<OrderItem>) {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val jsonSet = orders.map {
        Json.encodeToString(OrderItem.serializer(), it)
    }.toSet()
    prefs.edit().putStringSet(ORDER_HISTORY, jsonSet).apply()
}
fun clearHistory(context: Context) {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    prefs.edit().putStringSet(ORDER_HISTORY, mutableSetOf()).apply()
}
fun getHistory(context: Context) : List<OrderItem> {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val jsonSet = prefs.getStringSet(ORDER_HISTORY, setOf()) ?: setOf()
    return jsonSet.mapNotNull {
        try{
            Json.decodeFromString<OrderItem>(it)
        } catch(e: Exception) {
            null
        }
    }
}
fun saveRewards(context: Context, orders: List<OrderItem>) {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val jsonSet = orders.map {
        Json.encodeToString(OrderItem.serializer(), it)
    }.toSet()
    prefs.edit().putStringSet(REWARDS_HISTORY, jsonSet).apply()
}
fun getRewards(context: Context) : List<OrderItem> {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val jsonSet = prefs.getStringSet(REWARDS_HISTORY, setOf()) ?: setOf()
    return jsonSet.mapNotNull {
        try{
            Json.decodeFromString<OrderItem>(it)
        } catch (e: Exception) {
            null
        }
    }
}
fun saveRedeemHistory(context: Context, redeems: List<RedeemItem>) {
    val prefs = context.getSharedPreferences(CART_PREFS,Context.MODE_PRIVATE)
    val jsonSet = redeems.map {
        Json.encodeToString(RedeemItem.serializer(), it)
    }.toSet()
    prefs.edit().putStringSet(REDEEM_HISTORY, jsonSet).apply()
}
fun getRedeemHistory(context: Context) : List<RedeemItem> {
    val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val jsonSet = prefs.getStringSet(REDEEM_HISTORY, setOf()) ?: setOf()
    return jsonSet.mapNotNull {
        try{
            Json.decodeFromString<RedeemItem>(it)
        } catch(e: Exception) {
            null
        }
    }
}
fun resetIds(orders: List<OrderItem>): List<OrderItem> {
    return orders.mapIndexed { index, item ->
        item.copy(id = index)
    }
}



@Serializable
data class User (val full_name: String, val phone: String, val email: String, val address: String)

private const val PREFS_USER = "user"
private const val PROFILE_KEY = "profile"

fun saveUser(context: Context, user: User) {
    val prefs = context.getSharedPreferences(PREFS_USER, Context.MODE_PRIVATE)
    val json = Json.encodeToString(user)
    prefs.edit().putString(PROFILE_KEY, json).apply()
}
fun getUser(context: Context) : User {
    val prefs = context.getSharedPreferences(PREFS_USER, Context.MODE_PRIVATE)
    val json = prefs.getString(PROFILE_KEY, null)
    return if (json != null) {
        try {
            Json.decodeFromString<User>(json)
        }
        catch(e: Exception){
            User("","","","")
        }
    } else {
        User("","","","")
    }
}





private const val PREFS_POINTS = "pointPrefs"
private const val LOYALTY_KEY = "loyalty"
private const val MY_POINTS_KEY = "mypoints"

fun saveLoyalty(context: Context, loyaltyPoint: Int) {
    val prefs = context.getSharedPreferences(PREFS_POINTS, Context.MODE_PRIVATE)
    prefs.edit().putInt(LOYALTY_KEY, loyaltyPoint).apply()
}

fun getLoyalty(context: Context): Int {
    val prefs = context.getSharedPreferences(PREFS_POINTS, Context.MODE_PRIVATE)
    val point = prefs.getInt(LOYALTY_KEY, 0)
    return if (point == 9) 1 else point
}

fun saveMyPoints(context: Context, myPoints: Int) {
    val prefs = context.getSharedPreferences(PREFS_POINTS, Context.MODE_PRIVATE)
    prefs.edit().putInt(MY_POINTS_KEY, myPoints).apply()
}

fun getMyPoints(context: Context) : Int {
    val prefs = context.getSharedPreferences(PREFS_POINTS, Context.MODE_PRIVATE)
    return prefs.getInt(MY_POINTS_KEY, 0)
}


class Model(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val _user = MutableStateFlow(getUser(context))
    val user: StateFlow<User> = _user
    private val _loyalty = MutableStateFlow(getLoyalty(context))
    val loyalty_points: StateFlow<Int> = _loyalty
    private val _myPoints = MutableStateFlow(getMyPoints(context))
    val my_points: StateFlow<Int> = _myPoints

    fun update() {
        _user.value = getUser(context)
        _loyalty.value = getLoyalty(context)
        _myPoints.value = getMyPoints(context)
    }
}





fun discount20(context: Context) {
    val sharedPreferences = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val cartItemsJsonSet = sharedPreferences.getStringSet(CART_KEY, setOf()) ?: setOf()
    val updatedJsonSet = cartItemsJsonSet.mapNotNull { json ->
        try {
            val item = Json.decodeFromString<CartItem>(json)
            val discountedItem = item.copy(price = item.price * 0.8)
            Json.encodeToString(discountedItem)
        } catch (e: Exception) {
            null
        }
    }.toSet()
    sharedPreferences.edit().putStringSet(CART_KEY, updatedJsonSet).apply()
}

fun cancelDiscount20(context: Context) {
    val sharedPreferences = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
    val cartItemsJsonSet = sharedPreferences.getStringSet(CART_KEY, setOf()) ?: setOf()
    val updatedJsonSet = cartItemsJsonSet.mapNotNull { json ->
        try {
            val item = Json.decodeFromString<CartItem>(json)
            val discountedItem = item.copy(price = item.price / 0.8)
            Json.encodeToString(discountedItem)
        } catch (e: Exception) {
            null
        }
    }.toSet()
    sharedPreferences.edit().putStringSet(CART_KEY, updatedJsonSet).apply()
}

