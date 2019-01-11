package com.theapphideaway.memo.Model

class ToDo {
    var Id: Int? = null
    var ListTitle: String? = null
    var IsChecked: Int? = null

    constructor(id:Int?, title:String, isChecked: Int){
        Id = id
        ListTitle = title
        IsChecked = isChecked
    }
}