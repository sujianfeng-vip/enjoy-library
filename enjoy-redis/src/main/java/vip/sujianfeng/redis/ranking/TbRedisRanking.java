package vip.sujianfeng.redis.ranking;

import vip.sujianfeng.redis.TbRedisCache;

import java.util.List;
import java.util.Set;

/**
 * author SuJianFeng
 * create 2020-08-29 6:58
 */
public class TbRedisRanking {

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

    public Set<String> getTopMembers(int limit){
        //按照score降序
        return this.tbRedisCache.accessJedis(jedis -> jedis.zrevrange(rangName, 0, limit));
    }

    public Set<String> getGreaterThan(double score, int limit){
        return this.tbRedisCache.accessJedis(jedis -> jedis.zrangeByScore(this.rangName, score, Double.MAX_VALUE, 0, limit));
    }

    public void clear(){
        this.tbRedisCache.accessJedis(jedis -> jedis.del(rangName));
    }

    public Set<String> getLessThan(double score, int limit){
        return this.tbRedisCache.accessJedis(jedis -> jedis.zrevrangeByScore(this.rangName, score, Double.MIN_VALUE, 0, limit));
    }

    public double getScore(String member){
        return this.tbRedisCache.accessJedis(jedis -> jedis.zscore(this.rangName, member));
    }

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
