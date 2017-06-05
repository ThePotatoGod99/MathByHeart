package com.games.potato.mathbyheart.math;


import android.util.Log;

/**
 * Created by Simon Tran on 2017-04-13.
 */

public class Xd {
    /* This class should be deleted at release. */

    //TODO: Remove this.
    public static void print(String string){
        if(string != null) {
            Log.d("LOGGING_HOLA", string);
        }
        else{
            print("null");
        }
    }

    public static void error(String string){
        if(string != null) {
            Log.e("ERROR_HOLA", string);
        }
        else{
            print("null");
        }
    }
    
}
