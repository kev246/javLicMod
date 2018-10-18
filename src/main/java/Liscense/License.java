package Liscense;

import java.util.ResourceBundle;

public class License {

        final static ResourceBundle bundle = java.util.ResourceBundle.getBundle("licenseInfo");

        public final static String getId() {
            return bundle.getString("license.id");
        }

        public final static String getEmail() {
            return bundle.getString("license.email");
        }

        public final static String getCompany() {
            return bundle.getString("license.company");
        }
}
