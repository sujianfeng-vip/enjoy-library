package vip.sujianfeng.enums;

/**
 * author SuJianFeng
 * createTime  2022/10/8
 * @Description
 **/
public enum EnvType {
    DEV("dev"), TEST("test"), PROD("prod"), TASK("task");

    public static EnvType valueOfProfile(String profile) {
        for (EnvType value : EnvType.values()) {
            if (value.profile.equalsIgnoreCase(profile)) {
                return value;
            }
        }
        return DEV;
    }

    EnvType(String profile) {
        this.profile = profile;
    }

    private String profile;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
