package com.bumperpick.bumperickUser.Screens.Support



import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage

import com.bumperpick.bumperickUser.API.New_model.tickerdetails
import com.bumperpick.bumperickUser.API.New_model.ticket_add_model
import com.bumperpick.bumperickUser.API.New_model.ticketmessage
import com.bumperpick.bumperpickvendor.API.FinalModel.Message

import com.bumperpick.bumperpickvendor.API.Model.success_model
import com.bumperpick.bumperpickvendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*



// Colors matching the screenshots
val PrimaryRed = BtnColor
val LightGray = Color(0xFFF5F5F5)
val DarkGray = Color(0xFF666666)
val MessageBubbleGray = Color(0xFFE8E8E8)



// Support Tickets List Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportTicketsScreen(
    onBackPressed:()-> Unit,
    gototicketdetail:(id: String)-> Unit,
    viewModel: SupportViewModel= koinViewModel ()
) { val statusBarColor = Color(0xFF5A0E26)
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // true for dark icons on light background
        )
    }
    val ticketsState by viewModel.ticketsState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.getTickets()
    }

    var size by remember { mutableStateOf(IntSize.Zero) }
    val backgroundModifier = remember(size) {
        if (size.width > 0 && size.height > 0) {
            val radius = kotlin.comparisons.maxOf(size.width, size.height) / 1.5f
            Modifier.background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF8B1538), Color(0xFF5A0E26)),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = radius
                )
            )
        } else {
            Modifier.background(Color(0xFF8B1538))
        }
    }

    Scaffold {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGray)
            ) {
                // Top Bar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { size = it },
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    ),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(backgroundModifier)
                            .padding(bottom = 0.dp)
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))

                        // Top App Bar with improved spacing
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                IconButton(
                                    onClick = onBackPressed,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Support Tickets",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Content based on state
                when (ticketsState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryRed)
                        }
                    }

                    is UiState.Success -> {
                        // Safe cast - we know it's Success here
                        val successState = ticketsState as UiState.Success<ticketmessage>
                        val tickets = successState.data.data

                        if (tickets.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(tickets) { ticket ->
                                    TicketItem(
                                        subject = ticket.subject ?: "No Subject",
                                        date = ticket.created_at ?: "",
                                        status = "open", // Assuming all are open based on screenshot
                                        onClick = {
                                            // Navigate to ticket details
                                            gototicketdetail(ticket.id.toString())
                                        }
                                    )
                                }
                            }
                        } else {
                            // Handle empty tickets list
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No tickets found",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Create your first support ticket",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    is UiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error loading tickets",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Red
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = (ticketsState as UiState.Error).message,
                                    fontSize = 14.sp,
                                    color = Color.Red,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.getTickets() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryRed
                                    )
                                ) {
                                    Text("Retry", color = Color.White)
                                }
                            }
                        }
                    }

                    is UiState.Empty -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No tickets found",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Create your first support ticket",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Floating Action Button
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = PrimaryRed,
                shape = CircleShape,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Ticket")
            }

            // Bottom Sheet
            if (showBottomSheet) {
                NewTicketBottomSheetContent(
                    onDismiss = {
                        showBottomSheet = false
                        viewModel.getTickets() // Refresh tickets after creating new one
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}

// New Ticket Bottom Sheet Content
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTicketBottomSheetContent(
    onDismiss: () -> Unit,
    viewModel: SupportViewModel
) {
    val ticketAddState by viewModel.ticketAddState.collectAsStateWithLifecycle()
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    // Clear form and state when sheet opens
    LaunchedEffect(true) {
        subject = ""
        message = ""
        viewModel.resetTicketAddState()
    }

    // Handle Success/Error UI state
    LaunchedEffect(ticketAddState) {
        when (ticketAddState) {
            is UiState.Success -> {
                subject = ""
                message = ""
                show_toast("Ticket submitted successfully", context)
                viewModel.resetTicketAddState()
                onDismiss()
            }
            is UiState.Error -> {
                show_toast((ticketAddState as UiState.Error).message, context)
                Log.d("Error", (ticketAddState as UiState.Error).message)
                subject = ""
                message = ""
                viewModel.resetTicketAddState()
            }
            else -> Unit
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        contentColor = Color.Black,
        dragHandle = null,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = Color.Gray.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(2.dp)
                    )
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "New Support Ticket",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Describe your issue and we'll help you resolve it",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Subject",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    placeholder = {
                        Text(
                            "Enter ticket subject",
                            color = Color.Gray.copy(alpha = 0.7f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryRed,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        cursorColor = PrimaryRed
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Description",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = {
                        Text(
                            "Provide detailed description of your issue...",
                            color = Color.Gray.copy(alpha = 0.7f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(bottom = 32.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryRed,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        cursorColor = PrimaryRed
                    ),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    maxLines = 6
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryRed),
                    border = BorderStroke(1.dp, PrimaryRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        if (subject.isNotBlank() && message.isNotBlank()) {
                            viewModel.addTicket(subject.trim(), message.trim())
                            keyboardController?.hide()
                        }
                    },
                    enabled = subject.isNotBlank() && message.isNotBlank() && ticketAddState !is UiState.Loading,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryRed,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    if (ticketAddState is UiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Submit Ticket", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

private fun show_toast(message: String, context: Context) {
    Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
}

// Ticket Item Component
@Composable
fun TicketItem(
    subject: String,
    date: String,
    status: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            Column(
                modifier = Modifier.weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = subject,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, // Using as calendar icon
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = DarkGray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatDate(date),
                        fontSize = 12.sp,
                        color = DarkGray
                    )
                }
            }

            // Status badge
            Surface(
                color = Color(0xFFFFF3CD),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.Top)
            ) {
                Text(
                    text = status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = Color(0xFF856404),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Ticket Details Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailsScreen(
    ticketId: String,
    onBackPressed: () -> Unit,
    viewModel: SupportViewModel = koinViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val ticketDetailState by viewModel.ticketDetailState.collectAsStateWithLifecycle()
    val ticketReplyState by viewModel.ticketReplyState.collectAsStateWithLifecycle()
    var replyMessage by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val statusBarColor = Color(0xFF5A0E26)
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // true for dark icons on light background
        )
    }
    // Colors
    val PrimaryRed = BtnColor
    val LightGray = Color(0xFFF5F5F5)
    val DarkGray = Color(0xFF666666)
    val MessageBubbleGray = Color(0xFFE8E8E8)

    LaunchedEffect(ticketId) {
        viewModel.getTicketDetails(ticketId)
    }

    // Handle reply success without full refresh
    LaunchedEffect(ticketReplyState) {
        if (ticketReplyState is UiState.Success) {
            replyMessage = ""
            viewModel.getTicketDetails(ticketId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
    ) {
        when (ticketDetailState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryRed)
                }
            }
            is UiState.Success -> {
                val ticket = (ticketDetailState as UiState.Success<tickerdetails>).data

                // Top Bar
                TopAppBar(
                    title = {
                        Text(
                            text = ticket.data.subject ?: "Ticket Details",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBackPressed() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        Surface(
                            color = Color(0xFFFFF3CD),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = "Open",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = Color(0xFF856404),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Messages List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ticket.data.messages?.let { messages ->
                            items(messages) { message ->
                                MessageCard(
                                    message = message,
                                    primaryRed = PrimaryRed,
                                    messageBubbleGray = MessageBubbleGray,
                                    darkGray = DarkGray
                                )
                            }
                        }
                    }

                    // Reply Section
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            // Reply input with integrated send button
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val keyboardController = LocalSoftwareKeyboardController.current

                                OutlinedTextField(
                                    value = replyMessage,
                                    onValueChange = { replyMessage = it },
                                    placeholder = {
                                        Text(
                                            "Type your reply...",
                                            color = DarkGray.copy(alpha = 0.6f)
                                        )
                                    },
                                    trailingIcon = {
                                        if (ticketReplyState is UiState.Loading) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(18.dp),
                                                color = BtnColor,
                                                strokeWidth = 2.dp
                                            )
                                        } else {
                                            Icon(
                                                Icons.Default.Send,
                                                contentDescription = "Send",
                                                tint = if (replyMessage.isNotBlank() && ticketReplyState !is UiState.Loading)
                                                    PrimaryRed else DarkGray.copy(alpha = 1f),
                                                modifier = Modifier.size(18.dp).clickable{
                                                    if (replyMessage.isNotBlank()) {
                                                        viewModel.replyToTicket(ticketId, replyMessage)
                                                        keyboardController?.hide()
                                                    }
                                                }
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(5f)
                                        .padding(8.dp), // optional outer padding
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryRed,
                                        cursorColor = PrimaryRed,
                                        unfocusedBorderColor = DarkGray.copy(alpha = 0.3f)
                                    ),
                                    shape = RoundedCornerShape(28.dp),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                    keyboardActions = KeyboardActions(
                                        onSend = {
                                            if (replyMessage.isNotBlank()) {
                                                viewModel.replyToTicket(ticketId, replyMessage)
                                                keyboardController?.hide()
                                            }
                                        }
                                    ),
                                    maxLines = 4,
                                    minLines = 1
                                )


                                // Circular send button inside text field

                            }
                        }
                    }
                }
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector =  Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = (ticketDetailState as UiState.Error).message,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.getTicketDetails(ticketId) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                    ) {
                        Text("Retry")
                    }
                }
            }
            is UiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Outlined.Email,
                            contentDescription = null,
                            tint = DarkGray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No ticket details found",
                            color = DarkGray,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun MessageCard(
    message: Message, // Replace with your actual message data class
    primaryRed: Color,
    messageBubbleGray: Color,
    darkGray: Color
) {
    val blue= Color(0xff1f76b8)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (message.author.type.lowercase() == "user")
                blue.copy(alpha = 0.1f)
            else
                BtnColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with author and timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card (shape = CircleShape,
                        modifier = Modifier.size(36.dp),
                        colors = CardDefaults.cardColors(containerColor =
                            if (message.author.type.lowercase() == "user")
                                blue.copy(0.1f) else BtnColor.copy(0.1f)
                        )){
                        val errorpainter = rememberVectorPainter(image = Icons.Default.Person)
                        AsyncImage(model = message.author.id,
                            error = errorpainter,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Fit
                        )

                    }



                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text =  if (message.author.type.lowercase() == "user")
                            message.author.name?:"" else "You",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }

                Text(
                    text = formatDateTime(message.created_at ?: ""),
                    fontSize = 12.sp,
                    color = darkGray.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Message body
            Text(
                text = message.body ?: "",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )
        }
    }
}

// Helper function to format date and time
private fun formatDateTime(dateTime: String): String {
    return try {
        // Parse your datetime format and return formatted string
        // Example: "Jan 15, 2024 • 2:30 PM"
        // You'll need to implement this based on your datetime format
        val formatter = SimpleDateFormat("MMM dd, yyyy • h:mm a", Locale.getDefault())
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dateTime)
        formatter.format(date ?: Date())
    } catch (e: Exception) {
        dateTime
    }
}


// Utility function to format date
fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}