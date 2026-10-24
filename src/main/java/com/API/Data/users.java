package com.API.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data                       // getters, setters, toString, equals, hashCode
@Builder     
@NoArgsConstructor               
@AllArgsConstructor         // needed by @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class users {
    private String name;
    private String email;
    private String gender;
    private String status;
    private  String id;

}
