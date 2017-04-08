package ir.parsansoft.app.ihs.center;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Utility {

    //      All WiFi sources neede is here
    //     https://github.com/CyanogenMod/android_frameworks_base/tree/cm-10.1/wifi/java/android/net/wifi
    public static class MyWiFiManager {

        private WifiManager wifiManager;
        String apDefaultSSID = "IHS_Settings";
        String apDefaultPassword = "G10PZVuznrKMDqWKr9a";


        public class wifiStation {
            public String macAddress;
            public String ipAddress;

            public wifiStation(String mac, String ip) {
                macAddress = mac;
                ipAddress = ip;
            }
        }

        void clearArpTable() {
            //            try {
            //                File arpFile = new File("/proc/net/arp");
            //                PrintWriter writer = new PrintWriter(arpFile);
            //                writer.print("");
            //                writer.close();
            //            }
            //            catch (Exception e) {
            //                G.printStackTrace(e);
            //            }
        }

        public MyWiFiManager() {
            wifiManager = (WifiManager) G.context.getSystemService(Context.WIFI_SERVICE);
        }

        public void createWifiAccessPoint() {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
            Method[] wmMethods = wifiManager.getClass().getDeclaredMethods(); //Get all declared methods in WifiManager class
            boolean methodFound = false;

            clearArpTable();

            for (Method method : wmMethods) {
                if (method.getName().equals("setWifiApEnabled")) {
                    methodFound = true;
                    WifiConfiguration netConfig = new WifiConfiguration();
                    netConfig.SSID = apDefaultSSID;
                    netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    netConfig.preSharedKey = apDefaultPassword;
                    try {
                        boolean apstatus = (Boolean) method.invoke(wifiManager, netConfig, true);
                        for (Method isWifiApEnabledmethod : wmMethods) {
                            if (isWifiApEnabledmethod.getName().equals("isWifiApEnabled")) {
                                while (!(Boolean) isWifiApEnabledmethod.invoke(wifiManager)) {
                                }
                                ;
                                for (Method method1 : wmMethods) {
                                    if (method1.getName().equals("getWifiApState")) {
                                        int apstate;
                                        apstate = (Integer) method1.invoke(wifiManager);
                                    }
                                }
                            }
                        }

                        if (apstatus) {
                            System.out.println("SUCCESSdddd");
                        } else {
                            System.out.println("FAILED");
                        }
                    } catch (IllegalArgumentException e) {
                        G.printStackTrace(e);
                    } catch (IllegalAccessException e) {
                        G.printStackTrace(e);
                    } catch (InvocationTargetException e) {
                        G.printStackTrace(e);
                    }
                }
            }
            if (!methodFound) {
                //statusView.setText("Your phone's API does not contain setWifiApEnabled method to configure an access point");
            }
        }


        public ArrayList<wifiStation> getWifiStations() {
            int macCount = 0;
            ArrayList<wifiStation> result = null;
            BufferedReader br = null;
            try {
                //br = new BufferedReader(new FileReader("/data/misc/dhcp/dnsmasq.leases"));
                br = new BufferedReader(new FileReader("/proc/net/arp"));
                //"/proc/net/arp"
                //"/data/misc/dhcp/dnsmasq.leases"
                String line;
                result = new ArrayList<wifiStation>();

                while ((line = br.readLine()) != null) {
                    G.log("Utility-getWifiStations", line);
                    String[] splitted = line.split(" +");
                    if (splitted != null && splitted.length >= 1) {
                        String mac = splitted[3];
                        String ip = splitted[0];
                        //                        G.log("Found : " + ip + "  " + mac);
                        if (mac.matches("..:..:..:..:..:..") && !mac.equals("00:00:00:00:00:00")) {
                            G.log("Utility-getWifiStations", "Found WiFi Station: " + mac + " - " + ip);
                            result.add(new wifiStation(mac, ip));
                            macCount++;
                        }
                    }
                }
            } catch (Exception e) {
                G.printStackTrace(e);
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    G.printStackTrace(e);
                }
            }
            return result;
        }

        public void connectToMainAP() {
            WifiConfiguration wc = new WifiConfiguration();
            connectToAP(G.networkSetting.wiFiSSID, G.networkSetting.wiFiSecurityKey);
        }

        public boolean reconnect(WifiClass wifi, WifiConfiguration conf) {
            wifiManager.addNetwork(conf);
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                wifiManager.removeNetwork(i.networkId);
                if (i.SSID != null && i.SSID.equals("\"" + wifi.ssid + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    boolean result = wifiManager.reconnect();
                    G.log("Wifi Connection Result :" + result);
                    return result;
                }
            }
            G.log("Wifi Connection Faild ....");
            return false;


            //            // setup a wifi configuration
            //            conf.status = WifiConfiguration.Status.ENABLED;
            //            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            //            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            //            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            //            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            //            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            //            // connect to and enable the connection
            //            int netId = wifiManager.addNetwork(conf);
            //            wifiManager.enableNetwork(netId, true);
            //            wifiManager.setWifiEnabled(true);
        }

        public void connectToAP(String ssid, String pass) {
            WifiManager wifiManager = (WifiManager) G.context.getSystemService(Context.WIFI_SERVICE);
            // setup a wifi configuration
            WifiConfiguration wc = new WifiConfiguration();
            wc.SSID = "\"" + ssid + "\"";
            wc.preSharedKey = "\"" + pass + "\"";
            wc.status = WifiConfiguration.Status.ENABLED;
            // wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            //wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            // wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            //wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            // connect to and enable the connection            
            int netId = wifiManager.addNetwork(wc);
            wifiManager.enableNetwork(netId, true);
            wifiManager.setWifiEnabled(true);
            //            
            //            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            //
            //            wifiManager.setWifiEnabled(true);
            //            for (WifiConfiguration i: list) {
            //                if (i.SSID != null && i.SSID.equals("\"" + setting.wiFiSSID + "\"")) {
            //                    wifiManager.disconnect();
            //                    wifiManager.enableNetwork(i.networkId, true);
            //                    wifiManager.reconnect();
            //                    break;
            //                }
            //            }
        }


        public void turnOffWiFi() {
            WifiManager wifiManager = (WifiManager) G.context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
            //            WifiConfiguration wificonfiguration = null;
            //            try {
            //                Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            //                method.invoke(wifiManager, wificonfiguration, false);
            //            }
            //            catch (NoSuchMethodException e) {
            //                G.printStackTrace(e);
            //            }
            //            catch (IllegalArgumentException e) {
            //                G.printStackTrace(e);
            //            }
            //            catch (IllegalAccessException e) {
            //                G.printStackTrace(e);
            //            }
            //            catch (InvocationTargetException e) {
            //                G.printStackTrace(e);
            //            }
        }

        public void setWifiTetheringEnabled(boolean enable) {
            Method[] methods = wifiManager.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("setWifiApEnabled")) {
                    try {
                        method.invoke(wifiManager, null, enable);
                    } catch (Exception ex) {
                    }
                    break;
                }
            }
        }

        public static boolean isWifiTetheringEnabled() {
            WifiManager wifi = (WifiManager) G.context.getSystemService(Context.WIFI_SERVICE);
            try {
                final Method method = wifi.getClass().getDeclaredMethod("isWifiApEnabled");
                method.setAccessible(true); //in the case of visibility change in future APIs
                return (Boolean) method.invoke(wifi);
            } catch (final Throwable ignored) {
            }
            return false;
        }
    }


    public static class GPS {

        static final double _eQuatorialEarthRadius = 6378.1370D;
        static final double _d2r = (Math.PI / 180D);

        public static int getDistanceInM(double lat1, double long1, double lat2, double long2) {
            return (int) (1000D * getDistanceInKM(lat1, long1, lat2, long2));
        }

        public static double getDistanceInKM(double lat1, double long1, double lat2, double long2) {
            double dlong = (long2 - long1) * _d2r;
            double dlat = (lat2 - lat1) * _d2r;
            double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                    * Math.pow(Math.sin(dlong / 2D), 2D);
            double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
            double d = _eQuatorialEarthRadius * c;
            return d;
        }
    }

    public static class IPAddress {
        public static class ipV4Parameters {
            String ipAddress;
            String subnetMask;
            String defaultGateway;
            String dns1;
            String dns2;

            public int getSubnetPrefix() {
                G.log("Subnet :" + subnetMask);
                String[] parts = subnetMask.split("\\.");
                if (parts.length != 4) {
                    G.log("subnet is not in correct format !");
                    return -1;
                }
                String binarySubnet = "";
                int[] intParts = new int[4];
                for (int i = 0; i < 4; i++) {
                    try {
                        intParts[i] = Integer.parseInt(parts[i]);
                        if (intParts[i] > 255 || intParts[i] < 0) {
                            G.log("IP address is not valid - not in correct range !");
                            return -2;
                        }
                        String temp = Integer.toBinaryString(intParts[i]);
                        for (int j = 0; j < 8 - temp.length(); j++)
                            binarySubnet += "0";
                        binarySubnet += temp;
                    } catch (Exception e) {
                        G.log("Subnet address is not valid numbers");
                        return -3;
                    }
                }
                G.log("Subnet binary is : " + binarySubnet);
                if (!binarySubnet.matches("1*0+")) {
                    G.log("Subnet address is not a valid mask");
                    return -4;
                }
                return binarySubnet.indexOf("0");
            }

            public int checkIfParamsAreCorrect() {
                if (ipToInt(ipAddress) < 0) {
                    G.log("IP Address is not correct !");
                    return -1;
                }
                if (ipToInt(subnetMask) < 0) {
                    G.log("Subnet mask is not correct !");
                    return -2;
                }
                if (getSubnetPrefix() < 0) {
                    G.log("Subnet mask is not correct number !");
                    return -2;
                }
                if (ipToInt(defaultGateway) < 0) {
                    G.log("Default Gateway is not correct !");
                    return -3;
                }
                if (ipToInt(dns1) < 0) {
                    G.log("DNS 1 is not correct !");
                    return -4;
                }
                return 1;
            }


        }

        public static String getIPv4Address() {
            return getIPAddress(true);
        }

        public static String getIPv6Address() {
            return getIPAddress(false);
        }

        public static String getWifiMacAddress() {
            return getMACAddress("wlan0");
        }

        public static String getEthernetMacAddress() {
            return getMACAddress("eth0");
        }


        /**
         * Convert byte array to hex string
         *
         * @param bytes
         * @return
         */
        private static String bytesToHex(byte[] bytes) {
            StringBuilder sbuf = new StringBuilder();
            for (int idx = 0; idx < bytes.length; idx++) {
                int intVal = bytes[idx] & 0xff;
                if (intVal < 0x10)
                    sbuf.append("0");
                sbuf.append(Integer.toHexString(intVal).toUpperCase());
            }
            return sbuf.toString();
        }

        /**
         * Get utf8 byte array.
         *
         * @param str
         * @return array of NULL if error was found
         */
        private static byte[] getUTF8Bytes(String str) {
            try {
                return str.getBytes("UTF-8");
            } catch (Exception ex) {
                return null;
            }
        }

        /**
         * Load UTF8withBOM or any ansi text file.
         *
         * @param filename
         * @return
         * @throws IOException
         */
        private static String loadFileAsString(String filename) throws IOException {
            final int BUFLEN = 1024;
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
                byte[] bytes = new byte[BUFLEN];
                boolean isUTF8 = false;
                int read, count = 0;
                while ((read = is.read(bytes)) != -1) {
                    if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                        isUTF8 = true;
                        baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                    } else {
                        baos.write(bytes, 0, read);
                    }
                    count += read;
                }
                return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
            } finally {
                try {
                    is.close();
                } catch (Exception ex) {
                }
            }
        }

        /**
         * Returns MAC address of the given interface name.
         *
         * @param interfaceName eth0, wlan0 or NULL=use first interface
         * @return mac address or empty string
         */
        private static String getMACAddress(String interfaceName) {
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    if (interfaceName != null) {
                        if (!intf.getName().equalsIgnoreCase(interfaceName))
                            continue;
                    }
                    byte[] mac = intf.getHardwareAddress();
                    if (mac == null)
                        return "";
                    StringBuilder buf = new StringBuilder();
                    for (int idx = 0; idx < mac.length; idx++)
                        buf.append(String.format("%02X:", mac[idx]));
                    if (buf.length() > 0)
                        buf.deleteCharAt(buf.length() - 1);
                    return buf.toString();
                }
            } catch (Exception ex) {
            } // for now eat exceptions
            return "";
            /*try {
                // this is so Linux hack
                return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
            } catch (IOException ex) {
                return null;
            }*/
        }

        /**
         * Get IP address from first non-localhost interface
         * <p>
         * ///@param ipv4 true=return ipv4, false=return ipv6
         *
         * @return address or empty string
         */
        private static String getIPAddress(boolean useIPv4) {
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) {
                            String sAddr = addr.getHostAddress().toUpperCase();
                            boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            if (useIPv4) {
                                if (isIPv4)
                                    return sAddr;
                            } else {
                                if (!isIPv4) {
                                    int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                    return delim < 0 ? sAddr : sAddr.substring(0, delim);
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
            } // for now eat exceptions
            return "";
        }

        public static String getSubnetMask() {
            // TODO: No code found that works properly 
            return "255.255.255.0";
        }

        public static long ipToInt(String iNet) {
            if(iNet==null)
                return 0 ;

            String[] parts = iNet.split("\\.");
            if (parts.length != 4) {
                G.log("IP is not in correct format !");
                return -1;
            }
            long ipNumber = 0;
            int[] intParts = new int[4];
            for (int i = 0; i < 4; i++) {
                try {
                    intParts[i] = Integer.parseInt(parts[i]);
                    if (intParts[i] > 255 || intParts[i] < 0) {
                        G.log("IP address is not valid - not in correct range !");
                        return -2;
                    }
                } catch (Exception e) {
                    G.log("IP address is not valid numbers");
                    return -3;
                }
            }
            for (int i = 0; i < 4; i++)
                ipNumber += Math.pow(256, 3 - i) * intParts[i];
            return ipNumber;
        }

        public static String intToIP(long ip) {
            if (ip >= Math.pow(256, 4) || ip < 0) {
                G.log("The Ip number is not in valid range !");
                return "";
            }
            int[] parts = new int[4];
            parts[3] = (int) (ip / Math.pow(256, 3));
            parts[2] = (int) ((ip - parts[3] * Math.pow(256, 3)) / Math.pow(256, 2));
            parts[0] = (int) (ip % 256);
            parts[1] = (int) ((ip - parts[3] * Math.pow(256, 3) - parts[2] * Math.pow(256, 2) - parts[0]) / 256);
            return parts[3] + "." + parts[2] + "." + parts[1] + "." + parts[0];
        }

        public static String intToIPReverse(long ip) {
            if (ip >= Math.pow(256, 4) || ip < 0) {
                G.log("The Ip number is not in valid range !");
                return "";
            }
            int[] parts = new int[4];
            parts[3] = (int) (ip / Math.pow(256, 3));
            parts[2] = (int) ((ip - parts[3] * Math.pow(256, 3)) / Math.pow(256, 2));
            parts[0] = (int) (ip % 256);
            parts[1] = (int) ((ip - parts[3] * Math.pow(256, 3) - parts[2] * Math.pow(256, 2) - parts[0]) / 256);
            return parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
        }

        /**
         * Validates an IPv4 address. Returns true if valid.
         *
         * @param inet4Address the IPv4 address to validate
         * @return true if the argument contains a valid IPv4 address
         */
        public static boolean isValidV4Address(String inet4Address) {
            try {
                if (inet4Address == null || inet4Address.isEmpty()) {
                    return false;
                }
                String[] parts = inet4Address.split("\\.");
                if (parts.length != 4) {
                    return false;
                }

                for (String s : parts) {
                    int i = Integer.parseInt(s);
                    if ((i < 0) || (i > 255)) {
                        return false;
                    }
                }
                if (inet4Address.endsWith(".")) {
                    return false;
                }

                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }

        public static boolean isDhcpEnabled() {
            try {
                return Settings.System.getInt(G.context.getContentResolver(), "wifi_use_static_ip") == 1;
            } catch (SettingNotFoundException e) {
                return false;
            }
        }

        //        public static ipV4Parameters getStaticIpV4Parameters() {
        //            return null;
        //        }
        public static ipV4Parameters getDhcpIpV4Parameters() {
            ipV4Parameters result = new ipV4Parameters();
            try {
                WifiManager wifii = (WifiManager) G.context.getSystemService(Context.WIFI_SERVICE);
                DhcpInfo d = wifii.getDhcpInfo();
                G.log("Utility : ip address is " + d.ipAddress);
                result.ipAddress = IPAddress.intToIPReverse(d.ipAddress);
                G.log("Utility : ip after convert address is " + result.ipAddress);
                result.subnetMask = IPAddress.intToIPReverse(d.netmask);
                result.defaultGateway = IPAddress.intToIPReverse(d.gateway);
                result.dns1 = IPAddress.intToIPReverse(d.dns1);
                result.dns2 = IPAddress.intToIPReverse(d.dns2);
                if (result.ipAddress.equals("0.0.0.0"))
                    return null;
                if (result.subnetMask.equals("0.0.0.0"))
                    return null;
            } catch (Exception e) {
                G.printStackTrace(e);
                return null;
            }
            return result;
        }

        public static boolean setStaticIpV4Address(ipV4Parameters newStaticAddress) {
            WifiConfiguration wifiConf = null;
            WifiManager wifiManager = (WifiManager) G.context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration conf : configuredNetworks) {
                if (conf.networkId == connectionInfo.getNetworkId()) {
                    wifiConf = conf;
                    break;
                }
            }

            try {
                setIpAssignment("STATIC", wifiConf); //or "DHCP" for dynamic setting
                setIpAddress(InetAddress.getByName(newStaticAddress.ipAddress), newStaticAddress.getSubnetPrefix(), wifiConf);
                setGateway(InetAddress.getByName(newStaticAddress.defaultGateway), wifiConf);
                setDNS(InetAddress.getByName(newStaticAddress.dns1), wifiConf);
                wifiManager.updateNetwork(wifiConf); //apply the setting
                wifiManager.saveConfiguration(); //Save it
                return true;
            } catch (Exception e) {
                G.printStackTrace(e);
            }
            return false;
        }
        //        public static boolean setDhcpEnabled() {
        //            
        //            return true;
        //        }

        //--------------------------------------------------------------------------------
        private static void setIpAssignment(String assign, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
            setEnumField(wifiConf, assign, "ipAssignment");
        }

        private static void setIpAddress(InetAddress addr, int prefixLength, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InstantiationException, InvocationTargetException {
            Object linkProperties = getField(wifiConf, "linkProperties");
            if (linkProperties == null)
                return;
            Class laClass = Class.forName("android.net.LinkAddress");
            Constructor laConstructor = laClass.getConstructor(new Class[]{InetAddress.class, int.class});
            Object linkAddress = laConstructor.newInstance(addr, prefixLength);

            ArrayList mLinkAddresses = (ArrayList) getDeclaredField(linkProperties, "mLinkAddresses");
            mLinkAddresses.clear();
            mLinkAddresses.add(linkAddress);
        }

        private static void setGateway(InetAddress gateway, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException {
            Object linkProperties = getField(wifiConf, "linkProperties");
            if (linkProperties == null)
                return;
            Class routeInfoClass = Class.forName("android.net.RouteInfo");
            Constructor routeInfoConstructor = routeInfoClass.getConstructor(new Class[]{InetAddress.class});
            Object routeInfo = routeInfoConstructor.newInstance(gateway);

            ArrayList mRoutes = (ArrayList) getDeclaredField(linkProperties, "mRoutes");
            mRoutes.clear();
            mRoutes.add(routeInfo);
        }

        private static void setDNS(InetAddress dns, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
            Object linkProperties = getField(wifiConf, "linkProperties");
            if (linkProperties == null)
                return;

            ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>) getDeclaredField(linkProperties, "mDnses");
            mDnses.clear(); //or add a new dns address , here I just want to replace DNS1
            mDnses.add(dns);
        }

        private static Object getField(Object obj, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            java.lang.reflect.Field f = obj.getClass().getField(name);
            Object out = f.get(obj);
            return out;
        }

        private static Object getDeclaredField(Object obj, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            java.lang.reflect.Field f = obj.getClass().getDeclaredField(name);
            f.setAccessible(true);
            Object out = f.get(obj);
            return out;
        }

        private static void setEnumField(Object obj, String value, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            java.lang.reflect.Field f = obj.getClass().getField(name);
            f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
        }
    }

    public static String getIdHash(long id) {
        String hash = null;
        long intId = id ^ Long.MAX_VALUE;
        String md5 = String.format("%X-ANY-TEXT", intId);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] arr = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < arr.length; ++i)
                sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));

            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            G.printStackTrace(e);
        }

        return hash.toUpperCase();
    }

    public static String MD5(String planetext) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(planetext.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            G.printStackTrace(e);
        }
        return "";
    }

    public static String readTime(int timeInSeconds) {
        if (timeInSeconds % 3600 == 0)
            return (int) timeInSeconds / 3600 + " " + G.T.getSentence(504);
        else if (timeInSeconds % 60 == 0)
            return (int) timeInSeconds / 60 + " " + G.T.getSentence(503);
        else
            return timeInSeconds + " " + G.T.getSentence(502);
    }

    public static long getSystemMillis() {
        return System.currentTimeMillis();
    }


    public static void showMemoryInfo() {
        MemoryInfo mi = new MemoryInfo();
        ActivityManager activityManager = (ActivityManager) G.context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        G.toast("Nenory : Total= " + mi.totalMem / 1048576 + " Available= " + mi.availMem / 1048576);
    }

    public static Bitmap makeNewQR(String market) {
        QRCodeWriter qrMaker = new QRCodeWriter();
        String qrString = market;
        try {
            int width = 256;
            int height = 256;
            ByteMatrix bitMatrix = qrMaker.encode(qrString, BarcodeFormat.QR_CODE, width, height);
            final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 10; x < width - 10; x++) {
                for (int y = 10; y < height - 10; y++) {
                    if (bitMatrix.get(x, y) == 0)
                        bmp.setPixel(x, y, Color.BLACK);
                    else
                        bmp.setPixel(x, y, Color.WHITE);
                }
            }
            return bmp;
        } catch (WriterException e) {
            G.printStackTrace(e);
        }
        return null;
    }

    public static void changeKeyboardLanguages() {
        int lang = G.setting.languageID;
        String region = "en";
        switch (lang) {
            case 1:
                region = "en";
                break;
            case 2:
                region = "ir";
                break;
            case 3:
                region = "ar";
                break;
            case 4:
                region = "tr";
                break;
        }
        Resources res = G.context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(region.toLowerCase());
        res.updateConfiguration(conf, dm);
    }

    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) G.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void forgetWifiNetworks() {
        WifiManager wifiManager = (WifiManager) G.context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            //int networkId = wifiManager.getConnectionInfo().getNetworkId();
            wifiManager.removeNetwork(i.networkId);
            wifiManager.saveConfiguration();
        }
    }

    public static boolean validateEmail(String emailStr) {
        String VALID_EMAIL_ADDRESS_REGEX = "^[_A-Za-z0-9-\\+\\.]+@[A-Za-z0-9-]+\\.[A-Za-z0-9]+";
        return emailStr.matches(VALID_EMAIL_ADDRESS_REGEX);
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                G.log("Error Download Image From Url : " + e.getMessage());
                G.printStackTrace(e);
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public static String getApplicationVersionCode() {
        try {
            PackageInfo info = G.context.getPackageManager().getPackageInfo(G.context.getPackageName(), 0);
            String ver = String.valueOf(info.versionCode);
            return ver;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*******
     * Jahanbin
     ***********/
    public static String getApplicationVersionName() {
        try {
            PackageInfo info = G.context.getPackageManager().getPackageInfo(G.context.getPackageName(), 0);
            String ver = String.valueOf(info.versionName);
            return ver;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*******
     * Jahanbin
     ***********/

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model.toUpperCase();
        }
        return manufacturer.toUpperCase() + " " + model;
    }

    public static String getDateFormated() {

        SimpleDateFormat sDate = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sDate.format(new Date());
        return date;
    }

    static boolean deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        return fileOrDirectory.delete();

    }


}
