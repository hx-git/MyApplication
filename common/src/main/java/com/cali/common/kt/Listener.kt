package com.cali.common.kt

import android.text.Editable
import android.text.TextWatcher


class MyTextChangeListener(var block:(s: Editable?)->Unit,
                           var block2: () -> Unit,
                           var block3: () -> Unit):TextWatcher{

    override fun afterTextChanged(s: Editable?) {
        block(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}