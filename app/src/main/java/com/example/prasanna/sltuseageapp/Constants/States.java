package com.example.prasanna.sltuseageapp.Constants;

import java.util.ArrayList;

/**
 * Created by prasanna on 6/10/17.
 */

public abstract class States {
    public static String getStatus(int index){
        ArrayList<String> arrStatus = new ArrayList<>();
        arrStatus.add("Status1");

        if(index>=arrStatus.size()){
            return "Index Error [States]";
        }

        if(index<0){
            return "Index Error [States]";
        }

        return arrStatus.get(index);
    }
}
