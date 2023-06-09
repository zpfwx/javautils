/*
 *  PHEX - The pure-java Gnutella-servent.
 *  Copyright (C) 2001 - 2011 Phex Development Group
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 *  --- SVN Information ---
 *  $Id: Ip2CountryDB.java 4525 2011-06-27 15:05:39Z gregork $
 */
package com.myutil.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Ip2CountryDB {
    private static final Logger logger = LoggerFactory.getLogger(Ip2CountryDB.class);
    /**
     * Indicates if the database is fully loaded or not.
     */
    private boolean isLoaded;
    private List<IpCountryRange> ipCountryRangeList;

    private Ip2CountryDB() {
        isLoaded = false;
        ipCountryRangeList = new ArrayList<>();

        Runnable runnable = this::loadIp2CountryDB;

        // TODO block job from execution until Phex initialization is finished.
        Environment.getInstance().executeOnThreadPool(runnable, "IP2CountryLoader");
    }

    /**
     * Returns the country code if found, empty string if not found, and null
     * if DB has not been loaded yet.
     *
     * @param address
     * @return the country code or null;
     */
    public static String getCountryCode(String address) {
        return Holder.manager.getCountryCodeInt(address);
    }

    /**
     * Returns the country code if found, empty string if not found, and null
     * if DB has not been loaded yet.
     *
     * @param address
     * @return the country code or null;
     */
    private String getCountryCodeInt(String address) {
        if (!isLoaded) {
            return null;
        }
        IpCountryRange range = binarySearch(address.getBytes());
        if (range == null) {
            return "";
        }
        return range.countryCode;
    }

    private void loadIp2CountryDB() {
        InputStream inStream = ClassLoader.getSystemResourceAsStream(
                "phex/resources/ip2country.csv");
        if (inStream == null) {
            logger.debug("Default GWebCache file not found.");
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        ArrayList<IpCountryRange> initialList = new ArrayList<>(5000);
        IpCountryRange range;
        String line;
        try {
            line = reader.readLine();
            while (line != null) {
                range = new IpCountryRange(line);
                initialList.add(range);
                line = reader.readLine();
            }
        } catch (IOException exp) {
            logger.error(exp.toString(), exp);
        } finally {
            try {
                reader.close();
            }catch (Exception exception){

            }

        }
        initialList.trimToSize();
        Collections.sort(initialList);
        ipCountryRangeList = Collections.unmodifiableList(initialList);
        isLoaded = true;
    }

    private IpCountryRange binarySearch(byte[] hostIp) {
        int low = 0;
        int high = ipCountryRangeList.size() - 1;

        while (low <= high) {
            int mid = (low + high) >> 1;
            IpCountryRange midVal = ipCountryRangeList.get(mid);
            int cmp = midVal.compareHostAddress(hostIp);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return midVal; // key found
            }
        }
        return null;  // key not found
    }

    static private class Holder {
        static protected final Ip2CountryDB manager = new Ip2CountryDB();
    }

    private static class IpCountryRange implements Comparable<IpCountryRange> {
        final int from;
        final int to;
        final String countryCode;

        public IpCountryRange(String line) {
            // "33996344","33996351","GB"
            int startIdx, endIdx;
            startIdx = 0;

            endIdx = line.indexOf(',', startIdx);
            from = Integer.parseInt(line.substring(startIdx, endIdx));

            startIdx = endIdx + 1;
            endIdx = line.indexOf(',', startIdx);
            to = Integer.parseInt(line.substring(startIdx, endIdx));

            startIdx = endIdx + 1;
            String subCode = line.substring(startIdx);
            // take the internal string representation of the country code to 
            // save memory...
            countryCode = subCode.intern();
        }

        public int compareHostAddress(byte[] hostIp) {
            long hostIpL;
            hostIpL = unsignedInt2Long(
                    deserializeInt(hostIp, 0));
            long fromIpL = unsignedInt2Long(from);
            long cmp = hostIpL - fromIpL;
            if (cmp == 0) {
                return 0;
            }
            if (cmp < 0) {// host Ip is lower..
                return 1;
            }

            // validate to range..
            long toIpL = unsignedInt2Long(to);
            cmp = hostIpL - toIpL;
            if (cmp == 0 || cmp < 0) {// we are between from and to
                return 0;
            } else {// host Ip is higher..
                return -1;
            }
        }

        public int compareTo(IpCountryRange range) {
            if (range == this) {
                return 0;
            }

            long ip1l = unsignedInt2Long(from);
            long ip2l = unsignedInt2Long(range.from);

            if (ip1l < ip2l) {
                return -1;
            }
            // only if rate and object is equal return 0
            else {
                return 1;
            }
        }


        public static   long unsignedInt2Long(int x) {
            return x & 0xFFFFFFFFL;
        }

        public static int deserializeInt(byte[] inbuf, int offset) {
            return (inbuf[offset]) << 24 |
                    (inbuf[offset + 1] & 0xff) << 16 |
                    (inbuf[offset + 2] & 0xff) << 8 |
                    (inbuf[offset + 3] & 0xff);
        }
    }
}