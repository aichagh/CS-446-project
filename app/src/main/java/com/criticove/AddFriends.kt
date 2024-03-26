package com.criticove

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.criticove.backend.userModel


//var ognewFriendsList = mutableStateListOf(
//    Friend("Aras"),
//    Friend("Meer"),
//    Friend("Amu"),
//    Friend("Ahtsimrahs"),
//    Friend("Atimhsa"),
//    Friend("Ahcia")
//)



//@SuppressLint("UnrememberedMutableState")
@SuppressLint("UnrememberedMutableState")
@Composable
fun createNewFriendsList(usermodel: userModel): SnapshotStateList<Friend> {
    usermodel.getUsers()
    var FriendsList = mutableStateListOf<Friend>()
    val user_map by usermodel.userMap.collectAsState()
    for ((key, value) in user_map) {
        FriendsList.add(Friend(value))
    }
    return FriendsList
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriends(navController: NavController, usermodel: userModel) {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var filteredFriends by remember { mutableStateOf(emptyList<Friend>()) }
    var ognewFriendsList = createNewFriendsList(usermodel)

    fun perform_search() {
        filteredFriends = if (isSearchActive) {
            ognewFriendsList.filter {
                it.username.contains(searchText, ignoreCase = true)
            }
        } else {
            ognewFriendsList
        }
    }

    perform_search()
    MainLayout(
        title = "Add Friends",
        navController = navController,
        friends = true
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(id = R.color.off_white))
                .fillMaxHeight()
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .weight(1F)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        isSearchActive = it.isNotEmpty()
                        perform_search()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = colorResource(id = R.color.off_white),
                        focusedBorderColor = colorResource(id = R.color.blue),
                        unfocusedBorderColor = colorResource(id = R.color.teal)
                    ),
                    label = {
                        Text(
                            text = "Search friends ...",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.search),
                            contentDescription = "search", tint = colorResource(id = R.color.black),
                            modifier = Modifier
                                .size(24.dp)

                        )
                    },

                    )
                if (searchText.isNotEmpty()) {
                    LazyColumn {
                        items(filteredFriends) { friend ->
                            AddFriends(friend, ognewFriendsList, usermodel)
                        }
                    }
                }
            }

            Navbar(navController)
        }
    }
}

@Composable
fun AddFriends(friend: Friend, ognewFriendsList: SnapshotStateList<Friend>, usermodel: userModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 15.dp, 15.dp, 0.dp)
            .clip(shape = RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.green))
                .padding(4.dp, 4.dp, 20.dp, 4.dp)
        ) {
            TextButton(
                modifier = Modifier
                    .width(50.dp),
                onClick = {
                    /* Nothing */
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.default_profile),
                    contentDescription = "profile", tint = colorResource(id = R.color.black)
                )
            }

            Column(
                modifier = Modifier
                    .width(30.dp)
                    .weight(1F)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "${friend.username}",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                    )
                )

            }

            TextButton(
                modifier = Modifier
                    .width(50.dp),
                onClick = {
//                    ogcurFriendsList.add(Friend(friend.username))
                    usermodel.addFriend(friend.username)
                    remove_new_friend(friend.username, ognewFriendsList)
                    println("the new list of new friends is $ognewFriendsList")}
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.add_friend_svgrepo_com),
                    contentDescription = "add", tint = colorResource(id = R.color.black)
                )
            }
        }
    }
}

fun remove_new_friend(username: String, ognewFriendsList: SnapshotStateList<Friend>) {
    ognewFriendsList.remove(Friend(username))
}