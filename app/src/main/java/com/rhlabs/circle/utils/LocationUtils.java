package com.rhlabs.circle.utils;

import com.rhlabs.protobufs.services.organization.containers.LocationV1;

/**
 * Created by anju on 6/10/15.
 */
public class LocationUtils {

    public static String shortOfficeAddress(LocationV1 locationV1) {
        StringBuilder addressStringBuilder = new StringBuilder();
        addressStringBuilder.append(locationV1.address_1);

        if (locationV1.address_2 != null && !locationV1.address_2.isEmpty()) {
            addressStringBuilder.append(", ");
            addressStringBuilder.append(locationV1.address_2);
        }

        return addressStringBuilder.toString().trim();
    }

    public static String cityRegionPostalCode(LocationV1 locationV1, boolean includeCountryCode) {
        StringBuilder addressStringBuilder = new StringBuilder();
        addressStringBuilder.append(locationV1.city);

        if (locationV1.region != null && !locationV1.region.isEmpty()) {
            addressStringBuilder.append(", ");
            addressStringBuilder.append(locationV1.region);
        }

        if (includeCountryCode) {
            addressStringBuilder.append(", ");
            addressStringBuilder.append(locationV1.country_code);
        }

        if (locationV1.postal_code != null && !locationV1.postal_code.isEmpty()) {
            addressStringBuilder.append(" ");
            addressStringBuilder.append(locationV1.postal_code);
        }

        return addressStringBuilder.toString().trim();
    }

    public static String fullAddress(LocationV1 locationV1) {
        return LocationUtils.shortOfficeAddress(locationV1) + ",\n" + LocationUtils.cityRegionPostalCode(locationV1, true);
    }

    public static String officeName(LocationV1 locationV1) {
        String officeName = locationV1.name;
        if (officeName.isEmpty()) {
            officeName = locationV1.city + ", " + locationV1.region;
        }

        return officeName;
    }
}
