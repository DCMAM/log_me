package com.logmeapp.activity.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.logmeapp.activity.data.ActivityLog

data class LogTemplate(
    val id: String,
    val name: String,
    val description: String,
    val isPremium: Boolean,
    val color: Color
)

val availableTemplates = listOf(
    LogTemplate("basic", "Basic Log", "Simple activity log", false, Color(0xFF2196F3)),
    LogTemplate("premium_1", "Detailed Journal", "With mood tracking and location", true, Color(0xFF9C27B0)),
    LogTemplate("premium_2", "Professional Log", "For work activities with tags", true, Color(0xFFFF9800)),
    LogTemplate("premium_3", "Health Tracker", "Track health & wellness activities", true, Color(0xFF4CAF50))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLogScreen(
    onSave: (ActivityLog) -> Unit,
    onBack: () -> Unit,
    isTemplateUnlocked: (String) -> Boolean,
    onUnlockTemplate: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Personal") }
    var selectedTemplate by remember { mutableStateOf(availableTemplates[0]) }
    var mood by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showTemplateDialog by remember { mutableStateOf(false) }
    
    val categories = listOf("Personal", "Work", "Health", "Study", "Social", "Other")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Activity Log") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Template Selection Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTemplateDialog = true },
                colors = CardDefaults.cardColors(
                    containerColor = selectedTemplate.color.copy(alpha = 0.1f)
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Template",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                selectedTemplate.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = selectedTemplate.color
                            )
                            if (selectedTemplate.isPremium) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Template"
                    )
                }
            }
            
            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Edit, contentDescription = null)
                },
                singleLine = true
            )
            
            // Category Selection
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCategoryDialog = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.List, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Category",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                category,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Category"
                    )
                }
            }
            
            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                leadingIcon = {
                    Icon(Icons.Default.Create, contentDescription = null)
                },
                maxLines = 6
            )
            
            // Premium Template Fields
            if (selectedTemplate.id == "premium_1") {
                OutlinedTextField(
                    value = mood,
                    onValueChange = { mood = it },
                    label = { Text("Mood") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                    },
                    placeholder = { Text("How are you feeling?") }
                )
                
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Place, contentDescription = null)
                    },
                    placeholder = { Text("Where did this happen?") }
                )
            }
            
            if (selectedTemplate.id == "premium_2") {
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                    },
                    placeholder = { Text("Comma-separated tags") }
                )
            }
            
            if (selectedTemplate.id == "premium_3") {
                OutlinedTextField(
                    value = mood,
                    onValueChange = { mood = it },
                    label = { Text("Health Status") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Favorite, contentDescription = null)
                    },
                    placeholder = { Text("How's your health?") }
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Save Button
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        val log = ActivityLog(
                            title = title,
                            description = description,
                            category = category,
                            templateId = selectedTemplate.id,
                            mood = mood.ifBlank { null },
                            location = location.ifBlank { null },
                            tags = tags.ifBlank { null }
                        )
                        onSave(log)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = title.isNotBlank() && description.isNotBlank()
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Activity Log", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    
    // Category Selection Dialog
    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Select Category") },
            text = {
                Column {
                    categories.forEach { cat ->
                        TextButton(
                            onClick = {
                                category = cat
                                showCategoryDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                cat,
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = if (cat == category) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showCategoryDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Template Selection Dialog
    if (showTemplateDialog) {
        AlertDialog(
            onDismissRequest = { showTemplateDialog = false },
            title = { Text("Select Template") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableTemplates.forEach { template ->
                        val isUnlocked = isTemplateUnlocked(template.id)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = isUnlocked) {
                                    if (isUnlocked) {
                                        selectedTemplate = template
                                        showTemplateDialog = false
                                    } else {
                                        onUnlockTemplate()
                                        showTemplateDialog = false
                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (template.id == selectedTemplate.id) {
                                    template.color.copy(alpha = 0.2f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            ),
                            border = if (!isUnlocked) {
                                CardDefaults.outlinedCardBorder()
                            } else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            template.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (template.isPremium) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color(0xFFFFD700),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        template.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (!isUnlocked) {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = "Locked",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showTemplateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
