package net.xdevelop.template.helloworld.spike.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdevelop.template.helloworld.spike.entity.OrderEntity;
import net.xdevelop.template.helloworld.spike.entity.SeckillEntity;
import net.xdevelop.template.helloworld.spike.mapper.OrderMapper;
import net.xdevelop.template.helloworld.spike.mapper.SeckillMapper;
import net.xdevelop.template.helloworld.utils.GenerateToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class SpikeService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private GenerateToken generateToken;
    @Autowired
    private SpikeProducer spikeProducer;

    public String spikeToken(Long seckillId, Long tokenQuantity) {
        // 1.验证参数
        if (seckillId == null) {
            return ("商品库存id不能为空!");
        }
        if (tokenQuantity == null) {
            return ("token数量不能为空!");
        }
        SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity == null) {
            return ("商品信息不存在!");
        }
        // 2.使用多线程异步生产令牌
        createSeckillToken(seckillId, tokenQuantity);
        return ("令牌正在生成中.....");
    }

    @Async
    public void createSeckillToken(Long seckillId, Long tokenQuantity) {
        generateToken.createListToken("seckill_", seckillId + "", tokenQuantity);
    }

    public String spikeDo(String phone, Long seckillId) {
        // 1.参数验证
        if (StringUtils.isEmpty(phone)) {
            return ("手机号码不能为空!");
        }
        if (seckillId == null) {
            return ("商品库存id不能为空!");
        }
        // 2.从redis从获取对应的秒杀token
        String seckillToken = generateToken.getListKeyToken(seckillId + "");
        if (StringUtils.isEmpty(seckillToken)) {
            log.info(">>>seckillId:{}, 亲，该秒杀已经售空，请下次再来!", seckillId);
            return ("亲，该秒杀已经售空，请下次再来!");
        }

        // 3.获取到秒杀token之后，异步放入mq中实现修改商品的库存
        sendSeckillMsg(seckillId, phone);
        return ("正在排队中.......");
    }

    @Async
    public void sendSeckillMsg(Long seckillId, String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seckillId", seckillId);
        jsonObject.put("phone", phone);
        spikeProducer.send(jsonObject);
    }

    public String getOrder(String phone, Long seckillId) {
        if (StringUtils.isEmpty(phone)) {
            return "手机号码不能为空!";
        }
        if (seckillId == null) {
            return "商品库存id不能为空!";
        }
        List<OrderEntity> orderEntityList = orderMapper.findByOrder(phone, seckillId);
        if (orderEntityList.size() > 0) {
            return "正在排队中.....";
        }
        return "恭喜你秒杀成功!";
    }

}
