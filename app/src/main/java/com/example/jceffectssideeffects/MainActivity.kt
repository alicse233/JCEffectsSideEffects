package com.example.jceffectssideeffects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.jceffectssideeffects.ui.theme.JCEffectsSideEffectsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//          for showing snack bar added scaffold and remembered its state
            val scaffoldState = rememberScaffoldState()

//            to safely execute suspend functions with in composable fun
//            you need coroutine scope
            val scope = rememberCoroutineScope()

            Scaffold(scaffoldState = scaffoldState) {
                var state by remember {
                    mutableStateOf(0)
                }

//                in this case as scope is alive state of the snack bar will remain here

                if (state % 5 == 0 && state > 0) {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Hello")
                    }
                }
//

//                Now if we want to cancel snack bar on every recomposition

                if (state % 5 == 0 && state > 0) {
                    LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
                        scaffoldState.snackbarHostState.showSnackbar("Hello")
                    }
                }

                Button(onClick = { state++ }) {
                    Text(text = "Click me $state")
                }
            }
        }
    }
}

var i = 0

@Composable
fun Greeting(backPressedDispatcher: OnBackPressedDispatcher) {

//    For handling things out of scope of composeable fun
//    you need SideEffect and Effect blocks to avoid recalling and memory leaks
//    code in these blocks is recalled on UI updates
//    For the successful compose side effect block code is run

    SideEffect {
        i++
    }


//    If there is some code which requires clean up after the
//    successful composition then you use Disposable effect

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                do some thing
            }
        }
    }

//    so every time we need to free up memory or resources
//    then you need to call disposable effect
//    and in its on dispose block remove the call back

    DisposableEffect(key1 = backPressedDispatcher) {

        backPressedDispatcher.addCallback(callback)
        onDispose {
            backPressedDispatcher.addCallback(callback)
        }
    }

    Button(onClick = {}) {
        Text(text = "Click me")
    }
}