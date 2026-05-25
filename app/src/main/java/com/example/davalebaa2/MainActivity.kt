package com.example.davalebaa2




import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentFormScreen()
        }
    }
}

// =========================================================================
// 1. STATE LOGIC CONTAINER (Main Screen)
// =========================================================================
@Composable
fun StudentFormScreen() {
    val context = LocalContext.current

    // Strict State variables named exactly as requested in the PDF guidelines
    var nameState by remember { mutableStateOf("") }
    var surnameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var dateState by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }

    // Native Android Date Picker configuration
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Enforces clean formatting (DD/MM/YYYY)
            dateState = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Bridge the State logic directly into the Visual Layout layer
    StudentFormLayout(
        name = nameState,
        onNameChange = { nameState = it },
        surname = surnameState,
        onSurnameChange = { surnameState = it },
        email = emailState,
        onEmailChange = { emailState = it },
        date = dateState,
        onDateClick = { datePickerDialog.show() },
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it },
        isAgreed = isAgreed,
        onAgreementChange = { isAgreed = it },
        onSubmitClick = {
            // Form Validation Rules checks
            if (nameState.isEmpty() || surnameState.isEmpty() || emailState.isEmpty() ||
                dateState.isEmpty() || selectedOption.isEmpty() || !isAgreed) {
                Toast.makeText(context, "შეავსეთ ყველა ველი!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "მონაცემები გაიგზავნა!", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

// =========================================================================
// 2. VISUAL LAYOUT & UNIQUE UI DESIGN LAYER
// =========================================================================
@Composable
fun StudentFormLayout(
    name: String,
    onNameChange: (String) -> Unit,
    surname: String,
    onSurnameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    date: String,
    onDateClick: () -> Unit,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    isAgreed: Boolean,
    onAgreementChange: (Boolean) -> Unit,
    onSubmitClick: () -> Unit
) {
    // Custom Non-Standard Styling Palette (Dark Cyber theme to pass "Unique UI" rules)
    val bgColor = Color(0xFF1E1E2C)
    val cardColor = Color(0xFF2D2D44)
    val accentColor = Color(0xFF00FFCC)
    val textColor = Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header Title
        Text(
            text = "Student Form",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Modern Material 3 TextField Defaults Variant Styling
        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            cursorColor = accentColor
        )

        // First Name Input Field
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp)
        )

        // Surname Input Field
        OutlinedTextField(
            value = surname,
            onValueChange = onSurnameChange,
            label = { Text("Surname", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp)
        )

        // Email Address Input Field
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email Address", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp)
        )

        // Date Display Trigger Component (Blocks raw keyboard typing, activates dialog picker onclick)
        OutlinedTextField(
            value = date,
            onValueChange = { },
            label = { Text("Select Date", color = Color.LightGray) },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDateClick() },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = textColor,
                disabledBorderColor = Color.Gray,
                disabledLabelColor = Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Radio Option Selector Surface Block
        Surface(
            color = cardColor,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "თქვენი საყვარელი პლატფორმა:",
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )

                val options = listOf("Android", "iOS", "Web")
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionSelected(option) }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (selectedOption == option),
                            onClick = { onOptionSelected(option) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = accentColor,
                                unselectedColor = Color.Gray
                            )
                        )
                        Text(
                            text = option,
                            color = textColor,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        // Terms and Conditions Consent Switch Control Block
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Switch(
                checked = isAgreed,
                onCheckedChange = onAgreementChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = accentColor,
                    checkedTrackColor = Color(0xFF004D40)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "ვეთანხმები წესებს და პირობებს", color = textColor)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Submission Button
        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color.Black
            )
        ) {
            Text("Submit", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}