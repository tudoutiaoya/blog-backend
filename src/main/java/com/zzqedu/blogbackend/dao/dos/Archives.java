package com.zzqedu.blogbackend.dao.dos;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Archives {

    private Integer year;

    private Integer month;

    private Integer count;

}
