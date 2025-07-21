package com.vpk.hackerfeed

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vpk.hackerfeed.components.ThemedTopAppBar
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme

class SettingsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HackerFeedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val localContext = LocalContext.current

    Scaffold(
        topBar = {
            ThemedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        (localContext as? ComponentActivity)?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back_content_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                SettingsSection(
                    title = stringResource(R.string.app_settings_section),
                    items = listOf(
                        SettingsItem(
                            title = stringResource(R.string.cache_management),
                            description = stringResource(R.string.manage_app_cache),
                            icon = Icons.Filled.Storage,
                            onClick = {
                                val intent = Intent(localContext, CacheManagementActivity::class.java)
                                localContext.startActivity(intent)
                            }
                        )
                    )
                )
            }
            
            item {
                SettingsSection(
                    title = stringResource(R.string.privacy_legal_section),
                    items = listOf(
                        SettingsItem(
                            title = stringResource(R.string.privacy_policy),
                            description = stringResource(R.string.privacy_policy_description),
                            icon = Icons.Filled.Policy,
                            onClick = {
                                val intent = LegalInfoActivity.createPrivacyPolicyIntent(localContext)
                                localContext.startActivity(intent)
                            }
                        ),
                        SettingsItem(
                            title = stringResource(R.string.terms_conditions),
                            description = stringResource(R.string.terms_conditions_description),
                            icon = Icons.Filled.Description,
                            onClick = {
                                val intent = LegalInfoActivity.createTermsConditionsIntent(localContext)
                                localContext.startActivity(intent)
                            }
                        ),
                        SettingsItem(
                            title = stringResource(R.string.data_protection),
                            description = stringResource(R.string.data_protection_description),
                            icon = Icons.Filled.Security,
                            onClick = {
                                val intent = LegalInfoActivity.createDataProtectionIntent(localContext)
                                localContext.startActivity(intent)
                            }
                        )
                    )
                )
            }
            
            item {
                SettingsSection(
                    title = stringResource(R.string.general_section),
                    items = listOf(
                        SettingsItem(
                            title = stringResource(R.string.about),
                            description = stringResource(R.string.about_app_description),
                            icon = Icons.Filled.Info,
                            onClick = {
                                val intent = Intent(localContext, AboutActivity::class.java)
                                localContext.startActivity(intent)
                            }
                        )
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSection(
    title: String,
    items: List<SettingsItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        supportingContent = {
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { item.onClick() }
                    )
                }
            }
        }
    }
}

data class SettingsItem(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    HackerFeedTheme {
        SettingsScreen()
    }
}
