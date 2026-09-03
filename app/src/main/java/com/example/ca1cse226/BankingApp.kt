package com.example.ca1cse226

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ca1cse226.ui.theme.CA1CSE226Theme

data class Transaction(
    val id: Int,
    val title: String,
    val amount: Double
)

class BankingApp : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CA1CSE226Theme {
                TransactionScreen()
            }
        }
    }
}

@Composable
fun TransactionScreen() {

    val listState = rememberLazyListState()

    val showQuickFilter by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 3
        }
    }

    var transactionToDelete by remember {
        mutableStateOf<Transaction?>(null)
    }

    val transactions = remember {
        List(50) { index ->
            Transaction(
                id = index,
                title = "Transaction ${index + 1}",
                amount = (index + 1) * 100.0
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 32.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(
                items = transactions,
                key = { transaction -> transaction.id }
            ) { transaction ->

                TransactionItem(
                    transaction = transaction,
                    onDeleteClick = {
                        transactionToDelete = transaction
                    }
                )
            }
        }

        if (showQuickFilter) {

            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text("Filter")
            }
        }

        transactionToDelete?.let { transaction ->

            DeleteConfirmationDialog(
                transaction = transaction,
                onConfirm = {
                    transactionToDelete = null
                },
                onDismiss = {
                    transactionToDelete = null
                }
            )
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onDeleteClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "₹${transaction.amount}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            TextButton(
                onClick = onDeleteClick
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    transaction: Transaction,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Delete Transaction?")
        },

        text = {
            Text(
                "Are you sure you want to delete ${transaction.title}?"
            )
        },

        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Delete")
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BankingAppPreview() {
    CA1CSE226Theme {
        TransactionScreen()
    }
}