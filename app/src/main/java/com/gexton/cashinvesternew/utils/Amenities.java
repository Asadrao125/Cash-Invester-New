package com.gexton.cashinvesternew.utils;

import com.gexton.cashinvesternew.R;

public class Amenities {
    //cash investor app
    public static String[] allAminities = {
            "Air Conditioning",
            "Garage",
            "Swimming Pool",
            "Jacuzzi",
            "Dryer",
            "Washer",
            "Gym",
            "Refrigerator"
    };

    public static int[] allAminitiesIcons = {
            R.drawable.cooling_amenties_grey,
            R.drawable.garage_amenties_grey,
            R.drawable.pool_amenties_grey,
            R.drawable.jacuzzi_amenties_grey,
            R.drawable.dryer_amenties_grey,
            R.drawable.washer_amenties_grey,
            R.drawable.gym_amenties_grey,
            R.drawable.refrigerator_amenties_green
    };

    public static int getAmmeityDrawableRes(String amenityTitle) {
        int resID = R.drawable.heating_amenties_grey;

        if (amenityTitle.equalsIgnoreCase("Air Conditioning")) {
            resID = R.drawable.cooling_amenties_grey;
        } else if (amenityTitle.equalsIgnoreCase("Garage")) {
            resID = R.drawable.garage_amenties_grey;
        } else if (amenityTitle.equalsIgnoreCase("Swimming Pool")) {
            resID = R.drawable.pool_amenties_grey;
        } else if (amenityTitle.equalsIgnoreCase("Jacuzzi")) {
            resID = R.drawable.jacuzzi_amenties_grey;
        } else if (amenityTitle.equalsIgnoreCase("Dryer")) {
            resID = R.drawable.dryer_amenties_grey;
        } else if (amenityTitle.equalsIgnoreCase("Washer")) {
            resID = R.drawable.washer_amenties_grey;
        } else if (amenityTitle.equalsIgnoreCase("Gym")) {
            resID = R.drawable.gym_amenties_grey;
        } else if (amenityTitle.equalsIgnoreCase("Refrigerator")) {
            resID = R.drawable.rerfigerator_amenties_grey;
        }
        return resID;
    }
}