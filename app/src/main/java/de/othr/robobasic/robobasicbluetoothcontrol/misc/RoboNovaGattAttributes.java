package de.othr.robobasic.robobasicbluetoothcontrol.misc;

import java.util.HashMap;

/**
 * This abstract class defines methods to find String descriptions for GATT Services and Characteristics
 */
public abstract class RoboNovaGattAttributes {

    private static final HashMap<String,String> services = new HashMap<>();
    private static final HashMap<String,String> characteristics = new HashMap<>();

    static {
        addData();
    }

    private static void addData() {
        characteristics.put("53530000","RoboBASIC Custom Characteristic");
        services.put("53530000","RoboBASIC Escort Service");

        characteristics.put("00002A7E","Aerobic Heart Rate Lower Limit");
        characteristics.put("00002A84","Aerobic Heart Rate Upper Limit");
        characteristics.put("00002A7F","Aerobic Threshold");
        characteristics.put("00002A80","Age");
        characteristics.put("00002A5A","Aggregate");
        characteristics.put("00002A43","Alert Category ID");
        characteristics.put("00002A42","Alert Category ID Bit Mask");
        characteristics.put("00002A06","Alert Level");
        characteristics.put("00002A44","Alert Notification Control Point");
        characteristics.put("00002A3F","Alert Status");
        characteristics.put("00002AB3","Altitude");
        characteristics.put("00002A81","Anaerobic Heart Rate Lower Limit");
        characteristics.put("00002A82","Anaerobic Heart Rate Upper Limit");
        characteristics.put("00002A83","Anaerobic Threshold");
        characteristics.put("00002A58","Analog");
        characteristics.put("00002A59","Analog Output");
        characteristics.put("00002A73","Apparent Wind Direction");
        characteristics.put("00002A72","Apparent Wind Speed");
        characteristics.put("00002A01","Appearance");
        characteristics.put("00002AA3","Barometric Pressure Trend");
        characteristics.put("00002A19","Battery Level");
        characteristics.put("00002A1B","Battery Level State");
        characteristics.put("00002A1A","Battery Power State");
        characteristics.put("00002A49","Blood Pressure Feature");
        characteristics.put("00002A35","Blood Pressure Measurement");
        characteristics.put("00002A9B","Body Composition Feature");
        characteristics.put("00002A9C","Body Composition Measurement");
        characteristics.put("00002A38","Body Sensor Location");
        characteristics.put("00002AA4","Bond Management Control Point");
        characteristics.put("00002AA5","Bond Management Features");
        characteristics.put("00002A22","Boot Keyboard Input Report");
        characteristics.put("00002A32","Boot Keyboard Output Report");
        characteristics.put("00002A33","Boot Mouse Input Report");
        characteristics.put("00002AA8","CGM Feature");
        characteristics.put("00002AA7","CGM Measurement");
        characteristics.put("00002AAB","CGM Session Run Time");
        characteristics.put("00002AAA","CGM Session Start Time");
        characteristics.put("00002AAC","CGM Specific Ops Control Point");
        characteristics.put("00002AA9","CGM Status");
        characteristics.put("00002ACE","Cross Trainer Data");
        characteristics.put("00002A5C","CSC Feature");
        characteristics.put("00002A5B","CSC Measurement");
        characteristics.put("00002A2B","Current Time");
        characteristics.put("00002A66","Cycling Power Control Point");
        characteristics.put("00002A65","Cycling Power Feature");
        characteristics.put("00002A63","Cycling Power Measurement");
        characteristics.put("00002A64","Cycling Power Vector");
        characteristics.put("00002A99","Database Change Increment");
        characteristics.put("00002A85","Date of Birth");
        characteristics.put("00002A86","Date of Threshold Assessment");
        characteristics.put("00002A08","Date Time");
        characteristics.put("00002AED","Date UTC");
        characteristics.put("00002A0A","Day Date Time");
        characteristics.put("00002A09","Day of Week");
        characteristics.put("00002A7D","Descriptor Value Changed");
        characteristics.put("00002A7B","Dew Point");
        characteristics.put("00002A56","Digital");
        characteristics.put("00002A57","Digital Output");
        characteristics.put("00002A0D","DST Offset");
        characteristics.put("00002A6C","Elevation");
        characteristics.put("00002A87","Email Address");
        characteristics.put("00002A0B","Exact Time 100");
        characteristics.put("00002A0C","Exact Time 256");
        characteristics.put("00002A88","Fat Burn Heart Rate Lower Limit");
        characteristics.put("00002A89","Fat Burn Heart Rate Upper Limit");
        characteristics.put("00002A26","Firmware Revision String");
        characteristics.put("00002A8A","First Name");
        characteristics.put("00002AD9","Fitness Machine Control Point");
        characteristics.put("00002ACC","Fitness Machine Feature");
        characteristics.put("00002ADA","Fitness Machine Status");
        characteristics.put("00002A8B","Five Zone Heart Rate Limits");
        characteristics.put("00002AB2","Floor Number");
        characteristics.put("00002AA6","Central Address Resolution");
        characteristics.put("00002A00","Device Name");
        characteristics.put("00002A04","Peripheral Preferred Connection Parameters");
        characteristics.put("00002A02","Peripheral Privacy Flag");
        characteristics.put("00002A03","Reconnection Address");
        characteristics.put("00002A05","Service Changed");
        characteristics.put("00002A8C","Gender");
        characteristics.put("00002A51","Glucose Feature");
        characteristics.put("00002A18","Glucose Measurement");
        characteristics.put("00002A34","Glucose Measurement Context");
        characteristics.put("00002A74","Gust Factor");
        characteristics.put("00002A27","Hardware Revision String");
        characteristics.put("00002A39","Heart Rate Control Point");
        characteristics.put("00002A8D","Heart Rate Max");
        characteristics.put("00002A37","Heart Rate Measurement");
        characteristics.put("00002A7A","Heat Index");
        characteristics.put("00002A8E","Height");
        characteristics.put("00002A4C","HID Control Point");
        characteristics.put("00002A4A","HID Information");
        characteristics.put("00002A8F","Hip Circumference");
        characteristics.put("00002ABA","HTTP Control Point");
        characteristics.put("00002AB9","HTTP Entity Body");
        characteristics.put("00002AB7","HTTP Headers");
        characteristics.put("00002AB8","HTTP Status Code");
        characteristics.put("00002ABB","HTTPS Security");
        characteristics.put("00002A6F","Humidity");
        characteristics.put("00002B22","IDD Annunciation Status");
        characteristics.put("00002B25","IDD Command Control Point");
        characteristics.put("00002B26","IDD Command Data");
        characteristics.put("00002B23","IDD Features");
        characteristics.put("00002B28","IDD History Data");
        characteristics.put("00002B27","IDD Record Access Control Point");
        characteristics.put("00002B21","IDD Status");
        characteristics.put("00002B20","IDD Status Changed");
        characteristics.put("00002B24","IDD Status Reader Control Point");
        characteristics.put("00002A2A","IEEE 11073-20601 Regulatory Certification Data List");
        characteristics.put("00002AD2","Indoor Bike Data");
        characteristics.put("00002AAD","Indoor Positioning Configuration");
        characteristics.put("00002A36","Intermediate Cuff Pressure");
        characteristics.put("00002A1E","Intermediate Temperature");
        characteristics.put("00002A77","Irradiance");
        characteristics.put("00002AA2","Language");
        characteristics.put("00002A90","Last Name");
        characteristics.put("00002AAE","Latitude");
        characteristics.put("00002A6B","LN Control Point");
        characteristics.put("00002A6A","LN Feature");
        characteristics.put("00002AB1","Local East Coordinate");
        characteristics.put("00002AB0","Local North Coordinate");
        characteristics.put("00002A0F","Local Time Information");
        characteristics.put("00002A67","Location and Speed Characteristic");
        characteristics.put("00002AB5","Location Name");
        characteristics.put("00002AAF","Longitude");
        characteristics.put("00002A2C","Magnetic Declination");
        characteristics.put("00002AA0","Magnetic Flux Density – 2D");
        characteristics.put("00002AA1","Magnetic Flux Density – 3D");
        characteristics.put("00002A29","Manufacturer Name String");
        characteristics.put("00002A91","Maximum Recommended Heart Rate");
        characteristics.put("00002A21","Measurement Interval");
        characteristics.put("00002A24","Model Number String");
        characteristics.put("00002A68","Navigation");
        characteristics.put("00002A3E","Network Availability");
        characteristics.put("00002A46","New Alert");
        characteristics.put("00002AC5","Object Action Control Point");
        characteristics.put("00002AC8","Object Changed");
        characteristics.put("00002AC1","Object First-Created");
        characteristics.put("00002AC3","Object ID");
        characteristics.put("00002AC2","Object Last-Modified");
        characteristics.put("00002AC6","Object List Control Point");
        characteristics.put("00002AC7","Object List Filter");
        characteristics.put("00002ABE","Object Name");
        characteristics.put("00002AC4","Object Properties");
        characteristics.put("00002AC0","Object Size");
        characteristics.put("00002ABF","Object Type");
        characteristics.put("00002ABD","OTS Feature");
        characteristics.put("00002A5F","PLX Continuous Measurement Characteristic");
        characteristics.put("00002A60","PLX Features");
        characteristics.put("00002A5E","PLX Spot-Check Measurement");
        characteristics.put("00002A50","PnP ID");
        characteristics.put("00002A75","Pollen Concentration");
        characteristics.put("00002A2F","Position 2D");
        characteristics.put("00002A30","Position 3D");
        characteristics.put("00002A69","Position Quality");
        characteristics.put("00002A6D","Pressure");
        characteristics.put("00002A4E","Protocol Mode");
        characteristics.put("00002A62","Pulse Oximetry Control Point");
        characteristics.put("00002A78","Rainfall");
        characteristics.put("00002B1D","RC Feature");
        characteristics.put("00002B1E","RC Settings");
        characteristics.put("00002B1F","Reconnection Configuration Control Point");
        characteristics.put("00002A52","Record Access Control Point");
        characteristics.put("00002A14","Reference Time Information");
        characteristics.put("00002A3A","Removable");
        characteristics.put("00002A4D","Report");
        characteristics.put("00002A4B","Report Map");
        characteristics.put("00002AC9","Resolvable Private Address Only");
        characteristics.put("00002A92","Resting Heart Rate");
        characteristics.put("00002A40","Ringer Control point");
        characteristics.put("00002A41","Ringer Setting");
        characteristics.put("00002AD1","Rower Data");
        characteristics.put("00002A54","RSC Feature");
        characteristics.put("00002A53","RSC Measurement");
        characteristics.put("00002A55","SC Control Point");
        characteristics.put("00002A4F","Scan Interval Window");
        characteristics.put("00002A31","Scan Refresh");
        characteristics.put("00002A3C","Scientific Temperature Celsius");
        characteristics.put("00002A10","Secondary Time Zone");
        characteristics.put("00002A5D","Sensor Location");
        characteristics.put("00002A25","Serial Number String");
        characteristics.put("00002A3B","Service Required");
        characteristics.put("00002A28","Software Revision String");
        characteristics.put("00002A93","Sport Type for Aerobic and Anaerobic Thresholds");
        characteristics.put("00002AD0","Stair Climber Data");
        characteristics.put("00002ACF","Step Climber Data");
        characteristics.put("00002A3D","String");
        characteristics.put("00002AD7","Supported Heart Rate Range");
        characteristics.put("00002AD5","Supported Inclination Range");
        characteristics.put("00002A47","Supported New Alert Category");
        characteristics.put("00002AD8","Supported Power Range");
        characteristics.put("00002AD6","Supported Resistance Level Range");
        characteristics.put("00002AD4","Supported Speed Range");
        characteristics.put("00002A48","Supported Unread Alert Category");
        characteristics.put("00002A23","System ID");
        characteristics.put("00002ABC","TDS Control Point");
        characteristics.put("00002A6E","Temperature");
        characteristics.put("00002A1F","Temperature Celsius");
        characteristics.put("00002A20","Temperature Fahrenheit");
        characteristics.put("00002A1C","Temperature Measurement");
        characteristics.put("00002A1D","Temperature Type");
        characteristics.put("00002A94","Three Zone Heart Rate Limits");
        characteristics.put("00002A12","Time Accuracy");
        characteristics.put("00002A15","Time Broadcast");
        characteristics.put("00002A13","Time Source");
        characteristics.put("00002A16","Time Update Control Point");
        characteristics.put("00002A17","Time Update State");
        characteristics.put("00002A11","Time with DST");
        characteristics.put("00002A0E","Time Zone");
        characteristics.put("00002AD3","Training Status");
        characteristics.put("00002ACD","Treadmill Data");
        characteristics.put("00002A71","True Wind Direction");
        characteristics.put("00002A70","True Wind Speed");
        characteristics.put("00002A95","Two Zone Heart Rate Limit");
        characteristics.put("00002A07","Tx Power Level");
        characteristics.put("00002AB4","Uncertainty");
        characteristics.put("00002A45","Unread Alert Status");
        characteristics.put("00002AB6","URI");
        characteristics.put("00002A9F","User Control Point");
        characteristics.put("00002A9A","User Index");
        characteristics.put("00002A76","UV Index");
        characteristics.put("00002A96","VO2 Max");
        characteristics.put("00002A97","Waist Circumference");
        characteristics.put("00002A98","Weight");
        characteristics.put("00002A9D","Weight Measurement");
        characteristics.put("00002A9E","Weight Scale Feature");
        characteristics.put("00002A79","Wind Chill");
        services.put("00001800","Generic Access");
        services.put("00001811","Alert Notification Service");
        services.put("00001815","Automation IO");
        services.put("0000180F","Battery Service");
        services.put("00001810","Blood Pressure");
        services.put("0000181B","Body Composition");
        services.put("0000181E","Bond Management Service");
        services.put("0000181F","Continuous Glucose Monitoring");
        services.put("00001805","Current Time Service");
        services.put("00001818","Cycling Power");
        services.put("00001816","Cycling Speed and Cadence");
        services.put("0000180A","Device Information");
        services.put("0000181A","Environmental Sensing");
        services.put("00001826","Fitness Machine");
        services.put("00001801","Generic Attribute");
        services.put("00001808","Glucose");
        services.put("00001809","Health Thermometer");
        services.put("0000180D","Heart Rate");
        services.put("00001823","HTTP Proxy");
        services.put("00001812","Human Interface Device");
        services.put("00001802","Immediate Alert");
        services.put("00001821","Indoor Positioning");
        services.put("0000183A","Insulin Delivery");
        services.put("00001820","Internet Protocol Support Service");
        services.put("00001803","Link Loss");
        services.put("00001819","Location and Navigation");
        services.put("00001827","Mesh Provisioning Service");
        services.put("00001828","Mesh Proxy Service");
        services.put("00001807","Next DST Change Service");
        services.put("00001825","Object Transfer Service");
        services.put("0000180E","Phone Alert Status Service");
        services.put("00001822","Pulse Oximeter Service");
        services.put("00001829","Reconnection Configuration");
        services.put("00001806","Reference Time Update Service");
        services.put("00001814","Running Speed and Cadence");
        services.put("00001813","Scan Parameters");
        services.put("00001824","Transport Discovery");
        services.put("00001804","Tx Power");
        services.put("0000181C","User Data");
        services.put("0000181D","Weight Scale");

    }


    /**
     * Lookup characteristics string.
     *
     * @param uuid        the Characteristics UUID
     * @param defaultName the default name to be returned if no matching Description was found
     * @return the string description
     */
    public static String lookupCharacteristics(String uuid, String defaultName){
        String uuidCharNumber = uuid.substring(0, 8).toUpperCase();
        String name = characteristics.get(uuidCharNumber);
        return name == null ? defaultName : name;
    }

    /**
     * Lookup service string.
     *
     * @param uuid        the Service UUID
     * @param defaultName the default name to be returned if no matching Description was found
     * @return the string description
     */
    public static String lookupService(String uuid, String defaultName){
        String uuidServiceNumber = uuid.substring(0, 8).toUpperCase();
        String name = services.get(uuidServiceNumber);
        return name == null ? defaultName : name;
    }

}