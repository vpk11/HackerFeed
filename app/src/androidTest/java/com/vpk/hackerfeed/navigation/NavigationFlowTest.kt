package com.vpk.hackerfeed.navigation

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.vpk.hackerfeed.AboutActivity
import com.vpk.hackerfeed.CacheManagementActivity
import com.vpk.hackerfeed.FavouritesActivity
import com.vpk.hackerfeed.LegalInfoActivity
import com.vpk.hackerfeed.SettingsActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

class NavigationFlowTest {

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun settingsActivity_launches() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), SettingsActivity::class.java)
        ActivityScenario.launch<SettingsActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assert(!activity.isFinishing)
            }
        }
    }

    @Test
    fun favouritesActivity_launches() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), FavouritesActivity::class.java)
        ActivityScenario.launch<FavouritesActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assert(!activity.isFinishing)
            }
        }
    }

    @Test
    fun cacheManagementActivity_launches() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), CacheManagementActivity::class.java)
        ActivityScenario.launch<CacheManagementActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assert(!activity.isFinishing)
            }
        }
    }

    @Test
    fun aboutActivity_launches() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), AboutActivity::class.java)
        ActivityScenario.launch<AboutActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assert(!activity.isFinishing)
            }
        }
    }

    @Test
    fun legalInfoActivity_privacyPolicy_launches() {
        val intent = LegalInfoActivity.createPrivacyPolicyIntent(ApplicationProvider.getApplicationContext())
        ActivityScenario.launch<LegalInfoActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assert(!activity.isFinishing)
            }
        }
    }

    @Test
    fun legalInfoActivity_termsConditions_launches() {
        val intent = LegalInfoActivity.createTermsConditionsIntent(ApplicationProvider.getApplicationContext())
        ActivityScenario.launch<LegalInfoActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assert(!activity.isFinishing)
            }
        }
    }

    @Test
    fun legalInfoActivity_dataProtection_launches() {
        val intent = LegalInfoActivity.createDataProtectionIntent(ApplicationProvider.getApplicationContext())
        ActivityScenario.launch<LegalInfoActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assert(!activity.isFinishing)
            }
        }
    }
}
