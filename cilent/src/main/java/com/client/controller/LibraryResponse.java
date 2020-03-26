package com.client.controller;

import com.client.bean.LibrairieBean;
import lombok.Data;

import java.util.List;

@Data
public class LibraryResponse {

    private List <LibrairieBean> content;


}
