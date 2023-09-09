package vip.sujianfeng.redis.ranking;

import vip.sujianfeng.redis.TbRedisCache;

import java.util.List;
import java.util.Set;

/**
 * 通过redis处理实时排行榜
 * author SuJianFeng
 * @create 2020-08-29 6:58
 */
public class TbRedisRanking {

    /**
     * 更新排行榜成员
     * @param rankingMembers
     */
    public void addOrUpdateMembers(RankingMember... rankingMembers){
        for (RankingMember rankingMember : rankingMembers) {
            addOrUpdateMember(rankingMember.getMemberName(), rankingMember.getScore());
        }
    }

    public <T extends RankingMember> void addOrUpdateMembers(List<T> rankingMembers){
        for (T rankingMember : rankingMembers) {
            addOrUpdateMember(rankingMember.getMemberName(), rankingMember.getScore());
        }
    }

    public Long addOrUpdateMember(String member, double score){
        return this.tbRedisCache.accessJedis(jedis -> jedis.zadd(rangName, score, member));
    }

    /**
     * 取排行榜前几名
     * @param limit
     * @return
     */
    public Set<String> getTopMembers(int limit){
        //按照score降序
        return this.tbRedisCache.accessJedis(jedis -> jedis.zrevrange(rangName, 0, limit));
    }

    /**
     * 取得大于指定分数的最近几笔
     * @param score
     * @param limit
     * @return
     */
    public Set<String> getGreaterThan(double score, int limit){
        return this.tbRedisCache.accessJedis(jedis -> jedis.zrangeByScore(this.rangName, score, Double.MAX_VALUE, 0, limit));
    }

    /**
     * 清空排行榜
     */
    public void clear(){
        this.tbRedisCache.accessJedis(jedis -> jedis.del(rangName));
    }

    /**
     * 取得小于指定分数的最近几笔
     * @param score
     * @param limit
     * @return
     */
    public Set<String> getLessThan(double score, int limit){
        return this.tbRedisCache.accessJedis(jedis -> jedis.zrevrangeByScore(this.rangName, score, Double.MIN_VALUE, 0, limit));
    }

    /**
     * 取得指定成员的分数
     * @param member
     * @return
     */
    public double getScore(String member){
        return this.tbRedisCache.accessJedis(jedis -> jedis.zscore(this.rangName, member));
    }

    /**
     * 取得指定成员的排名
     * -1表示不存在
     * @param member
     * @return
     */
    public long getSortIndex(String member){
        return this.tbRedisCache.accessJedis(jedis -> {
            Double zscore = jedis.zscore(this.rangName, member);
            if (zscore == null){
                return -1L;
            }
            return jedis.zrevrank(this.rangName, member) + 1;
        });
    }

    public TbRedisRanking(TbRedisCache tbRedisCache, String rangName) {
        this.tbRedisCache = tbRedisCache;
        this.rangName = rangName;
    }

    private TbRedisCache tbRedisCache;

    /**
     * 排行榜名称
     */
    private String rangName;


    public TbRedisCache getTbRedisCache() {
        return tbRedisCache;
    }

    public void setTbRedisCache(TbRedisCache tbRedisCache) {
        this.tbRedisCache = tbRedisCache;
    }

    public String getRangName() {
        return rangName;
    }

    public void setRangName(String rangName) {
        this.rangName = rangName;
    }
}
