package cn.waynechu.seckill.web;

import cn.waynechu.seckill.dto.Exposer;
import cn.waynechu.seckill.dto.SeckillExecution;
import cn.waynechu.seckill.dto.SeckillResult;
import cn.waynechu.seckill.entity.Seckill;
import cn.waynechu.seckill.enums.SeckillStatEnum;
import cn.waynechu.seckill.exception.RepeatKillException;
import cn.waynechu.seckill.exception.SeckillCloseException;
import cn.waynechu.seckill.exception.SeckillException;
import cn.waynechu.seckill.service.SeckillService;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by waynechu on 2017-07-15.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /**
     * 列表页的请求处理
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        System.err.println("----------------------------reload---------------------");
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        //list.jsp + model = ModelAndView
        return "list";  //WEB-INF/jsp/"list"    .jsp
    }

    /**
     * 详情页的请求处理
     *
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {

        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getBySeckillId(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 获取ajax请求并返回json结果
     *
     * @param seckillId
     */
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    //@ResponseBody 表示该方法的返回结果直接写入HTTP
    //比如异步获取json数据，加上@Responsebody后，会直接返回json数据
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

//    @RequestMapping(value = "/{seckillId}/exposer",
//            method = RequestMethod.GET,
//            produces = {"application/json;charset=UTF-8"})
//    //@ResponseBody 表示该方法的返回结果直接写入HTTP
//    //比如异步获取json数据，加上@Responsebody后，会直接返回json数据
//    @ResponseBody
//    public Map<String,Object> exposer(@PathVariable("seckillId") Long seckillId) {
//        Map<String, Object> result = new HashMap<String, Object>();
//        try {
//            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
//            result.put("true",exposer);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            result.put("false",e.getMessage());
//        }
//        return result;
//    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long phone) {
        //验证参数
        if (phone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e1) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e2) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (Exception e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }
}
