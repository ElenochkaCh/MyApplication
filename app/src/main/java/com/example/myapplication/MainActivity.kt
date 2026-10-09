package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(
                        onOpenSecond = { text ->
                            val intent = Intent().apply {
                                setClassName(packageName, "$packageName.SecondActivity")
                                putExtra("EXTRA_TEXT", text)
                            }
                            startActivity(intent)
                        },
                        onCallFriend = { phone ->
                            startActivity(Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:$phone") })
                        },
                        onShareText = { text ->
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, text)
                                type = "text/plain"
                            }
                            startActivity(Intent.createChooser(sendIntent, "Share"))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    onOpenSecond: (String) -> Unit,
    onCallFriend: (String) -> Unit,
    onShareText: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; if (it.isNotBlank()) isError = false },
            label = { Text("Введите текст или номер") },
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { if (text.isBlank()) isError = true else onOpenSecond(text) }, modifier = Modifier.fillMaxWidth()) { Text("1. Открыть вторую Activity") }
        Button(onClick = { if (text.isBlank()) isError = true else onCallFriend(text) }, modifier = Modifier.fillMaxWidth()) { Text("2. Позвонить другу") }
        Button(onClick = { if (text.isBlank()) isError = true else onShareText(text) }, modifier = Modifier.fillMaxWidth()) { Text("3. Поделиться текстом") }
    }
}