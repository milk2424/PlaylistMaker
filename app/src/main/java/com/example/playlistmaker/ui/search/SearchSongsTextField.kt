package com.example.playlistmaker.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.theme.Black

@Composable
fun SearchSongsTextField(
    text: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onDoneClick: () -> Unit,
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
        value = text,
        onValueChange = { newText: String ->
            onValueChange(newText)
        },
        textStyle = androidx.compose.ui.text.TextStyle(
            color = Black,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontSize = 16.sp,
            fontWeight = FontWeight(400)
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDoneClick()
            }
        ),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = text,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
                        color = MaterialTheme.colorScheme.surface,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular))
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                leadingIcon = {
                    Image(
                        painterResource(R.drawable.search_edit_text_icon),
                        contentDescription = stringResource(R.string.search),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                    )
                },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        Image(
                            painterResource(R.drawable.btn_clear_edit_text),
                            contentDescription = stringResource(R.string.search),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface),
                            modifier = Modifier.clickable {
                                onClearClick()
                            }
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    errorContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedTextColor = MaterialTheme.colorScheme.surface,
                    unfocusedTextColor = MaterialTheme.colorScheme.surface,
                    disabledTextColor = MaterialTheme.colorScheme.surface,
                    errorTextColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            )
        },

        )
}