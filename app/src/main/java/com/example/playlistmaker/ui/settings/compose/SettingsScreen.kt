package com.example.playlistmaker.ui.settings.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.view_model.SettingsViewModel
import com.example.playlistmaker.ui.core.ScreenName
import com.example.playlistmaker.ui.theme.Blue
import com.example.playlistmaker.ui.theme.DarkBlue
import com.example.playlistmaker.ui.theme.Gray
import com.example.playlistmaker.ui.theme.LightGray
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {

    val darkState by viewModel.isNightLiveData().observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenName(text = stringResource(R.string.settings), false)
        SettingsItem(stringResource(R.string.dark_theme)) {
            Switch(
                modifier = Modifier
                    .scale(0.75f)
                    .padding(0.dp),
                checked = darkState?.isNight ?: false,
                onCheckedChange = { state ->
                    viewModel.switchTheme(state)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Blue,
                    checkedTrackColor = DarkBlue,
                    uncheckedThumbColor = Gray,
                    uncheckedTrackColor = LightGray,
                )
            )
        }
        SettingsItem(
            text = stringResource(R.string.share_app),
            onClickAction = { viewModel.shareApp() }) {
            Image(
                painter = painterResource(R.drawable.btn_share),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }
        SettingsItem(
            text = stringResource(R.string.support),
            onClickAction = { viewModel.openSupport() }
        ) {
            Image(
                painter = painterResource(R.drawable.btn_support),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }
        SettingsItem(
            text = stringResource(R.string.user_agreement),
            onClickAction = { viewModel.openTerms() }
        ) {
            Image(
                painter = painterResource(R.drawable.btn_arrow_forward),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }
    }
}