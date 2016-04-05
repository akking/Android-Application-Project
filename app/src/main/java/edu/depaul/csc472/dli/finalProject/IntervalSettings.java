package edu.depaul.csc472.dli.finalProject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.util.LinkedList;

public class IntervalSettings implements Parcelable {

    private LinkedList<Setting> settings = new LinkedList<>();



    protected IntervalSettings(Parcel in) {
        in.readTypedList(settings,Setting.CREATOR);
    }


    public static final Creator<IntervalSettings> CREATOR = new Creator<IntervalSettings>() {
        @Override
        public IntervalSettings createFromParcel(Parcel in) {
            return new IntervalSettings(in);
        }

        @Override
        public IntervalSettings[] newArray(int size) {
            return new IntervalSettings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(settings);
    }
}



