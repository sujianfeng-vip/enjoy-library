package vip.sujianfeng.utils.enums;

import vip.sujianfeng.utils.system.SystemEnvUtils;

/**
 * author SuJianFeng
 * createTime  2019/2/15 10:29
 **/
public enum OSPlatformEnum {
    Any("any"),
    Linux("Linux"),
    Mac_OS("Mac OS"),
    Mac_OS_X("Mac OS X"),
    Windows("Windows"),
    OS2("OS/2"),
    Solaris("Solaris"),
    SunOS("SunOS"),
    MPEiX("MPE/iX"),
    HP_UX("HP-UX"),
    AIX("AIX"),
    OS390("OS/390"),
    FreeBSD("FreeBSD"),
    Irix("Irix"),
    Digital_Unix("Digital Unix"),
    NetWare_411("NetWare"),
    OSF1("OSF1"),
    OpenVMS("OpenVMS"),
    Others("Others");

    private static OSPlatformEnum CURR_OS = null;


    public static OSPlatformEnum currOS(){
        if (CURR_OS == null){
            CURR_OS = OSPlatformEnum.Others;
            String osName = SystemEnvUtils.getSystemProperty("os.name");
            for (OSPlatformEnum os: OSPlatformEnum.values()){
                if (osName.contains(os.osName)){
                    CURR_OS = os;
                    break;
                }
            }
        }
        return CURR_OS;
    }

    OSPlatformEnum(String osName){
        this.osName = osName;
    }

    private String osName;

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }
}
