package com.criticove

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController

import com.criticove.backend.FirebaseManager
import com.criticove.backend.userModel

@Composable
fun LoginMainContent(navController: NavController, userModel: userModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoginEnabled by remember { mutableStateOf(false) }
    fun checkLoginEnabled() {
        isLoginEnabled = email.isNotBlank() && password.isNotBlank()
    }

    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue))
            .padding(bottom = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 10.dp)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = FontFamily(Font(R.font.righteous_regular)),
                color = colorResource(id = R.color.yellow),
                fontSize = 40.sp
            )
        }

        Column(
            modifier = Modifier
                .width(300.dp)
                .background(colorResource(id = R.color.green), shape = RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = errorMessage,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                color = colorResource(id = R.color.red),
                fontSize = 18.sp
            )

            OutlinedTextFieldLogin("Email") {
                email = it
                checkLoginEnabled()
            }

            OutlinedTextFieldLogin("Password", true) {
                password = it
                checkLoginEnabled()
            }

            Button(
                onClick = {
                    FirebaseManager.login(email, password) { success ->
                        if (success) {
                            userModel.loginUser()
                            userModel.getCurUser()
                            errorMessage = ""
                            navController.navigate("Dashboard")
                        } else {
                            errorMessage = "Login failed. Try again."
                        }
                    }
                },
                enabled = isLoginEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.teal),
                    contentColor = colorResource(id = R.color.yellow)
                ),
                modifier = Modifier
                    .width(150.dp)
                    .padding(top = 20.dp, bottom = 30.dp),
            ) {
                Text(
                    text = "Login",
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                    color = colorResource(id = R.color.off_white),
                    fontSize = 20.sp
                )
            }
        }

        Row(
            modifier = Modifier.padding(50.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 5.dp),
                text = stringResource(id = R.string.dont_have_account),
                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                color = colorResource(id = R.color.yellow),
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier.clickable { navController.navigate("Signup") },
                text = stringResource(id = R.string.signup),
                style = TextStyle(textDecoration = TextDecoration.Underline),
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                color = colorResource(id = R.color.yellow),
                fontSize = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldLogin(
    placeholder: String,
    isPassword: Boolean = false,
    onValueChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChanged(it)
        },
        modifier = Modifier
            .width(280.dp)
            .padding(10.dp)
            .background(colorResource(id = R.color.green), shape = RoundedCornerShape(10.dp)),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = colorResource(id = R.color.off_white),
            focusedBorderColor = colorResource(id = R.color.blue),
            unfocusedBorderColor = colorResource(id = R.color.teal)
        ),
        textStyle = TextStyle(fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))),
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                )
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
    )
}
