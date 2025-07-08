package com.example.ordinary_coffee_house

import android.content.Context
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class EditableField(val key: String, val label: String, val value: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(onBackPressed: () -> Unit = {}, onShowNavBar: () -> Unit = {}, onUserSaved: () -> Unit = {}) {
    val context = LocalContext.current
    var user by remember { mutableStateOf(getUser(context)) }
    var editing by remember { mutableStateOf<EditableField?>(null) }
    var editingValue by remember { mutableStateOf("") }

    val profileFields = listOf(
        EditableField("fullName", "Full name", user.full_name, Icons.Default.Person),
        EditableField("phone", "Phone number", user.phone, Icons.Default.Phone),
        EditableField("email", "Email", user.email, Icons.Default.Email),
        EditableField("address", "Address", user.address, Icons.Default.LocationOn)
    )

    Box(Modifier.fillMaxSize().background(Color(0xFFF5F7F9))) {
        Column {
            TopAppBar(
                title = {
                    Box(
                        Modifier.fillMaxWidth().padding(end = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Profile",
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
                        onShowNavBar()
                    }) {
                        Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Back", tint = Color(0xFF132032))
                    }
                }
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(profileFields) { field ->
                    ProfileFieldUI(
                        field = field,
                        onEdit = {
                            editing = field
                            editingValue = field.value
                        }
                    )
                }
            }
        }
        if (editing != null) {
            EditProfileFieldDialog(
                field = editing!!,
                value = editingValue,
                onValueChange = { editingValue = it },
                onCancel = { editing = null },
                onSave = {
                    user = when (editing!!.key) {
                        "fullName" -> user.copy(full_name = editingValue)
                        "phone" -> user.copy(phone = editingValue)
                        "email" -> user.copy(email = editingValue)
                        "address" -> user.copy(address = editingValue)
                        else -> user
                    }
                    saveUser(context, user)
                    onUserSaved()
                    editing = null
                }
            )
        }
    }
}

@Composable
fun ProfileFieldUI(field: EditableField, onEdit: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            field.icon,
            contentDescription = field.label,
            tint = Color(0xFFB5C2CE),
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(field.label, color = Color(0xFFB5C2CE), fontSize = 13.sp, fontFamily = FontFamily.Serif)
            Text(field.value, fontWeight = FontWeight.Bold, color = Color(0xFF223140), fontSize = 16.sp, fontFamily = FontFamily.Serif)
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Edit ${field.label}", tint = Color(0xFF324A59))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileFieldDialog(
    field: EditableField,
    value: String,
    onValueChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(onClick = onSave) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onCancel) { Text("Cancel") }
        },
        title = { Text("Edit ${field.label}") },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    )
}