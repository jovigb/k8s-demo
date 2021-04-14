package net.xdevelop.template.helloworld;

import lombok.extern.slf4j.Slf4j;
import net.xdevelop.template.helloworld.spike.service.SpikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloController {
    @Autowired
    IVersionService versionService;
    @Autowired
    SpikeService spikeService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        String version = versionService.getVersion();
        return "Hello " + name + " version: " + version;
    }

    @RequestMapping("/addSpikeToken")
    public ResponseEntity<Object> addSpikeToken(Long seckillId, Long tokenQuantity) {
        return ResponseEntity.ok(spikeService.spikeToken(seckillId, tokenQuantity));
    }

    @RequestMapping("/spike")
    public ResponseEntity<Object> spike(String phone, Long seckillId) {
        return ResponseEntity.ok(spikeService.spikeDo(phone, seckillId));
    }

    @RequestMapping("/order")
    public ResponseEntity<Object> order(String phone, Long seckillId) {
        return ResponseEntity.ok(spikeService.getOrder(phone, seckillId));
    }

}
