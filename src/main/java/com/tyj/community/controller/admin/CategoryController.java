package com.tyj.community.controller.admin;

import com.tyj.community.constant.WebConstant;
import com.tyj.community.controller.BaseController;
import com.tyj.community.dto.MetaDto;
import com.tyj.community.dto.Types;
import com.tyj.community.entity.RestResponseBo;
import com.tyj.community.exception.TipException;
import com.tyj.community.service.MetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by tyj on 2018/11/28.
 */
@Controller
@RequestMapping("admin/category")
public class CategoryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Resource
    private MetaService metasService;

    /**
     * 分类页
     * @param request
     * @return
     */
    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        List<MetaDto> categories = metasService.getMetaList(Types.CATEGORY.getType(), null, WebConstant.MAX_POSTS);
        List<MetaDto> tags = metasService.getMetaList(Types.TAG.getType(),  null, WebConstant.MAX_POSTS);
        request.setAttribute("categories", categories);
        request.setAttribute("tags", tags);
        return "admin/category";
    }

    @PostMapping(value = "save")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo saveCategory(@RequestParam String cname, @RequestParam Integer mid) {
        try {
            metasService.saveMeta(Types.CATEGORY.getType(),cname,mid);
        } catch (Exception e) {
            String msg = "分类保存失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    /**
     * 删除分类
     * @param mid
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam int mid) {
        try {
            metasService.delete(mid);
        } catch (Exception e) {
            String msg = "删除失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

}
