package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

/**
 * @author Bayramov Nizhad
 */
public class Utils {
    public static String escape(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\'') {
                result.append("\\''");
            } else {
                result.append(s.charAt(i));
            }
        }
        return result.toString();
    }
}
