package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import org.springframework.web.bind.annotation.RequestParam;

public interface IProductService {
    public ServerResponse list(Integer categoryId,
                               String keyword,
                               Integer pageNum,
                               Integer pageSize,
                               String orderBy);
    public ServerResponse detail_portal(Integer productId);

    public ServerResponse topcategory(Integer sid);
}
