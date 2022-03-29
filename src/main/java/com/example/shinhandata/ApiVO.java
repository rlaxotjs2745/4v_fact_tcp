package com.example.shinhandata;

import lombok.Data;
import java.util.Date;


@Data
public class ApiVO {
    int idx_shinhandata;
    String rec_data;
    Date rec_reg_dt;
    Date reg_dt;
}
