package cz.michalbelohoubek.interviewshowcase

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.auth.FirebaseAuth
import cz.michalbelohoubek.interviewshowcase.app.feature_sign_in.UserSettingsViewModel
import cz.michalbelohoubek.interviewshowcase.common.analytics.AnalyticsUtil
import cz.michalbelohoubek.interviewshowcase.core.model.UiMode.Companion.isNotSet
import cz.michalbelohoubek.interviewshowcase.ui.InterviewShowcaseApp
import cz.michalbelohoubek.interviewshowcase.updates.InAppUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var inAppUpdate: InAppUpdate

    private val viewModel: UserSettingsViewModel by viewModels<UserSettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiMode.collect { uiMode ->
                    splashScreen.setKeepOnScreenCondition { uiMode.isNotSet }
                    setContent {
                        InterviewShowcaseApp(
                            onExitApp = {
                                finish()
                            },
                            uiMode = uiMode,
                            onUiModeChanged = viewModel::setUiMode
                        )
                    }
                }
            }
        }
        super.onCreate(savedInstanceState)
        inAppUpdate = InAppUpdate(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inAppUpdate.onActivityResult(requestCode,resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        inAppUpdate.onResume()
        FirebaseAuth.getInstance().currentUser?.let {
            if (it.isAnonymous.not()) {
                AnalyticsUtil.login(it.uid, it.email)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdate.onDestroy()
    }
}
