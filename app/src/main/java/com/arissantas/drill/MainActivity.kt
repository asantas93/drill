package com.arissantas.drill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.arissantas.drill.ui.MainPage
import com.arissantas.drill.ui.theme.DrillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm = ViewModelProvider(this)[DrillViewModel::class.java]
        setContent {
            DrillTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPage(
                        todo = vm.todo.value,
                        done = vm.done.value,
                        updateDrill = vm::updateDrill,
                        deleteDrill = vm::deleteDrill,
                        moveTodo = vm::moveTodo,
                        moveDone = vm::moveDone,
                        completeDrill = vm::completeDrill,
                        uncompleteDrill = vm::uncompleteDrill,
                        newDrill = vm::newDrill,
                        setDay = vm::changeDay,
                        day = vm.day.value
                    )
                }
            }
        }
    }
}
