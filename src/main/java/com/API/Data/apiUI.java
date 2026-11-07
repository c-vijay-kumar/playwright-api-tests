package com.API.Data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;


@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
@JsonIgnoreProperties(ignoreUnknown = true)
public class apiUI {
    private String id;
    private String name;
    private String description;
    private String price;

}
