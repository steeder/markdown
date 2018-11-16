package com.app.controller;

import com.app.service.GeneratorService;
import com.app.utils.PageUtils;
import com.app.utils.Query;
import com.app.utils.R;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("generator")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    /**
     * 列表
     */
    @GetMapping
    public R list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = generatorService.queryList(query);
        int total = generatorService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     */
    @GetMapping("download")
    public void download(String tables, HttpServletResponse response) throws IOException {
        byte[] data = generatorService.generator(tables.split(","));
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"markdown.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }
}
