package com.vpk.hackerfeed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vpk.hackerfeed.components.ThemedTopAppBar
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme

class LegalInfoActivity : ComponentActivity() {
    
    companion object {
        private const val EXTRA_TITLE_RES_ID = "title_res_id"
        private const val EXTRA_CONTENT_RES_ID = "content_res_id"
        
        fun createDataProtectionIntent(context: Context): Intent {
            return Intent(context, LegalInfoActivity::class.java).apply {
                putExtra(EXTRA_TITLE_RES_ID, R.string.data_protection_title)
                putExtra(EXTRA_CONTENT_RES_ID, R.string.data_protection_content)
            }
        }
        
        fun createPrivacyPolicyIntent(context: Context): Intent {
            return Intent(context, LegalInfoActivity::class.java).apply {
                putExtra(EXTRA_TITLE_RES_ID, R.string.privacy_policy_title)
                putExtra(EXTRA_CONTENT_RES_ID, R.string.privacy_policy_content)
            }
        }
        
        fun createTermsConditionsIntent(context: Context): Intent {
            return Intent(context, LegalInfoActivity::class.java).apply {
                putExtra(EXTRA_TITLE_RES_ID, R.string.terms_conditions_title)
                putExtra(EXTRA_CONTENT_RES_ID, R.string.terms_conditions_content)
            }
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val titleResId = intent.getIntExtra(EXTRA_TITLE_RES_ID, R.string.data_protection_title)
        val contentResId = intent.getIntExtra(EXTRA_CONTENT_RES_ID, R.string.data_protection_content)
        
        setContent {
            HackerFeedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LegalInfoScreen(
                        titleResId = titleResId,
                        contentResId = contentResId
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalInfoScreen(
    @StringRes titleResId: Int,
    @StringRes contentResId: Int
) {
    val localContext = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ThemedTopAppBar(
                title = {
                    Text(text = stringResource(id = titleResId))
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            LegalContentText(
                content = stringResource(id = contentResId)
            )
        }
    }
}

@Composable
fun LegalContentText(content: String) {
    val lines = content.replace("\\n", "\n").trim().split("\n")
    
    for (line in lines) {
        val trimmedLine = line.trim()
        
        when {
            trimmedLine.isEmpty() -> {
                // Add spacing between sections
                Text(
                    text = "",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            trimmedLine.matches(Regex("^\\d+\\..*")) -> {
                // Section headers (1. 2. 3. etc.)
                Text(
                    text = trimmedLine,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            trimmedLine.startsWith("•") -> {
                // Bullet points
                Text(
                    text = trimmedLine,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            trimmedLine.contains("Last updated:") || 
            trimmedLine.endsWith("Privacy Policy") ||
            trimmedLine.endsWith("Terms and Conditions") ||
            trimmedLine.endsWith("Data Protection Information") ||
            trimmedLine.endsWith("Your Rights Under GDPR") -> {
                // Main title or subtitle
                Text(
                    text = trimmedLine,
                    style = if (trimmedLine.contains("Last updated:")) 
                        MaterialTheme.typography.bodyMedium 
                    else MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            else -> {
                // Regular paragraph text
                Text(
                    text = trimmedLine,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LegalInfoScreenPreview() {
    HackerFeedTheme {
        LegalInfoScreen(
            titleResId = R.string.data_protection_title,
            contentResId = R.string.data_protection_content
        )
    }
}
