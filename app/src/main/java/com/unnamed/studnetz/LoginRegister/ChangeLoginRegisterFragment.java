package com.unnamed.studnetz.LoginRegister;

import android.content.Context;
import android.support.v4.app.Fragment;

public interface ChangeLoginRegisterFragment {

    void ChangeFragment(Fragment fragment, boolean addToBackStack);
    Context getContext();

}
