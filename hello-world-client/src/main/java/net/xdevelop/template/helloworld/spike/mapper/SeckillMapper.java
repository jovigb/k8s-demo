package net.xdevelop.template.helloworld.spike.mapper;

import net.xdevelop.template.helloworld.spike.entity.SeckillEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface SeckillMapper {

    @Select("SELECT seckill_id AS seckillId,name as name,inventory as inventory,start_time as startTime,end_time as endTime,create_time as createTime,version as version from seckill where seckill_id=#{seckillId}")
    SeckillEntity findBySeckillId(Long seckillId);

    /**
     * 悲观锁防止超卖
     *
     * @param seckillId
     * @return
     */
    @Update("update seckill set inventory=inventory-1 where  seckill_id=#{seckillId} and inventory>0;")
    int pessimisticDeduction(@Param("seckillId") Long seckillId);

    /**
     * 乐观锁防止超卖
     *
     * @param seckillId
     * @param version
     * @return
     */
    @Update("update seckill set inventory=inventory-1, version=version+1 where  seckill_id=#{seckillId} and inventory>0  and version=#{version} ;")
    int optimisticDeduction(@Param("seckillId") Long seckillId, @Param("version") Long version);

}