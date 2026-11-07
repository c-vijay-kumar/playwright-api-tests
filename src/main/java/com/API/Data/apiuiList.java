package com.API.Data;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
@Data 
@NoArgsConstructor 
@JsonIgnoreProperties(ignoreUnknown = true)
public class apiuiList {

    @JsonProperty("current_page")
    private int currentPage;

    private  List<apiUI> data;

}
