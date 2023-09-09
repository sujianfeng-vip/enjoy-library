package vip.sujianfeng.redis.ranking;

/**
 * author SuJianFeng
 * @create 2020-08-29 7:02
 */
public interface RankingMember {
    double getScore();
    void setScore(double value);
    String getMemberName();
    void setMemberName(String value);
}
