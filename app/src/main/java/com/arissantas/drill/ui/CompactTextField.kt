package com.arissantas.drill.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    isError: Boolean = false,
    suffix: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
  val interactionSource = remember { MutableInteractionSource() }
  BasicTextField(
      value = value,
      onValueChange = onValueChange,
      textStyle =
          MaterialTheme.typography.bodyLarge.copy(
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = textAlign,
          ),
      modifier = modifier,
      visualTransformation = visualTransformation,
      cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
  ) { innerTextField ->
    TextFieldDefaults.DecorationBox(
        interactionSource = interactionSource,
        innerTextField = innerTextField,
        value = value.text,
        enabled = true,
        contentPadding = PaddingValues(horizontal = 4.dp),
        singleLine = false,
        suffix = suffix,
        placeholder = placeholder,
        container = {
          TextFieldDefaults.Container(
              enabled = true,
              interactionSource = interactionSource,
              isError = isError,
              colors =
                  TextFieldDefaults.colors(
                      focusedContainerColor = MaterialTheme.colorScheme.surface,
                      unfocusedIndicatorColor = Color.Transparent,
                      unfocusedContainerColor = Color.Transparent,
                      disabledContainerColor = MaterialTheme.colorScheme.surface,
                  ),
          )
        },
        visualTransformation = VisualTransformation.None,
    )
  }
}
