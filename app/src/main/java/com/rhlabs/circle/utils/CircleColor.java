package com.rhlabs.circle.utils;

import android.graphics.Color;

import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by anju on 6/2/15.
 */
public class CircleColor {

    enum PaletteType {
        Profile,
        Team
    }

    public static Map<String, String> GroupColorsHolder = new HashMap<String, String>();
    public static Map<String, String> ProfileColorsHolder = new HashMap<String, String>();
    public static Map<String, String> LocationColorsHolder = new HashMap<String, String>();
    public static Map<String, String> TeamColorsHolder = new HashMap<String, String>();


    static class UIColor {
        int redColor;
        int greenColor;
        int blueColor;

        public UIColor(int redColor, int greenColor, int blueColor) {
            this.redColor = redColor;
            this.greenColor = greenColor;
            this.blueColor = blueColor;
        }
    }

    private static String getRandomColor(PaletteType paletteType) {
        //flatUIColors.com
        UIColor[] uiColors = getPalette(paletteType);
        Random random = new Random();
        int randomIndex = random.nextInt(uiColors.length);
        UIColor selectedRandomColor = uiColors[randomIndex];
        return toHex(selectedRandomColor.redColor, selectedRandomColor.greenColor, selectedRandomColor.blueColor);
    }

    private static UIColor[] getPalette(PaletteType paletteType) {
        switch (paletteType) {
            case Team:
                return new UIColor[]{
                        new UIColor(26, 188, 156),
                        new UIColor(26, 188, 156),
                        new UIColor(46, 204, 113),
                        new UIColor(52, 152, 219),
                        new UIColor(155, 89, 182),
                        new UIColor(52, 73, 94),
                        new UIColor(22, 160, 133),
                        new UIColor(39, 174, 96),
                        new UIColor(41, 128, 185),
                        new UIColor(142, 68, 173),
                        new UIColor(44, 62, 80),
                        new UIColor(241, 196, 15),
                        new UIColor(230, 126, 34),
                        new UIColor(231, 76, 60),
                        new UIColor(149, 165, 166),
                        new UIColor(243, 156, 18),
                        new UIColor(211, 84, 0),
                        new UIColor(192, 57, 43),
                        new UIColor(127, 140, 141)
                };

            case Profile:
                return new UIColor[]{
                        new UIColor(30, 146, 57),
                        new UIColor(14, 99, 177),
                        new UIColor(109, 109, 109),
                        new UIColor(213, 102, 19),
                        new UIColor(119, 65, 133),
                        new UIColor(179, 44, 40),
                };
        }

        return new UIColor[]{ new UIColor(222, 222, 222)};
    }

    private static String toHex(int redColor, int greenColor, int blueColor) {
        return String.format("#%02x%02x%02x", redColor, greenColor, blueColor);
    }

    public static int appTeamBackgroundColor(TeamV1 teamV1) {
        final String teamColorKey = teamV1.name.substring(0, 1);
        if (TeamColorsHolder.get(teamColorKey) != null) {
            return Color.parseColor(TeamColorsHolder.get(teamColorKey));
        } else {
            String randomColor = getRandomColor(PaletteType.Team);
            TeamColorsHolder.put(teamColorKey, randomColor);
            return Color.parseColor(randomColor);
        }
    }

    public static int appGroupBackgroundColor(GroupV1 group) {
        final String groupColorKey = group.name.substring(0, 1);
        if (GroupColorsHolder.get(groupColorKey) != null) {
            return Color.parseColor(GroupColorsHolder.get(groupColorKey));
        } else {
            String randomColor = getRandomColor(PaletteType.Team);
            GroupColorsHolder.put(groupColorKey, randomColor);
            return Color.parseColor(randomColor);
        }
    }

    public static int appProfileBackgroundColor(ProfileV1 profileV1) {
        final String profileColorKey = profileV1.first_name.substring(0, 1);
        if (ProfileColorsHolder.get(profileColorKey) != null) {
            return Color.parseColor(ProfileColorsHolder.get(profileColorKey));
        } else {
            String randomColor = getRandomColor(PaletteType.Profile);
            ProfileColorsHolder.put(profileColorKey, randomColor);
            return Color.parseColor(randomColor);
        }
    }

    public static int appLocationBackgroundColor(LocationV1 locationV1) {
        final String locationColorKey = locationV1.name.substring(0, 1);
        if (LocationColorsHolder.get(locationColorKey) != null) {
            return Color.parseColor(LocationColorsHolder.get(locationColorKey));
        } else {
            String randomColor = getRandomColor(PaletteType.Profile);
            LocationColorsHolder.put(locationColorKey, randomColor);
            return Color.parseColor(randomColor);
        }
    }
}
