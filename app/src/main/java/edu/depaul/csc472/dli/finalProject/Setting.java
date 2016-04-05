package edu.depaul.csc472.dli.finalProject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DLI on 11/22/15.
 */
class Setting implements Parcelable {
    double intervalWeight;
    int interTime;

    protected Setting(Parcel in) {
        intervalWeight = in.readDouble();
        interTime = in.readInt();
    }

    public Setting(double weight, int time){
        intervalWeight = weight;
        interTime =time;
    }

    public double getIntervalWeight() {
        return intervalWeight;
    }

    public int getInterTime() {
        return interTime;
    }

    public static final Creator<Setting> CREATOR = new Creator<Setting>() {
        @Override
        public Setting createFromParcel(Parcel in) {
            return new Setting(in);
        }

        @Override
        public Setting[] newArray(int size) {
            return new Setting[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(intervalWeight);
        dest.writeInt(interTime);
    }
}
