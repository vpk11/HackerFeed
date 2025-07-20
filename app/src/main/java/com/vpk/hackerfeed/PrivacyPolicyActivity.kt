package com.vpk.hackerfeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.vpk.hackerfeed.components.ThemedTopAppBar
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme

class PrivacyPolicyActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HackerFeedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PrivacyPolicyScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen() {
    val localContext = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ThemedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.privacy_policy_title))
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
        val htmlContent = stringResource(id = R.string.privacy_policy_content)
        val parsedText = HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()

        Text(
            text = parsedText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyScreenPreview() {
    HackerFeedTheme {
        PrivacyPolicyScreen()
    }
}
