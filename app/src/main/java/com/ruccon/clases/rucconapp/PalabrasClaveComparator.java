package com.ruccon.clases.rucconapp;

import android.os.Build;

import java.util.Comparator;

/**
 * Created by Fede on 23/4/2018.
 */

public class PalabrasClaveComparator implements Comparator<String> {
    private String stringGuia;
    public PalabrasClaveComparator(String stringGuia){
        this.stringGuia = stringGuia;

    }
    @Override
    public int compare(String unString, String otroString) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Integer.compare(LevenshteinDistance.computeLevenshteinDistance(unString,stringGuia),
                            LevenshteinDistance.computeLevenshteinDistance(otroString,stringGuia));
        }
        return 0;
    }


    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
