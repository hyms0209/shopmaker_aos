package com.android.shopmaker.presentation.main

import androidx.lifecycle.ViewModel

interface  MainViewModelType {
    var input: MainViewModelInput
    var output: MainViewModelOutput
}

interface  MainViewModelInput {

}

interface  MainViewModelOutput {

}

class MainViewModel:ViewModel(),
    MainViewModelType,
    MainViewModelInput,
    MainViewModelOutput {
    override var input: MainViewModelInput = this
    override var output: MainViewModelOutput = this
}