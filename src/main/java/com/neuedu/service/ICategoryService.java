package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICategoryService {
    public ServerResponse get_category(Integer categoryId);
    public ServerResponse get_deep_category(Integer categoryId);
}
