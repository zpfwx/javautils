package com.myutil;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wyu
 * @date 2021/07/12日 20:54
 */
public class VersionUtil {

    public static boolean hasNewVersion(Integer verCode, String verName, Integer lastVerCode, String lastVerName) {
        boolean hasNewVersion = false;
        if (verCode != null && lastVerCode != null) {
            hasNewVersion = (lastVerCode > verCode);
        } else if(verName != null && lastVerName != null){
            String[] vers = verName.split("\\.");
            String[] lastVers = lastVerName.split("\\.");
            int len = vers.length;
            int newLen = lastVers.length;
            int maxLen = (len > newLen ? len : newLen);
            int minLen = (len > newLen ? newLen :len);
            int iVerCode = 0;
            int iNewcode = 0;
            for(int i= 0;  i< minLen; i++) {
                iVerCode = (int)(Integer.valueOf(vers[i]));
                iNewcode = (int)(Integer.valueOf(lastVers[i]));
                if (iNewcode > iVerCode) {
                    return true;
                } else if (iNewcode < iVerCode){
                    //if sub min return false
                    return hasNewVersion;
                } else {
                    continue;
                }
            }
            if (!hasNewVersion && (newLen > len)) {
                hasNewVersion = true;
            }
        }
        return hasNewVersion;
    }

    public static boolean strCodeCompare(String verName, String lastVerName) {
        if (StringUtils.isEmpty(verName)) {
            verName = "0";
        }
        if (StringUtils.isEmpty(lastVerName)) {
            lastVerName = "0";
        }
        boolean hasNewVersion = false;
        String[] vers = verName.split("\\.");
        String[] lastVers = lastVerName.split("\\.");
        int len = vers.length;
        int newLen = lastVers.length;
        int maxLen = (len > newLen ? len : newLen);
        int minLen = (len > newLen ? newLen :len);
        int iVerCode = 0;
        int iNewcode = 0;
        for(int i= 0;  i< minLen; i++) {
            iVerCode = (int)(Integer.valueOf(vers[i]));
            iNewcode = (int)(Integer.valueOf(lastVers[i]));
            if (iNewcode > iVerCode) {
                return true;
            } else if (iNewcode < iVerCode){
                //if sub min return false
                return hasNewVersion;
            } else {
                continue;
            }
        }
        if (!hasNewVersion && (newLen > len)) {
            hasNewVersion = true;
        }
        return hasNewVersion;
    }

    public static void main(String[] args) {
        String version = "1.6.1";
        String lastVersion = "1.12.2";
        boolean brt = strCodeCompare(version,lastVersion);
        System.out.println(brt);
    }
}
